package com.plus33.erp.manufacturing.service.impl;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.InventoryStock;
import com.plus33.erp.inventory.repository.InventoryStockRepository;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.inventory.repository.UnitOfMeasureRepository;
import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.entity.*;
import com.plus33.erp.manufacturing.repository.*;
import com.plus33.erp.manufacturing.service.ManufacturingCalendarService;
import com.plus33.erp.manufacturing.service.MrpService;
import com.plus33.erp.manufacturing.service.ProductionOrderService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Service
@Transactional
public class MrpServiceImpl implements MrpService {

    private final MrpRunRepository mrpRunRepository;
    private final MrpPlannedOrderRepository mrpPlannedOrderRepository;
    private final BomHeaderRepository bomHeaderRepository;
    private final ProductRepository productRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final MrpPeggingLinkRepository mrpPeggingLinkRepository;
    private final ProductionOrderService productionOrderService;
    private final ManufacturingCalendarService calendarService;
    private final ApplicationEventPublisher eventPublisher;
    private final RoutingHeaderRepository routingHeaderRepository;
    private final CapacityPlanRepository capacityPlanRepository;
    private final WorkCenterRepository workCenterRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors())
    );

    public MrpServiceImpl(MrpRunRepository mrpRunRepository,
                          MrpPlannedOrderRepository mrpPlannedOrderRepository,
                          BomHeaderRepository bomHeaderRepository,
                          ProductRepository productRepository,
                          UnitOfMeasureRepository unitOfMeasureRepository,
                          InventoryStockRepository inventoryStockRepository,
                          MrpPeggingLinkRepository mrpPeggingLinkRepository,
                          ProductionOrderService productionOrderService,
                          ManufacturingCalendarService calendarService,
                          ApplicationEventPublisher eventPublisher,
                          RoutingHeaderRepository routingHeaderRepository,
                          CapacityPlanRepository capacityPlanRepository,
                          WorkCenterRepository workCenterRepository) {
        this.mrpRunRepository = mrpRunRepository;
        this.mrpPlannedOrderRepository = mrpPlannedOrderRepository;
        this.bomHeaderRepository = bomHeaderRepository;
        this.productRepository = productRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.inventoryStockRepository = inventoryStockRepository;
        this.mrpPeggingLinkRepository = mrpPeggingLinkRepository;
        this.productionOrderService = productionOrderService;
        this.calendarService = calendarService;
        this.eventPublisher = eventPublisher;
        this.routingHeaderRepository = routingHeaderRepository;
        this.capacityPlanRepository = capacityPlanRepository;
        this.workCenterRepository = workCenterRepository;
    }

    @Override
    public List<MrpSuggestionDto> runMrp(MrpRunRequest request) {
        MrpRun run = new MrpRun();
        run.setCompanyId(request.getCompanyId());
        run.setRunNumber("MRP-" + System.currentTimeMillis());
        run.setPlanningHorizonDays(request.getPlanningHorizonDays() != null ? request.getPlanningHorizonDays() : 90);
        run.setStatus("IN_PROGRESS");
        run.setExecutedBy(request.getExecutedBy() != null ? request.getExecutedBy() : 1L);
        run = mrpRunRepository.save(run);

        List<MrpPlannedOrder> generatedOrders = new CopyOnWriteArrayList<>();
        List<MrpPeggingLink> peggingLinks = new CopyOnWriteArrayList<>();

        if (request.getDemandItems() != null) {
            for (MrpRunRequest.DemandItem demand : request.getDemandItems()) {
                processDemandItem(demand, request, run, generatedOrders, peggingLinks);
            }
        }

        run.setItemsProcessed(generatedOrders.size());
        run.setOrdersGenerated(generatedOrders.size());
        run.setStatus("COMPLETED");
        run.setCompletedAt(LocalDateTime.now());
        mrpRunRepository.save(run);

        // Publish MRP event
        eventPublisher.publishEvent(new com.plus33.erp.manufacturing.entity.ManufacturingEventBus.MrpRunCompletedEvent(run.getId(), request.getCompanyId()));

        return generatedOrders.stream().map(this::mapToSuggestionDto).toList();
    }

    private void processDemandItem(MrpRunRequest.DemandItem demand,
                                   MrpRunRequest request,
                                   MrpRun run,
                                   List<MrpPlannedOrder> generatedOrders,
                                   List<MrpPeggingLink> peggingLinks) {
        Product product = productRepository.findById(demand.getProductId()).orElseThrow();
        BigDecimal currentStock = inventoryStockRepository.findByProductId(demand.getProductId()).stream()
                .map(s -> s.getQuantity().subtract(s.getReservedQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netQty = demand.getQuantity().subtract(currentStock);
        if (netQty.compareTo(BigDecimal.ZERO) <= 0) {
            return; // Stock is sufficient
        }

        // Create main supply planned order
        MrpPlannedOrder mainOrder = new MrpPlannedOrder();
        mainOrder.setMrpRun(run);
        mainOrder.setCompanyId(request.getCompanyId());
        mainOrder.setProduct(product);
        mainOrder.setOrderType("MANUFACTURE");
        mainOrder.setQuantity(netQty);
        mainOrder.setRequiredDate(demand.getDueDate());
        mainOrder.setSuggestedDueDate(demand.getDueDate());
        mainOrder.setStatus(PlannedOrderStatus.OPEN);
        mainOrder.setSourceType(demand.getSourceType() != null ? demand.getSourceType() : "FORECAST");
        mainOrder.setSourceReferenceId(demand.getSourceReferenceId());
        mainOrder.setUnit(product.getUnit());

        // Perform finite scheduling
        schedulePlannedOrderFinitely(mainOrder, request.getCompanyId(), run);

        mainOrder = mrpPlannedOrderRepository.save(mainOrder);

        synchronized (generatedOrders) {
            generatedOrders.add(mainOrder);
        }

        // Create pegging link
        MrpPeggingLink pegging = new MrpPeggingLink();
        pegging.setMrpRun(run);
        pegging.setCompanyId(request.getCompanyId());
        pegging.setDemandType(demand.getSourceType() != null ? demand.getSourceType() : "FORECAST");
        pegging.setDemandId(demand.getSourceReferenceId() != null ? demand.getSourceReferenceId() : 0L);
        pegging.setSupplyType("MANUFACTURE");
        pegging.setPeggedQuantity(netQty);
        pegging.setSupplyId(mainOrder.getId());
        mrpPeggingLinkRepository.save(pegging);

        synchronized (peggingLinks) {
            peggingLinks.add(pegging);
        }

        // Explode BOM
        List<BomHeader> activeBoms = bomHeaderRepository.findActiveBomForProduct(request.getCompanyId(), demand.getProductId(), LocalDate.now());
        if (!activeBoms.isEmpty()) {
            BomHeader bom = activeBoms.get(0);
            mainOrder.setBomHeaderId(bom.getId());
            BigDecimal multiplier = netQty.divide(bom.getBaseQuantity(), 6, RoundingMode.HALF_UP);

            for (BomLine line : bom.getLines()) {
                BigDecimal componentQty = line.getQuantity().multiply(multiplier);
                Product component = line.getComponentProduct();

                MrpPlannedOrder compOrder = new MrpPlannedOrder();
                compOrder.setMrpRun(run);
                compOrder.setCompanyId(request.getCompanyId());
                compOrder.setProduct(component);
                compOrder.setOrderType("PURCHASE");
                compOrder.setQuantity(componentQty);
                compOrder.setRequiredDate(mainOrder.getSuggestedStartDate());
                compOrder.setSuggestedDueDate(mainOrder.getSuggestedStartDate());
                compOrder.setStatus(PlannedOrderStatus.OPEN);
                compOrder.setSourceType("BOM_REQUIREMENT");
                compOrder.setUnit(component.getUnit());

                // If component is also manufactured, schedule finitely; otherwise use standard lead time
                List<BomHeader> compBoms = bomHeaderRepository.findActiveBomForProduct(request.getCompanyId(), component.getId(), LocalDate.now());
                if (!compBoms.isEmpty()) {
                    compOrder.setOrderType("MANUFACTURE");
                    schedulePlannedOrderFinitely(compOrder, request.getCompanyId(), run);
                } else {
                    LocalDate compDueDate = mainOrder.getSuggestedStartDate();
                    LocalDate compStart = scheduleBackwards(request.getCompanyId(), compDueDate, 14);
                    compOrder.setSuggestedStartDate(compStart);
                }

                compOrder = mrpPlannedOrderRepository.save(compOrder);

                synchronized (generatedOrders) {
                    generatedOrders.add(compOrder);
                }

                // Peg component supply to the main order
                MrpPeggingLink compPeg = new MrpPeggingLink();
                compPeg.setMrpRun(run);
                compPeg.setCompanyId(request.getCompanyId());
                compPeg.setDemandType("MANUFACTURE_PLANNED");
                compPeg.setDemandId(mainOrder.getId());
                compPeg.setSupplyType(compOrder.getOrderType());
                compPeg.setPeggedQuantity(componentQty);
                compPeg.setSupplyId(compOrder.getId());
                mrpPeggingLinkRepository.save(compPeg);

                synchronized (peggingLinks) {
                    peggingLinks.add(compPeg);
                }
            }
        }
    }

    private void schedulePlannedOrderFinitely(MrpPlannedOrder plannedOrder, Long companyId, MrpRun run) {
        List<RoutingHeader> activeRoutings = routingHeaderRepository.findActiveByProductAndDate(companyId, plannedOrder.getProduct().getId(), LocalDate.now());
        if (activeRoutings.isEmpty()) {
            // Keep default backward scheduling
            LocalDate suggestedStart = scheduleBackwards(companyId, plannedOrder.getSuggestedDueDate(), 7);
            plannedOrder.setSuggestedStartDate(suggestedStart);
            return;
        }

        RoutingHeader routing = activeRoutings.get(0);
        plannedOrder.setRoutingHeaderId(routing.getId());

        LocalDate currentDate = plannedOrder.getSuggestedDueDate();
        // Backward schedule operations
        for (int i = routing.getOperations().size() - 1; i >= 0; i--) {
            RoutingOperation ro = routing.getOperations().get(i);
            WorkCenter wc = ro.getWorkCenter();

            BigDecimal requiredHours = ro.getSetupTimeHours().add(
                    ro.getRunTimePerUnitHours().multiply(plannedOrder.getQuantity())
            );

            LocalDate scheduledDate = findCapacityDate(companyId, wc, currentDate, requiredHours, run);
            currentDate = scheduledDate;
        }
        plannedOrder.setSuggestedStartDate(currentDate);
    }

    private LocalDate findCapacityDate(Long companyId, WorkCenter wc, LocalDate startDate, BigDecimal requiredHours, MrpRun mrpRun) {
        LocalDate current = startDate;
        int maxAttempts = 100;
        while (maxAttempts > 0) {
            maxAttempts--;
            BigDecimal available = calendarService.getAvailableHours(companyId, "WORK_CENTER", wc.getId(), current);
            if (available.compareTo(BigDecimal.ZERO) <= 0) {
                current = current.minusDays(1);
                continue;
            }

            List<CapacityPlan> existing = capacityPlanRepository.findByWorkCenterIdAndPlanningDate(wc.getId(), current);
            BigDecimal loadedHours = existing.stream()
                    .map(CapacityPlan::getRequiredHours)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (loadedHours.add(requiredHours).compareTo(available) <= 0) {
                CapacityPlan plan = new CapacityPlan();
                plan.setMrpRun(mrpRun);
                plan.setWorkCenter(wc);
                plan.setPlanningDate(current);
                plan.setAvailableHours(available);
                plan.setRequiredHours(loadedHours.add(requiredHours));
                plan.setOverloaded(false);
                plan.setMachineUtilizationPct(loadedHours.add(requiredHours).divide(available, 4, RoundingMode.HALF_UP));
                plan.setLaborUtilizationPct(loadedHours.add(requiredHours).divide(available, 4, RoundingMode.HALF_UP));
                capacityPlanRepository.save(plan);
                return current;
            }

            // If overloaded, log overloaded capacity plan and move to previous day
            CapacityPlan plan = new CapacityPlan();
            plan.setMrpRun(mrpRun);
            plan.setWorkCenter(wc);
            plan.setPlanningDate(current);
            plan.setAvailableHours(available);
            plan.setRequiredHours(loadedHours.add(requiredHours));
            plan.setOverloaded(true);
            plan.setMachineUtilizationPct(loadedHours.add(requiredHours).divide(available, 4, RoundingMode.HALF_UP));
            plan.setLaborUtilizationPct(loadedHours.add(requiredHours).divide(available, 4, RoundingMode.HALF_UP));
            capacityPlanRepository.save(plan);

            current = current.minusDays(1);
        }
        return current;
    }

    private LocalDate scheduleBackwards(Long companyId, LocalDate dueDate, int durationDays) {
        LocalDate current = dueDate;
        int remainingDays = durationDays;
        while (remainingDays > 0) {
            current = current.minusDays(1);
            BigDecimal available = calendarService.getAvailableHours(companyId, "PLANT", 0L, current);
            if (available.compareTo(BigDecimal.ZERO) > 0) {
                remainingDays--;
            }
        }
        return current;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MrpSuggestionDto> getMrpSuggestionsByCompany(Long companyId) {
        List<MrpRun> runs = mrpRunRepository.findByCompanyIdOrderByCreatedAtDesc(companyId);
        if (runs.isEmpty()) {
            return Collections.emptyList();
        }
        return mrpPlannedOrderRepository.findByMrpRunId(runs.get(0).getId()).stream()
                .map(this::mapToSuggestionDto).toList();
    }

    @Override
    public MrpRunDto executeMrpRun(ExecuteMrpRunRequest request) {
        MrpRunRequest runReq = new MrpRunRequest();
        runReq.setCompanyId(request.getCompanyId());
        runReq.setNotes("Executed via Run API");
        runReq.setExecutedBy(request.getInitiatedBy());
        runReq.setPlanningHorizonDays(90);

        List<MrpSuggestionDto> suggestions = runMrp(runReq);

        MrpRun run = mrpRunRepository.findAll().stream()
                .filter(r -> r.getCompanyId().equals(request.getCompanyId()))
                .max(Comparator.comparing(MrpRun::getCreatedAt))
                .orElseThrow();

        return mapRunToDto(run);
    }

    @Override
    @Transactional(readOnly = true)
    public MrpRunDto getMrpRun(Long companyId, Long runId) {
        MrpRun run = mrpRunRepository.findById(runId)
                .orElseThrow(() -> new NoSuchElementException("MRP Run not found"));
        return mapRunToDto(run);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MrpRunDto> getMrpRunHistory(Long companyId) {
        return mrpRunRepository.findByCompanyIdOrderByCreatedAtDesc(companyId)
                .stream().map(this::mapRunToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MrpPlannedOrderDto> getPlannedOrders(Long companyId, Long runId) {
        return mrpPlannedOrderRepository.findByMrpRunId(runId).stream()
                .map(this::mapToPlannedOrderDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MrpPlannedOrderDto> getActionablePlannedOrders(Long companyId, LocalDate from, LocalDate to) {
        return mrpPlannedOrderRepository.findActionableByHorizon(companyId, from, to).stream()
                .map(this::mapToPlannedOrderDto).toList();
    }

    @Override
    public MrpPlannedOrderDto firmPlannedOrder(Long companyId, Long plannedOrderId, Long userId) {
        MrpPlannedOrder plannedOrder = mrpPlannedOrderRepository.findById(plannedOrderId)
                .orElseThrow(() -> new NoSuchElementException("Planned Order not found"));
        plannedOrder.setFirmed(true);
        plannedOrder.setStatus(PlannedOrderStatus.FIRMED);
        return mapToPlannedOrderDto(mrpPlannedOrderRepository.save(plannedOrder));
    }

    @Override
    public ProductionOrderDto releasePlannedOrder(Long companyId, Long plannedOrderId, Long userId) {
        MrpPlannedOrder plannedOrder = mrpPlannedOrderRepository.findById(plannedOrderId)
                .orElseThrow(() -> new NoSuchElementException("Planned Order not found"));

        if (plannedOrder.getStatus() == PlannedOrderStatus.RELEASED) {
            throw new IllegalStateException("Planned order is already released.");
        }

        CreateProductionOrderRequest req = new CreateProductionOrderRequest();
        req.setCompanyId(plannedOrder.getCompanyId());
        req.setOrderNumber("PO-MRP-" + System.currentTimeMillis());
        req.setProductId(plannedOrder.getProduct().getId());
        req.setPlannedQuantity(plannedOrder.getQuantity());
        req.setPlannedStartDate(plannedOrder.getSuggestedStartDate());
        req.setPlannedEndDate(plannedOrder.getSuggestedDueDate());
        req.setUnitId(plannedOrder.getUnit().getId());
        req.setPriority(5);
        req.setCreatedBy(userId);
        req.setNotes("Released from MRP Planned Order #" + plannedOrder.getId());

        ProductionOrderDto poDto = productionOrderService.createProductionOrder(req);

        plannedOrder.setStatus(PlannedOrderStatus.RELEASED);
        plannedOrder.setReleasedProductionOrderId(poDto.getId());
        mrpPlannedOrderRepository.save(plannedOrder);

        // Publish event
        eventPublisher.publishEvent(new com.plus33.erp.manufacturing.entity.ManufacturingEventBus.PlannedOrderReleasedEvent(plannedOrderId, poDto.getId()));

        return poDto;
    }

    @Override
    public void cancelMrpRun(Long companyId, Long runId, Long userId) {
        MrpRun run = mrpRunRepository.findById(runId)
                .orElseThrow(() -> new NoSuchElementException("MRP Run not found"));
        run.setStatus("CANCELLED");
        mrpRunRepository.save(run);
    }

    private MrpRunDto mapRunToDto(MrpRun run) {
        MrpRunDto dto = new MrpRunDto();
        dto.setId(run.getId());
        dto.setCompanyId(run.getCompanyId());
        dto.setRunNumber(run.getRunNumber());
        dto.setHorizonStartDate(LocalDate.now());
        dto.setHorizonEndDate(LocalDate.now().plusDays(run.getPlanningHorizonDays()));
        dto.setStatus(run.getStatus());
        dto.setItemsProcessed(run.getItemsProcessed());
        dto.setOrdersGenerated(run.getOrdersGenerated());
        dto.setInitiatedBy(run.getExecutedBy());
        dto.setStartedAt(run.getCreatedAt());
        dto.setCompletedAt(run.getCompletedAt());
        return dto;
    }

    private MrpPlannedOrderDto mapToPlannedOrderDto(MrpPlannedOrder po) {
        MrpPlannedOrderDto dto = new MrpPlannedOrderDto();
        dto.setId(po.getId());
        dto.setMrpRunId(po.getMrpRun().getId());
        dto.setCompanyId(po.getCompanyId());
        dto.setProductId(po.getProduct().getId());
        dto.setProductCode(po.getProduct().getCode());
        dto.setProductName(po.getProduct().getName());
        dto.setOrderType(po.getOrderType());
        dto.setQuantity(po.getQuantity());
        dto.setUnitCode(po.getUnit() != null ? po.getUnit().getCode() : null);
        dto.setReleaseDate(po.getSuggestedStartDate());
        dto.setDueDate(po.getSuggestedDueDate());
        dto.setStatus(po.getStatus() != null ? po.getStatus().name() : null);
        dto.setFirmed(po.getFirmed());
        dto.setReleasedProductionOrderId(po.getReleasedProductionOrderId());
        return dto;
    }

    private MrpSuggestionDto mapToSuggestionDto(MrpPlannedOrder po) {
        MrpSuggestionDto dto = new MrpSuggestionDto();
        dto.setId(po.getId());
        dto.setMrpRunId(po.getMrpRun().getId());
        dto.setCompanyId(po.getCompanyId());
        dto.setProductId(po.getProduct().getId());
        dto.setProductCode(po.getProduct().getCode());
        dto.setProductName(po.getProduct().getName());
        dto.setOrderType(po.getOrderType());
        dto.setQuantity(po.getQuantity());
        dto.setUnitCode(po.getUnit() != null ? po.getUnit().getCode() : null);
        dto.setReleaseDate(po.getSuggestedStartDate());
        dto.setDueDate(po.getSuggestedDueDate());
        dto.setStatus(po.getStatus() != null ? po.getStatus().name() : null);
        dto.setFirmed(po.getFirmed());
        dto.setReleasedProductionOrderId(po.getReleasedProductionOrderId());
        return dto;
    }
}
