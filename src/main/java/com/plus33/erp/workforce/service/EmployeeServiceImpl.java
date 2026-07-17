/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : EmployeeServiceImpl.java
 * Purpose           : Business logic service layer for Workforce Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeController
 * Related Service   : EmployeeServiceImpl
 * Related Repository: EmployeeRepository, CompanyRepository, RegionRepository, StoreRepository, UserRepository, UserRegionRepository, UserStoreRepository
 * Related Entity    : Employee
 * Related DTO       : EmployeeRequest, EmployeeResponse, EmployeeSearchRequest, mapToResponse, PageResponse
 * Related Mapper    : EmployeeMapper
 * Related DB Table  : employees
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module, Security Module
 * Used By           : EmployeeController, EmployeeServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Workforce Module. Implements EmployeeService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Region;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.RegionRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.workforce.dto.EmployeeRequest;
import com.plus33.erp.workforce.dto.EmployeeResponse;
import com.plus33.erp.workforce.dto.EmployeeSearchRequest;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.entity.UserRegion;
import com.plus33.erp.workforce.entity.UserStore;
import com.plus33.erp.workforce.entity.EmployeeSalaryStructure;
import com.plus33.erp.workforce.entity.EmployeeSalaryStructureItem;
import com.plus33.erp.workforce.entity.SalaryComponent;
import com.plus33.erp.workforce.entity.EmployeeShift;
import com.plus33.erp.workforce.entity.Shift;
import com.plus33.erp.workforce.entity.Attendance;
import com.plus33.erp.workforce.entity.EmployeeLeave;
import com.plus33.erp.workforce.mapper.EmployeeMapper;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.repository.UserRegionRepository;
import com.plus33.erp.workforce.repository.UserStoreRepository;
import com.plus33.erp.workforce.repository.EmployeeSalaryStructureRepository;
import com.plus33.erp.workforce.repository.EmployeeSalaryStructureItemRepository;
import com.plus33.erp.workforce.repository.SalaryComponentRepository;
import com.plus33.erp.workforce.repository.EmployeeShiftRepository;
import com.plus33.erp.workforce.repository.AttendanceRepository;
import com.plus33.erp.workforce.repository.EmployeeLeaveRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Service Implementation: business logic and transaction boundary for Workforce Module.</p>
 *
 * <p><b>Database Table   :</b> {@code employees}</p>
 * <p><b>Module Deps      :</b> Common, Organization, Security, Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final RegionRepository regionRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserRegionRepository userRegionRepository;
    private final UserStoreRepository userStoreRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeSalaryStructureRepository employeeSalaryStructureRepository;
    private final EmployeeSalaryStructureItemRepository employeeSalaryStructureItemRepository;
    private final SalaryComponentRepository salaryComponentRepository;
    private final EmployeeShiftRepository employeeShiftRepository;
    private final AttendanceRepository attendanceRepository;
    private final EmployeeLeaveRepository employeeLeaveRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               CompanyRepository companyRepository,
                               RegionRepository regionRepository,
                               StoreRepository storeRepository,
                               UserRepository userRepository,
                               UserRegionRepository userRegionRepository,
                               UserStoreRepository userStoreRepository,
                               EmployeeMapper employeeMapper,
                               EmployeeSalaryStructureRepository employeeSalaryStructureRepository,
                               EmployeeSalaryStructureItemRepository employeeSalaryStructureItemRepository,
                               SalaryComponentRepository salaryComponentRepository,
                               EmployeeShiftRepository employeeShiftRepository,
                               AttendanceRepository attendanceRepository,
                               EmployeeLeaveRepository employeeLeaveRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.regionRepository = regionRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.userRegionRepository = userRegionRepository;
        this.userStoreRepository = userStoreRepository;
        this.employeeMapper = employeeMapper;
        this.employeeSalaryStructureRepository = employeeSalaryStructureRepository;
        this.employeeSalaryStructureItemRepository = employeeSalaryStructureItemRepository;
        this.salaryComponentRepository = salaryComponentRepository;
        this.employeeShiftRepository = employeeShiftRepository;
        this.attendanceRepository = attendanceRepository;
        this.employeeLeaveRepository = employeeLeaveRepository;
    }

    /**
     * Creates a new employee and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the EmployeeResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));

        if (!company.getActive()) {
            throw new BusinessException("Cannot assign employee: Company is inactive");
        }

        if (employeeRepository.existsByCompanyIdAndEmployeeCode(request.companyId(), request.employeeCode())) {
            throw new DuplicateResourceException("Employee with code " + request.employeeCode() + " already exists in company " + company.getName());
        }

        if (request.email() != null && !request.email().isBlank()
                && employeeRepository.existsByCompanyIdAndEmail(request.companyId(), request.email())) {
            throw new DuplicateResourceException("Employee with email " + request.email() + " already exists in company " + company.getName());
        }

        Region region = null;
        if (request.regionId() != null) {
            region = regionRepository.findById(request.regionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + request.regionId()));
            if (!region.getCompany().getId().equals(request.companyId())) {
                throw new BusinessException("Cannot assign employee: Region does not belong to the selected company");
            }
        }

        Store store = null;
        if (request.storeId() != null) {
            store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.storeId()));
            if (!store.getActive()) {
                throw new BusinessException("Cannot assign employee: Store is inactive");
            }
            if (request.regionId() == null || !store.getRegion().getId().equals(request.regionId())) {
                throw new BusinessException("Cannot assign employee: Store does not belong to the selected region");
            }
            if (!store.getRegion().getCompany().getId().equals(request.companyId())) {
                throw new BusinessException("Cannot assign employee: Store does not belong to the selected company");
            }
        }

        User user = null;
        if (request.userId() != null) {
            user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User account not found with ID: " + request.userId()));
            if (!user.getActive()) {
                throw new BusinessException("Cannot assign employee: User account is inactive");
            }
            if (employeeRepository.existsByUserId(request.userId())) {
                throw new DuplicateResourceException("User account is already assigned to another employee");
            }
        }

        Employee employee = employeeMapper.toEntity(request);
        employee.setCompany(company);
        employee.setUser(user);
        if (request.active() != null) {
            employee.setActive(request.active());
        }

        Employee saved = employeeRepository.save(employee);

        if (user != null) {
            syncUserAssignments(user, region, store);
        }

        return mapToResponse(saved);
    }

    /**
     * Retrieves a single employee by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the EmployeeResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single employee by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the EmployeeResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return mapToResponse(employee);
    }

    /**
     * Returns a filtered paginated list of employees records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of employees records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<EmployeeResponse> searchEmployees(EmployeeSearchRequest searchRequest, Pageable pageable) {
        Specification<Employee> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.query() != null && !searchRequest.query().isBlank()) {
                String searchPattern = "%" + searchRequest.query().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("employeeCode")), searchPattern),
                        cb.like(cb.lower(root.get("firstName")), searchPattern),
                        cb.like(cb.lower(root.get("lastName")), searchPattern),
                        cb.like(cb.lower(root.get("email")), searchPattern)
                ));
            }

            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.companyId()));
            }

            if (searchRequest.regionId() != null) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<UserRegion> subRoot = subquery.from(UserRegion.class);
                subquery.select(subRoot.get("id").get("userId"));
                subquery.where(cb.equal(subRoot.get("region").get("id"), searchRequest.regionId()));
                predicates.add(root.get("user").get("id").in(subquery));
            }

            if (searchRequest.storeId() != null) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<UserStore> subRoot = subquery.from(UserStore.class);
                subquery.select(subRoot.get("id").get("userId"));
                subquery.where(cb.equal(subRoot.get("store").get("id"), searchRequest.storeId()));
                predicates.add(root.get("user").get("id").in(subquery));
            }

            Boolean activeFilter = searchRequest.active() != null ? searchRequest.active() : Boolean.TRUE;
            predicates.add(cb.equal(root.get("active"), activeFilter));

            if (searchRequest.designation() != null && !searchRequest.designation().isBlank()) {
                predicates.add(cb.equal(root.get("designation"), searchRequest.designation()));
            }

            if (searchRequest.employmentType() != null && !searchRequest.employmentType().isBlank()) {
                predicates.add(cb.equal(root.get("employmentType"), searchRequest.employmentType()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Employee> page = employeeRepository.findAll(spec, pageable);
        List<EmployeeResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    /**
     * Updates an existing employee record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the EmployeeResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        if (!employee.getActive()) {
            throw new BusinessException("Soft-deleted employee cannot be updated");
        }

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));

        if (!company.getActive()) {
            throw new BusinessException("Cannot assign employee: Company is inactive");
        }

        boolean codeChanged = !employee.getEmployeeCode().equals(request.employeeCode())
                || !employee.getCompany().getId().equals(request.companyId());
        if (codeChanged && employeeRepository.existsByCompanyIdAndEmployeeCode(request.companyId(), request.employeeCode())) {
            throw new DuplicateResourceException("Employee with code " + request.employeeCode() + " already exists in company " + company.getName());
        }

        boolean emailChanged = (request.email() != null && !request.email().isBlank() &&
                (!request.email().equals(employee.getEmail()) || !employee.getCompany().getId().equals(request.companyId())));
        if (emailChanged && employeeRepository.existsByCompanyIdAndEmail(request.companyId(), request.email())) {
            throw new DuplicateResourceException("Employee with email " + request.email() + " already exists in company " + company.getName());
        }

        User user = null;
        if (request.userId() != null) {
            user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User account not found with ID: " + request.userId()));
            if (!user.getActive()) {
                throw new BusinessException("Cannot assign employee: User account is inactive");
            }
            if (employeeRepository.existsByUserIdAndIdNot(request.userId(), id)) {
                throw new DuplicateResourceException("User account is already assigned to another employee");
            }
        }

        Region region = null;
        if (request.regionId() != null) {
            region = regionRepository.findById(request.regionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + request.regionId()));
            if (!region.getCompany().getId().equals(request.companyId())) {
                throw new BusinessException("Cannot assign employee: Region does not belong to the selected company");
            }
        }

        Store store = null;
        if (request.storeId() != null) {
            store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.storeId()));
            if (!store.getActive()) {
                throw new BusinessException("Cannot assign employee: Store is inactive");
            }
            if (request.regionId() == null || !store.getRegion().getId().equals(request.regionId())) {
                throw new BusinessException("Cannot assign employee: Store does not belong to the selected region");
            }
            if (!store.getRegion().getCompany().getId().equals(request.companyId())) {
                throw new BusinessException("Cannot assign employee: Store does not belong to the selected company");
            }
            boolean alreadyAssigned = false;
            User targetUser = (user != null) ? user : employee.getUser();
            final Long targetStoreId = store.getId();
            if (targetUser != null) {
                alreadyAssigned = userStoreRepository.findByIdUserId(targetUser.getId()).stream()
                        .anyMatch(us -> us.getId().getStoreId().equals(targetStoreId));
            }
        }

        // Clear assignments for previous user if mapping changed or removed
        if (employee.getUser() != null && (user == null || !employee.getUser().getId().equals(user.getId()))) {
            userRegionRepository.deleteByIdUserId(employee.getUser().getId());
            userStoreRepository.deleteByIdUserId(employee.getUser().getId());
        }

        employeeMapper.updateEntity(request, employee);
        employee.setCompany(company);
        employee.setUser(user);
        if (request.active() != null) {
            employee.setActive(request.active());
        }

        Employee saved = employeeRepository.save(employee);

        if (user != null) {
            syncUserAssignments(user, region, store);
        }

        return mapToResponse(saved);
    }

    /**
     * Permanently deletes the employee from the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     */
    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        employee.setActive(false);
        employee.setStatus("INACTIVE");

        if (employee.getUser() != null) {
            userRegionRepository.deleteByIdUserId(employee.getUser().getId());
            userStoreRepository.deleteByIdUserId(employee.getUser().getId());
        }

        employeeRepository.save(employee);
    }

    /**
     * Performs the activateEmployee operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the EmployeeResponse result
     */
    @Override
    @Transactional
    public EmployeeResponse activateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        employee.setActive(true);
        employee.setStatus("ACTIVE");

        Employee saved = employeeRepository.save(employee);
        return mapToResponse(saved);
    }

    /**
     * Performs the deactivateEmployee operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the EmployeeResponse result
     */
    @Override
    @Transactional
    public EmployeeResponse deactivateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        employee.setActive(false);
        employee.setStatus("INACTIVE");

        if (employee.getUser() != null) {
            userRegionRepository.deleteByIdUserId(employee.getUser().getId());
            userStoreRepository.deleteByIdUserId(employee.getUser().getId());
        }

        Employee saved = employeeRepository.save(employee);
        return mapToResponse(saved);
    }

    private void syncUserAssignments(User user, Region region, Store store) {
        userRegionRepository.deleteByIdUserId(user.getId());
        userStoreRepository.deleteByIdUserId(user.getId());

        if (region != null) {
            UserRegion ur = new UserRegion();
            ur.setId(new UserRegion.UserRegionId(user.getId(), region.getId()));
            ur.setUser(user);
            ur.setRegion(region);
            userRegionRepository.save(ur);
        }

        if (store != null) {
            UserStore us = new UserStore();
            us.setId(new UserStore.UserStoreId(user.getId(), store.getId()));
            us.setUser(user);
            us.setStore(store);
            userStoreRepository.save(us);
        }
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        EmployeeResponse response = employeeMapper.toResponse(employee);

        Long regionId = null;
        String regionName = null;
        String regionCode = null;
        Long storeId = null;
        String storeName = null;
        String storeCode = null;

        if (employee.getUser() != null) {
            List<UserRegion> regions = userRegionRepository.findByIdUserId(employee.getUser().getId());
            if (!regions.isEmpty()) {
                Region region = regions.get(0).getRegion();
                regionId = region.getId();
                regionName = region.getName();
                regionCode = region.getCode();
            }
            List<UserStore> stores = userStoreRepository.findByIdUserId(employee.getUser().getId());
            if (!stores.isEmpty()) {
                Store store = stores.get(0).getStore();
                storeId = store.getId();
                storeName = store.getName();
                storeCode = store.getCode();
            }
        }

        BigDecimal salaryVal = BigDecimal.valueOf(2400.00);
        try {
            Optional<EmployeeSalaryStructure> structOpt = employeeSalaryStructureRepository
                    .findByCompanyIdAndEmployeeIdAndStatus(employee.getCompany().getId(), employee.getId(), "ACTIVE");
            if (structOpt.isPresent()) {
                List<EmployeeSalaryStructureItem> items = employeeSalaryStructureItemRepository.findByStructureId(structOpt.get().getId());
                for (EmployeeSalaryStructureItem item : items) {
                    SalaryComponent comp = salaryComponentRepository.findById(item.getComponentId()).orElse(null);
                    if (comp != null && "BASIC".equalsIgnoreCase(comp.getCode())) {
                        salaryVal = item.getAmount();
                    }
                }
            }
        } catch (Exception e) {
            // fallback
        }

        String activeShiftVal = "No Assigned Shift";
        try {
            Optional<EmployeeShift> empShiftOpt = employeeShiftRepository.findActiveShiftForEmployeeOnDate(employee.getId(), LocalDate.now());
            if (empShiftOpt.isPresent()) {
                Shift s = empShiftOpt.get().getShift();
                if (s != null) {
                    String start = s.getStartTime() != null ? s.getStartTime().toString().substring(0, 5) : "00:00";
                    String end = s.getEndTime() != null ? s.getEndTime().toString().substring(0, 5) : "00:00";
                    activeShiftVal = s.getName() + " (" + start + " - " + end + ")";
                }
            }
        } catch (Exception e) {
            // fallback
        }

        String todayStatusVal = "ABSENT";
        try {
            Optional<Attendance> attendanceOpt = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), LocalDate.now());
            if (attendanceOpt.isPresent()) {
                todayStatusVal = attendanceOpt.get().getStatus();
            } else {
                List<EmployeeLeave> leaves = employeeLeaveRepository.findOverlapping(employee.getId(), LocalDate.now(), LocalDate.now());
                if (!leaves.isEmpty()) {
                    todayStatusVal = "ON_LEAVE";
                }
            }
        } catch (Exception e) {
            // fallback
        }

        String avatarUrlVal = employee.getUser() != null ? employee.getUser().getAvatarUrl() : null;
        if (avatarUrlVal == null || avatarUrlVal.isBlank()) {
            try {
                String code = employee.getEmployeeCode();
                if (code != null) {
                    String sanitizedCode = code.replaceAll("[^a-zA-Z0-9-]", "_");
                    String[] extensions = {"png", "jpg", "jpeg", "webp", "gif"};
                    for (String ext : extensions) {
                        java.io.File file = new java.io.File("frontend/user_uploads/avatars/" + sanitizedCode + "_profile_img." + ext);
                        if (file.exists()) {
                            avatarUrlVal = "user_uploads/avatars/" + sanitizedCode + "_profile_img." + ext;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // fallback
            }
        }

        return new EmployeeResponse(
                response.id(),
                response.employeeCode(),
                response.firstName(),
                response.lastName(),
                response.email(),
                response.phone(),
                response.companyId(),
                response.companyName(),
                response.companyCode(),
                regionId,
                regionName,
                regionCode,
                storeId,
                storeName,
                storeCode,
                response.userId(),
                response.userEmail(),
                response.designation(),
                response.department(),
                response.employmentType(),
                response.hireDate(),
                response.status(),
                response.active(),
                salaryVal,
                activeShiftVal,
                todayStatusVal,
                avatarUrlVal,
                response.createdAt(),
                response.updatedAt()
        );
    }
}