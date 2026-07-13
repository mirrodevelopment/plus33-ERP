package com.plus33.erp.workforce.mapper;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.workforce.dto.EmployeeRequest;
import com.plus33.erp.workforce.dto.EmployeeResponse;
import com.plus33.erp.workforce.entity.Employee;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-13T18:32:48+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public Employee toEntity(EmployeeRequest request) {
        if ( request == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setEmployeeCode( request.employeeCode() );
        employee.setFirstName( request.firstName() );
        employee.setLastName( request.lastName() );
        employee.setEmail( request.email() );
        employee.setPhone( request.phone() );
        employee.setDesignation( request.designation() );
        employee.setDepartment( request.department() );
        employee.setEmploymentType( request.employmentType() );
        employee.setHireDate( request.hireDate() );
        employee.setActive( request.active() );

        return employee;
    }

    @Override
    public EmployeeResponse toResponse(Employee entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        String companyName = null;
        String companyCode = null;
        Long userId = null;
        String userEmail = null;
        Long id = null;
        String employeeCode = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        String phone = null;
        String designation = null;
        String department = null;
        String employmentType = null;
        LocalDate hireDate = null;
        String status = null;
        Boolean active = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        companyId = entityCompanyId( entity );
        companyName = entityCompanyName( entity );
        companyCode = entityCompanyCode( entity );
        userId = entityUserId( entity );
        userEmail = entityUserEmail( entity );
        id = entity.getId();
        employeeCode = entity.getEmployeeCode();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        phone = entity.getPhone();
        designation = entity.getDesignation();
        department = entity.getDepartment();
        employmentType = entity.getEmploymentType();
        hireDate = entity.getHireDate();
        status = entity.getStatus();
        active = entity.getActive();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        Long regionId = null;
        String regionName = null;
        String regionCode = null;
        Long storeId = null;
        String storeName = null;
        String storeCode = null;

        EmployeeResponse employeeResponse = new EmployeeResponse( id, employeeCode, firstName, lastName, email, phone, companyId, companyName, companyCode, regionId, regionName, regionCode, storeId, storeName, storeCode, userId, userEmail, designation, department, employmentType, hireDate, status, active, createdAt, updatedAt );

        return employeeResponse;
    }

    @Override
    public void updateEntity(EmployeeRequest request, Employee entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmployeeCode( request.employeeCode() );
        entity.setFirstName( request.firstName() );
        entity.setLastName( request.lastName() );
        entity.setEmail( request.email() );
        entity.setPhone( request.phone() );
        entity.setDesignation( request.designation() );
        entity.setDepartment( request.department() );
        entity.setEmploymentType( request.employmentType() );
        entity.setHireDate( request.hireDate() );
        entity.setActive( request.active() );
    }

    private Long entityCompanyId(Employee employee) {
        Company company = employee.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private String entityCompanyName(Employee employee) {
        Company company = employee.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getName();
    }

    private String entityCompanyCode(Employee employee) {
        Company company = employee.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getCode();
    }

    private Long entityUserId(Employee employee) {
        User user = employee.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private String entityUserEmail(Employee employee) {
        User user = employee.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getEmail();
    }
}
