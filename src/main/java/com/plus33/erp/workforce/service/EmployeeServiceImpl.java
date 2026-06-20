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
import com.plus33.erp.workforce.mapper.EmployeeMapper;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.repository.UserRegionRepository;
import com.plus33.erp.workforce.repository.UserStoreRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               CompanyRepository companyRepository,
                               RegionRepository regionRepository,
                               StoreRepository storeRepository,
                               UserRepository userRepository,
                               UserRegionRepository userRegionRepository,
                               UserStoreRepository userStoreRepository,
                               EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.regionRepository = regionRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.userRegionRepository = userRegionRepository;
        this.userStoreRepository = userStoreRepository;
        this.employeeMapper = employeeMapper;
    }

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

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return mapToResponse(employee);
    }

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
            if (employeeRepository.existsByUserIdAndIdNot(request.userId(), id)) {
                throw new DuplicateResourceException("User account is already assigned to another employee");
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
                response.createdAt(),
                response.updatedAt()
        );
    }
}
