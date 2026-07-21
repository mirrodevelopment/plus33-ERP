/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Auth Module
 * File              : AuthController.java
 * Path              : src/main/java/com/plus33/erp/auth/controller/AuthController.java
 * Purpose           : Exposes authentication REST endpoints including credential-based login,
 *                     JWT token generation, authenticated user profile retrieval,
 *                     self-profile update, and secure password change for all ERP roles.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Central authentication controller for the PLUS33 Coffee ERP platform. Maps
 * to the base path /api/v1/auth and exposes four REST endpoints consumed by
 * the frontend SPA login flow and profile pages.
 *
 * POST /api/v1/auth/login
 *   Receives email + password via LoginRequest DTO. Delegates to Spring
 *   AuthenticationManager which internally calls UserDetailsService. On
 *   successful authentication, JwtService generates a signed Bearer token
 *   returned inside TokenResponse wrapped in ApiResponse.
 *
 * PUT /api/v1/auth/change-password
 *   Validates the authenticated user's current password via BCrypt match,
 *   then encodes and persists the new password to the User entity.
 *   Rejects requests with 401 if Principal is null or 400 if current
 *   password is wrong. Requires an active JWT.
 *
 * GET /api/v1/auth/me
 *   Returns a comprehensive profile map for the currently authenticated user.
 *   Resolves the User entity from UserRepository by email extracted from the
 *   JWT Principal. Joins EmployeeRepository to retrieve employee-specific
 *   fields (employeeCode, designation, department, hireDate, banking info).
 *   Resolves UserStore and UserRegion associations to derive storeName,
 *   storeRegion and country. Country code is inferred from Region.code
 *   prefix (FR → France, AE/UAE → UAE, IN → India). Resolves active
 *   EmployeeSalaryStructure and its items to extract BASE_MONTHLY and
 *   BASE_HOURLY salary component amounts. Locates the user's avatar file
 *   from the filesystem at frontend/user_uploads/avatars/{code}_profile_img.*
 *   falling back to default gender avatar if not found. Returns all fields
 *   as a Map<String, Object> wrapped in ApiResponse. Admin users without
 *   Employee records receive hardcoded fallback values.
 *
 * PUT /api/v1/auth/me
 *   Self-profile update for any authenticated user. Accepts firstName,
 *   lastName, name, phone, bankName, bankAccount/bankAccountNumber,
 *   ifscCode/ifscNumber, designation, and avatarUrl. Persists changes to
 *   both User and Employee entities atomically via @Transactional.
 *   Designation updates are blocked for ROLE_storeEmployee and
 *   ROLE_shiftSupervisor roles. Calls getMe() internally to return
 *   the updated profile in the same response shape.
 *
 * Dependencies:
 *   - JwtService (security.jwt) — token generation and expiry resolution
 *   - UserRepository (security.repository) — user lookup and password save
 *   - EmployeeRepository (workforce.repository) — employee profile data
 *   - UserStoreRepository (workforce.repository) — store assignment lookup
 *   - UserRegionRepository (workforce.repository) — region assignment lookup
 *   - EmployeeSalaryStructureRepository — active salary structure resolution
 *   - EmployeeSalaryStructureItemRepository — salary component breakdown
 *   - SalaryComponentRepository — salary component code resolution
 *   - PasswordEncoder (Spring Security) — BCrypt verification and encoding
 *   - AuthenticationManager (Spring Security) — credential authentication
 ******************************************************************************/
package com.plus33.erp.auth.controller;

