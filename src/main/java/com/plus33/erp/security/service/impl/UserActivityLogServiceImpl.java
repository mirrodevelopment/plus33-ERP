/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : UserActivityLogServiceImpl.java
 * Path              : src/main/java/com/plus33/erp/security/service/impl/UserActivityLogServiceImpl.java
 * Purpose           : Service implementation for user activity log management, user data pulling,
 *                     and role-based scoped activity log retrieval.
 * Version           : 1.0.0
 ******************************************************************************/
package com.plus33.erp.security.service.impl;

import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.entity.UserActivityLog;
import com.plus33.erp.security.entity.Role;
import com.plus33.erp.security.repository.UserActivityLogRepository;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.security.service.UserActivityLogService;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.entity.UserRegion;
import com.plus33.erp.workforce.entity.UserStore;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.repository.UserRegionRepository;
import com.plus33.erp.workforce.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserActivityLogServiceImpl implements UserActivityLogService {

    private final UserRepository userRepository;
    private final UserActivityLogRepository userActivityLogRepository;
    private final UserStoreRepository userStoreRepository;
    private final UserRegionRepository userRegionRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserActivityLog> getMyActivityLogs(String email, String startDate, String endDate) {
        LocalDateTime start = parseStart(startDate);
        LocalDateTime end = parseEnd(endDate);

        if (start != null && end != null) {
            return userActivityLogRepository.findByUsernameAndLoginTimeBetweenOrderByLoginTimeDesc(email, start, end);
        }
        return userActivityLogRepository.findByUsernameOrderByLoginTimeDesc(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, String>> getTargetUsersForAdmin(String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail).orElse(null);
        if (admin == null || admin.getRoles() == null || admin.getRoles().isEmpty()) {
            return Collections.emptyList();
        }

        boolean isUltimate = false;
        boolean isNational = false;
        boolean isRegional = false;

        for (Role r : admin.getRoles()) {
            if (r.getCode() != null) {
                String c = r.getCode().toUpperCase();
                if (c.contains("ULTIMATE") || c.equals("ADMIN") || c.equals("ROLE_ADMIN") || c.equals("ROLE_ULTIMATE_ADMIN")) {
                    isUltimate = true;
                    break;
                }
                if (c.contains("NATIONAL")) isNational = true;
                if (c.contains("REGIONAL")) isRegional = true;
            }
        }

        List<User> allUsers = userRepository.findAll();
        List<User> scopedUsers = new ArrayList<>();

        List<UserStore> adminStores = userStoreRepository.findByIdUserId(admin.getId());
        List<UserRegion> adminRegions = userRegionRepository.findByIdUserId(admin.getId());

        if (isUltimate || (!isNational && !isRegional && (adminStores == null || adminStores.isEmpty()))) {
            scopedUsers = allUsers;
        } else if (isNational) {
            String adminCountry = getUserCountry(admin.getId());
            for (User u : allUsers) {
                if (adminCountry.equalsIgnoreCase(getUserCountry(u.getId()))) {
                    scopedUsers.add(u);
                }
            }
        } else if (isRegional && adminRegions != null && !adminRegions.isEmpty()) {
            for (User u : allUsers) {
                if (shareRegion(admin.getId(), u.getId())) {
                    scopedUsers.add(u);
                }
            }
        } else if (adminStores != null && !adminStores.isEmpty()) {
            for (User u : allUsers) {
                if (shareStore(admin.getId(), u.getId())) {
                    scopedUsers.add(u);
                }
            }
        } else {
            scopedUsers = allUsers;
        }

        if (scopedUsers.isEmpty()) {
            scopedUsers = allUsers;
        }

        List<Map<String, String>> result = new ArrayList<>();
        for (User u : scopedUsers) {
            Map<String, String> m = new HashMap<>();
            m.put("email", u.getEmail());
            String fullName = ((u.getFirstName() != null ? u.getFirstName() : "") + " " + (u.getLastName() != null ? u.getLastName() : "")).trim();
            m.put("name", fullName.isEmpty() ? u.getEmail() : fullName);

            Optional<Employee> empOpt = employeeRepository.findByUserId(u.getId());
            if (empOpt.isPresent()) {
                m.put("employeeCode", empOpt.get().getEmployeeCode() != null ? empOpt.get().getEmployeeCode() : "EMP-" + empOpt.get().getId());
                m.put("designation", empOpt.get().getDesignation() != null ? empOpt.get().getDesignation() : "Employee");
            } else {
                m.put("employeeCode", "ADMIN");
                m.put("designation", "Administrator");
            }
            result.add(m);
        }

        result.sort(Comparator.comparing(a -> a.get("name")));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserActivityLog> searchActivityLogs(String adminEmail, String targetEmail, String startDate, String endDate) {
        LocalDateTime start = parseStart(startDate);
        LocalDateTime end = parseEnd(endDate);

        String activeTarget = (targetEmail != null && !targetEmail.trim().isEmpty()) ? targetEmail.trim() : adminEmail;

        if ("ALL_UNDER_ME".equalsIgnoreCase(activeTarget) || "ALL".equalsIgnoreCase(activeTarget)) {
            List<Map<String, String>> scopedUsers = getTargetUsersForAdmin(adminEmail);
            List<String> targetUsernames = new ArrayList<>();
            for (Map<String, String> u : scopedUsers) {
                if (u.containsKey("email")) {
                    targetUsernames.add(u.get("email"));
                }
            }
            if (targetUsernames.isEmpty()) {
                targetUsernames.add(adminEmail);
            }
            if (start != null && end != null) {
                return userActivityLogRepository.findByUsernameInAndLoginTimeBetweenOrderByLoginTimeDesc(targetUsernames, start, end);
            }
            return userActivityLogRepository.findByUsernameInOrderByLoginTimeDesc(targetUsernames);
        }

        if (start != null && end != null) {
            return userActivityLogRepository.findByUsernameAndLoginTimeBetweenOrderByLoginTimeDesc(activeTarget, start, end);
        }
        return userActivityLogRepository.findByUsernameOrderByLoginTimeDesc(activeTarget);
    }

    private LocalDateTime parseStart(String startDate) {
        if (startDate != null && !startDate.trim().isEmpty()) {
            try {
                return LocalDate.parse(startDate).atStartOfDay();
            } catch (Exception ignored) {}
        }
        return null;
    }

    private LocalDateTime parseEnd(String endDate) {
        if (endDate != null && !endDate.trim().isEmpty()) {
            try {
                return LocalDate.parse(endDate).atTime(23, 59, 59);
            } catch (Exception ignored) {}
        }
        return null;
    }

    private String getUserCountry(Long userId) {
        List<UserStore> userStores = userStoreRepository.findByIdUserId(userId);
        if (userStores != null && !userStores.isEmpty() && userStores.get(0).getStore() != null) {
            com.plus33.erp.organization.entity.Store store = userStores.get(0).getStore();
            if (store.getRegion() != null && store.getRegion().getCode() != null) {
                String code = store.getRegion().getCode().toUpperCase();
                if (code.startsWith("FR")) return "France";
                if (code.startsWith("AE") || code.startsWith("UAE")) return "UAE";
                if (code.startsWith("IN")) return "India";
            }
        }
        List<UserRegion> userRegions = userRegionRepository.findByIdUserId(userId);
        if (userRegions != null && !userRegions.isEmpty() && userRegions.get(0).getRegion() != null) {
            com.plus33.erp.organization.entity.Region region = userRegions.get(0).getRegion();
            if (region.getCode() != null) {
                String code = region.getCode().toUpperCase();
                if (code.startsWith("FR")) return "France";
                if (code.startsWith("AE") || code.startsWith("UAE")) return "UAE";
                if (code.startsWith("IN")) return "India";
            }
        }
        return "India";
    }

    private boolean shareRegion(Long userId1, Long userId2) {
        List<UserRegion> r1 = userRegionRepository.findByIdUserId(userId1);
        List<UserRegion> r2 = userRegionRepository.findByIdUserId(userId2);
        if (r1 == null || r2 == null || r1.isEmpty() || r2.isEmpty()) return false;
        return r1.get(0).getRegion().getId().equals(r2.get(0).getRegion().getId());
    }

    private boolean shareStore(Long userId1, Long userId2) {
        List<UserStore> s1 = userStoreRepository.findByIdUserId(userId1);
        List<UserStore> s2 = userStoreRepository.findByIdUserId(userId2);
        if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) return false;
        return s1.get(0).getStore().getId().equals(s2.get(0).getStore().getId());
    }
}
