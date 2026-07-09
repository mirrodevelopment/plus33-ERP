/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Auth Module
 * Package           : com.plus33.erp.auth.controller
 * File              : TestController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Auth Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TestController
 * Related Service   : TestControllerService, TestControllerServiceImpl
 * Related Repository: TestControllerRepository
 * Related Entity    : TestController
 * Related DTO       : N/A
 * Related Mapper    : TestControllerMapper
 * Related DB Table  : test_controllers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Auth Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Auth Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: N/A
 ******************************************************************************/
package com.plus33.erp.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>PLUS33 Coffee ERP -- Auth Module</b>
 *
 * <p><b>Class  :</b> {@code TestController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.auth.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to TestService.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
public class TestController {

    /**
     * Performs the test operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return the result string value
     */
    @GetMapping("/api/v1/test")
    public String test() {
        return "Authenticated";
    }

    /**
     * Performs the admin operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return the result string value
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ULTIMATE_ADMIN')")
    public String admin() {
        return "Admin access granted";
    }

    /**
     * Performs the users operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return the result string value
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public String users() {
        return "User permission granted";
    }
}