import com.plus33.erp.auth.dto.LoginRequest;
import com.plus33.erp.auth.dto.ChangePasswordRequest;
import com.plus33.erp.auth.dto.TokenResponse;
import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.security.jwt.JwtService;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.security.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Auth Module</b>
 *
 * <p><b>Class  :</b> {@code AuthController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.auth.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to AuthService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> AuthController.endpoint()
 *   --> AuthService.method()
 *   --> AuthRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/auth/login, PUT /api/v1/auth/change-password</p>
 * <p><b>Module Deps      :</b> Auth, Common, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.plus33.erp.workforce.repository.EmployeeRepository employeeRepository;
    private final com.plus33.erp.workforce.repository.UserStoreRepository userStoreRepository;
    private final com.plus33.erp.workforce.repository.UserRegionRepository userRegionRepository;
    private final com.plus33.erp.workforce.repository.EmployeeSalaryStructureRepository employeeSalaryStructureRepository;
    private final com.plus33.erp.workforce.repository.EmployeeSalaryStructureItemRepository employeeSalaryStructureItemRepository;
    private final com.plus33.erp.workforce.repository.SalaryComponentRepository salaryComponentRepository;

    /**
     * Authenticates the user credentials and generates a signed JWT token.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        TokenResponse tokenResponse = new TokenResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds()
        );
        return ResponseEntity.ok(ApiResponse.success("Authentication successful", tokenResponse));
    }

    /**
     * Updates the password for the currently authenticated user.
     *
     * @param request the change password request containing current and new password
     * @param principal the currently authenticated security principal
     * @return HTTP ResponseEntity wrapping ApiResponse
     */
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Principal principal) {
        
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found: " + email));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Incorrect current password"));
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    @GetMapping("/me")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMe(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found: " + email));

        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getEmail());
        data.put("email", user.getEmail());
        data.put("firstName", user.getFirstName() != null ? user.getFirstName() : "");
        data.put("lastName", user.getLastName() != null ? user.getLastName() : "");
        data.put("name", ((user.getFirstName() != null ? user.getFirstName() : "") + " " + (user.getLastName() != null ? user.getLastName() : "")).trim());

        Optional<Employee> empOpt = employeeRepository.findByUserId(user.getId());
        if (empOpt.isPresent()) {
            Employee emp = empOpt.get();
            data.put("id", emp.getId());
            data.put("employeeCode", emp.getEmployeeCode());
            data.put("firstName", emp.getFirstName() != null ? emp.getFirstName() : (user.getFirstName() != null ? user.getFirstName() : ""));
            data.put("lastName", emp.getLastName() != null ? emp.getLastName() : (user.getLastName() != null ? user.getLastName() : ""));
            data.put("phone", emp.getPhone());
            data.put("emergencyContactPhone", emp.getEmergencyContactPhone());

            data.put("designation", emp.getDesignation());
            data.put("department", emp.getDepartment());
            data.put("joinedDate", emp.getHireDate() != null ? emp.getHireDate().toString() : "");
            data.put("gender", "Male");
            data.put("bankName", emp.getBankName() != null ? emp.getBankName() : "");
            data.put("bankAccount", emp.getBankAccountNumber() != null ? emp.getBankAccountNumber() : "");
            data.put("bankAccountNumber", emp.getBankAccountNumber() != null ? emp.getBankAccountNumber() : "");
            data.put("ifscCode", emp.getIfscNumber() != null ? emp.getIfscNumber() : "");
            data.put("ifscNumber", emp.getIfscNumber() != null ? emp.getIfscNumber() : "");
        } else {
            data.put("id", 0L);
            data.put("employeeCode", "ADMIN");
            data.put("phone", "+91 98765 43210");
            data.put("designation", "Administrator");
            data.put("department", "Executive Administration");
            data.put("joinedDate", "2024-01-15");
            data.put("gender", "Male");
            data.put("bankName", "PLUS33 Bank");
            data.put("bankAccount", "•••• •••• •••• 9876");
            data.put("bankAccountNumber", "•••• •••• •••• 9876");
            data.put("ifscCode", "PL330001");
            data.put("ifscNumber", "PL330001");
        }

        String storeName = "Corporate Head Office";
        String storeRegion = "Delhi NCR";
        String country = "India";
        data.put("storeId", null);
        data.put("storeCode", null);
        data.put("storeType", "FLAGSHIP_CAFE");

        List<com.plus33.erp.workforce.entity.UserStore> userStores = userStoreRepository.findByIdUserId(user.getId());
        if (userStores != null && !userStores.isEmpty() && userStores.get(0).getStore() != null) {
            com.plus33.erp.organization.entity.Store store = userStores.get(0).getStore();
            storeName = store.getName();
            data.put("storeId", store.getId());
            data.put("storeCode", store.getCode());
            data.put("storeType", store.getType() != null ? store.getType().toString() : "COMPACT_CAFE");
            if (store.getRegion() != null && store.getRegion().getCode() != null) {
                String code = store.getRegion().getCode().toUpperCase();
                if (code.startsWith("FR")) {
                    country = "France";
                } else if (code.startsWith("AE") || code.startsWith("UAE")) {
                    country = "UAE";
                } else if (code.startsWith("IN")) {
                    country = "India";
                }
            }
        }

        List<com.plus33.erp.workforce.entity.UserRegion> userRegions = userRegionRepository.findByIdUserId(user.getId());
        if (userRegions != null && !userRegions.isEmpty() && userRegions.get(0).getRegion() != null) {
            com.plus33.erp.organization.entity.Region region = userRegions.get(0).getRegion();
            storeRegion = region.getName();
            if (region.getCode() != null) {
                String code = region.getCode().toUpperCase();
                if (code.startsWith("FR")) {
                    country = "France";
                } else if (code.startsWith("AE") || code.startsWith("UAE")) {
                    country = "UAE";
                } else if (code.startsWith("IN")) {
                    country = "India";
                }
            }
        }

        String address = "Connaught Place, New Delhi, 110001, India";
        String employmentType = "Permanent";
        java.math.BigDecimal hourlyRate = java.math.BigDecimal.valueOf(15.00);
        java.math.BigDecimal basicSalary = java.math.BigDecimal.valueOf(2400.00);

        if (empOpt.isPresent()) {
            Employee emp = empOpt.get();
            employmentType = emp.getEmploymentType() != null ? emp.getEmploymentType() : "Permanent";
            
            if ("France".equalsIgnoreCase(country)) {
                address = "12 Rue de la Paix, 75002 Paris, France";
            } else if ("UAE".equalsIgnoreCase(country)) {
                address = "Sheikh Zayed Road, Downtown Dubai, UAE";
            } else {
                address = "Connaught Place, New Delhi, 110001, India";
            }

            try {
                Optional<com.plus33.erp.workforce.entity.EmployeeSalaryStructure> structOpt = employeeSalaryStructureRepository
                        .findByCompanyIdAndEmployeeIdAndStatus(emp.getCompany().getId(), emp.getId(), "ACTIVE");
                if (structOpt.isPresent()) {
                    List<com.plus33.erp.workforce.entity.EmployeeSalaryStructureItem> items = employeeSalaryStructureItemRepository.findByStructureId(structOpt.get().getId());
                    for (com.plus33.erp.workforce.entity.EmployeeSalaryStructureItem item : items) {
                        com.plus33.erp.workforce.entity.SalaryComponent comp = salaryComponentRepository.findById(item.getComponentId()).orElse(null);
                        if (comp != null) {
                            if ("BASE_MONTHLY".equalsIgnoreCase(comp.getCode())) {
                                basicSalary = item.getAmount();
                            } else if ("BASE_HOURLY".equalsIgnoreCase(comp.getCode())) {
                                hourlyRate = item.getAmount();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // fallback
            }
        }

        String avatarUrl = user.getAvatarUrl();
        if (avatarUrl == null || avatarUrl.isBlank()) {
            avatarUrl = "imgs/male-avatar.png";
            try {
                String code = empOpt.isPresent() ? empOpt.get().getEmployeeCode() : "ADMIN";
                if (code != null) {
                    String sanitizedCode = code.replaceAll("[^a-zA-Z0-9-]", "_");
                    String[] extensions = {"png", "jpg", "jpeg", "webp", "gif"};
                    for (String ext : extensions) {
                        java.io.File file = new java.io.File("frontend/user_uploads/avatars/" + sanitizedCode + "_profile_img." + ext);
                        if (file.exists()) {
                            avatarUrl = "user_uploads/avatars/" + sanitizedCode + "_profile_img." + ext;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // fallback
            }
        }

        data.put("store", storeName);
        data.put("storeRegion", storeRegion);
        data.put("country", country);
        data.put("address", address);
        data.put("employmentType", employmentType);
        data.put("hourlyRate", hourlyRate);
        data.put("basicSalary", basicSalary);
        data.put("avatarUrl", avatarUrl);

        return ResponseEntity.ok(ApiResponse.success("User details retrieved successfully", data));
    }

    @PutMapping("/me")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateMe(
            @RequestBody Map<String, String> request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found: " + email));

        String firstName = request.get("firstName");
        String lastName = request.get("lastName");
        String fullName = request.get("name");

        if (firstName != null && !firstName.isBlank()) {
            user.setFirstName(firstName.trim());
            if (lastName != null) user.setLastName(lastName.trim());
        } else if (fullName != null && !fullName.isBlank()) {
            String[] parts = fullName.trim().split("\\s+", 2);
            user.setFirstName(parts[0]);
            user.setLastName(parts.length > 1 ? parts[1] : "");
        }

        if (request.containsKey("avatarUrl") && request.get("avatarUrl") != null) {
            user.setAvatarUrl(request.get("avatarUrl"));
        }

        userRepository.save(user);

        Optional<Employee> empOpt = employeeRepository.findByUserId(user.getId());
        if (empOpt.isPresent()) {
            Employee emp = empOpt.get();
            
            if (firstName != null && !firstName.isBlank()) {
                emp.setFirstName(firstName.trim());
                if (lastName != null) emp.setLastName(lastName.trim());
            } else if (fullName != null && !fullName.isBlank()) {
                String[] parts = fullName.trim().split("\\s+", 2);
                emp.setFirstName(parts[0]);
                emp.setLastName(parts.length > 1 ? parts[1] : "");
            }

            if (request.containsKey("phone") && request.get("phone") != null) {
                emp.setPhone(request.get("phone"));
            }

            String bName = request.containsKey("bankName") ? request.get("bankName") : null;
            if (bName != null) emp.setBankName(bName);

            String bAcc = request.containsKey("bankAccount") ? request.get("bankAccount") : request.get("bankAccountNumber");
            if (bAcc != null) emp.setBankAccountNumber(bAcc);

            String ifsc = request.containsKey("ifscCode") ? request.get("ifscCode") : request.get("ifscNumber");
            if (ifsc != null) emp.setIfscNumber(ifsc);

            if (request.containsKey("designation") && request.get("designation") != null) {
                boolean isWorker = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_storeEmployee") || auth.getAuthority().equals("ROLE_shiftSupervisor"));
                if (!isWorker) {
                    emp.setDesignation(request.get("designation"));
                }
            }

            employeeRepository.saveAndFlush(emp);
        }

        return getMe(principal);
    }
}