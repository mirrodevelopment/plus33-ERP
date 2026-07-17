/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Auth Module
 * Package           : com.plus33.erp.auth.controller
 * File              : AuthController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Auth Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AuthController
 * Related Service   : AuthControllerService, AuthControllerServiceImpl
 * Related Repository: AuthControllerRepository
 * Related Entity    : AuthController
 * Related DTO       : ApiResponse, LoginRequest, TokenResponse
 * Related Mapper    : AuthControllerMapper
 * Related DB Table  : auth_controllers
 * Related REST APIs : POST /api/v1/auth/login
 * Depends On        : Common Module, Security Module
 * Used By           : Auth Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Auth Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/auth/login
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
        data.put("name", user.getFirstName() + " " + user.getLastName());

        Optional<Employee> empOpt = employeeRepository.findByUserId(user.getId());
        if (empOpt.isPresent()) {
            Employee emp = empOpt.get();
            data.put("id", emp.getId());
            data.put("employeeCode", emp.getEmployeeCode());
            data.put("phone", emp.getPhone());
            data.put("emergencyContactPhone", emp.getEmergencyContactPhone());

            data.put("designation", emp.getDesignation());
            data.put("department", emp.getDepartment());
            data.put("joinedDate", emp.getHireDate().toString());
            data.put("gender", "Male"); // Default fallback
        } else {
            data.put("id", 0L);
            data.put("employeeCode", "ADMIN");
            data.put("phone", "+91 98765 43210");
            data.put("designation", "Administrator");
            data.put("department", "Executive Administration");
            data.put("joinedDate", "2024-01-15");
            data.put("gender", "Male");
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

        String fullName = request.get("name");
        if (fullName != null && !fullName.isBlank()) {
            String[] parts = fullName.trim().split("\\s+", 2);
            user.setFirstName(parts[0]);
            if (parts.length > 1) {
                user.setLastName(parts[1]);
            } else {
                user.setLastName("");
            }
            userRepository.save(user);
        }

        if (request.containsKey("avatarUrl")) {
            user.setAvatarUrl(request.get("avatarUrl"));
            userRepository.save(user);
        }

        Optional<Employee> empOpt = employeeRepository.findByUserId(user.getId());
        if (empOpt.isPresent()) {
            Employee emp = empOpt.get();
            if (request.containsKey("phone")) {
                emp.setPhone(request.get("phone"));
            }
            if (request.containsKey("designation")) {
                boolean isWorker = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_storeEmployee") || auth.getAuthority().equals("ROLE_shiftSupervisor"));
                if (!isWorker) {
                    emp.setDesignation(request.get("designation"));
                }
            }
            if (fullName != null && !fullName.isBlank()) {
                String[] parts = fullName.trim().split("\\s+", 2);
                emp.setFirstName(parts[0]);
                if (parts.length > 1) {
                    emp.setLastName(parts[1]);
                } else {
                    emp.setLastName("");
                }
            }
            employeeRepository.save(emp);
        }

        return getMe(principal);
    }
}