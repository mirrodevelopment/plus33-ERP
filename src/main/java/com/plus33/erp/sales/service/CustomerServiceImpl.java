/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : CustomerServiceImpl.java
 * Purpose           : Business logic service layer for Sales Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerController
 * Related Service   : CustomerServiceImpl
 * Related Repository: CustomerRepository, CompanyRepository
 * Related Entity    : Customer
 * Related DTO       : CustomerRequest, CustomerResponse, CustomerSearchRequest, PageResponse, searchRequest
 * Related Mapper    : CustomerMapper
 * Related DB Table  : customers
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : CustomerController, CustomerServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Sales Module. Implements CustomerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.sales.dto.CustomerRequest;
import com.plus33.erp.sales.dto.CustomerResponse;
import com.plus33.erp.sales.dto.CustomerSearchRequest;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerStatus;
import com.plus33.erp.sales.mapper.CustomerMapper;
import com.plus33.erp.sales.repository.CustomerRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Sales Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CustomerController
 *   --> CustomerServiceImpl (this)
 *   --> Validate business rules
 *   --> CustomerRepository (read/write 'customers')
 *   --> CustomerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code customers}</p>
 * <p><b>Module Deps      :</b> Common, Organization, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            CompanyRepository companyRepository,
            CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.customerMapper = customerMapper;
    }

    /**
     * Creates a new customer and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the CustomerResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));

        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        // Generate or validate customer code
        String code = request.code();
        if (code == null || code.isBlank()) {
            Long nextSeq = customerRepository.getNextSequenceValue();
            code = String.format("CUST-%d-%06d", LocalDate.now().getYear(), nextSeq);
        } else {
            if (customerRepository.existsByCompanyIdAndCode(request.companyId(), code)) {
                throw new DuplicateResourceException("Customer code " + code + " already exists in this company");
            }
        }

        // Validate email uniqueness
        if (request.email() != null && !request.email().isBlank() &&
                customerRepository.existsByCompanyIdAndEmail(request.companyId(), request.email())) {
            throw new DuplicateResourceException("Customer email " + request.email() + " already exists in this company");
        }

        Customer customer = customerMapper.toEntity(request);
        customer.setCompany(company);
        customer.setCode(code);

        if (request.status() != null) {
            customer.setStatus(request.status());
        } else {
            customer.setStatus(CustomerStatus.ACTIVE);
        }

        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }

    /**
     * Updates an existing customer record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the CustomerResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        if (customer.getStatus() == CustomerStatus.INACTIVE) {
            throw new BusinessException("Soft-deleted customers cannot be updated");
        }

        if (!customer.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Cannot transfer customer to a different company");
        }

        String newCode = request.code();
        if (newCode != null && !newCode.isBlank() && !newCode.equalsIgnoreCase(customer.getCode())) {
            if (customerRepository.existsByCompanyIdAndCode(request.companyId(), newCode)) {
                throw new DuplicateResourceException("Customer code " + newCode + " already exists in this company");
            }
            customer.setCode(newCode);
        }

        String newEmail = request.email();
        if (newEmail != null && !newEmail.isBlank() && !newEmail.equalsIgnoreCase(customer.getEmail())) {
            if (customerRepository.existsByCompanyIdAndEmail(request.companyId(), newEmail)) {
                throw new DuplicateResourceException("Customer email " + newEmail + " already exists in this company");
            }
        }

        customerMapper.updateEntity(request, customer);

        if (request.status() != null) {
            customer.setStatus(request.status());
        }

        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }

    /**
     * Retrieves a single customer by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the CustomerResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single customer by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the CustomerResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        return customerMapper.toResponse(customer);
    }

    /**
     * Returns a filtered paginated list of customers records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of customers records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<CustomerResponse> searchCustomers(CustomerSearchRequest searchRequest, Pageable pageable) {
        Specification<Customer> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.companyId()));
            }

            if (searchRequest.customerType() != null) {
                predicates.add(cb.equal(root.get("customerType"), searchRequest.customerType()));
            }

            if (searchRequest.pricingTier() != null && !searchRequest.pricingTier().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("pricingTier")), searchRequest.pricingTier().toLowerCase()));
            }

            if (searchRequest.paymentTermsDays() != null) {
                predicates.add(cb.equal(root.get("paymentTermsDays"), searchRequest.paymentTermsDays()));
            }

            if (searchRequest.status() != null) {
                predicates.add(cb.equal(root.get("status"), searchRequest.status()));
            } else {
                Boolean activeOnly = searchRequest.activeOnly();
                if (activeOnly == null || activeOnly) {
                    predicates.add(cb.equal(root.get("status"), CustomerStatus.ACTIVE));
                }
            }

            if (searchRequest.query() != null && !searchRequest.query().isBlank()) {
                String searchPattern = "%" + searchRequest.query().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("code")), searchPattern),
                        cb.like(cb.lower(root.get("name")), searchPattern),
                        cb.like(cb.lower(root.get("contactPerson")), searchPattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Customer> page = customerRepository.findAll(spec, pageable);
        List<CustomerResponse> content = page.getContent().stream()
                .map(customerMapper::toResponse)
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
     * Performs the activateCustomer operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the CustomerResponse result
     */
    @Override
    @Transactional
    public CustomerResponse activateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        customer.setStatus(CustomerStatus.ACTIVE);
        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }

    /**
     * Performs the deactivateCustomer operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the CustomerResponse result
     */
    @Override
    @Transactional
    public CustomerResponse deactivateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        customer.setStatus(CustomerStatus.INACTIVE);
        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }

    /**
     * Permanently deletes the customer from the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     */
    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        customer.setStatus(CustomerStatus.INACTIVE);
        customerRepository.save(customer);
    }
}