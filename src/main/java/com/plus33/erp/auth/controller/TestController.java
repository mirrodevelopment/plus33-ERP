package com.plus33.erp.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/v1/test")
    public String test() {
        return "Authenticated";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ULTIMATE_ADMIN')")
    public String admin() {
        return "Admin access granted";
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public String users() {
        return "User permission granted";
    }
}
