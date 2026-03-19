package com.eimc.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *      LoginController handles web requests
 *      and returns a View (an HTML page).
 * */

@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping("login")
    public String getLoginView() {
        return "login/login";
    }

    @GetMapping("logout")
    public String getLogoutView() {
        return "logout/logout";
    }

    @GetMapping("dashboard")
    public String getDashboardView(HttpServletRequest request) {

        if (request.isUserInRole("ADMIN")) {
            return "dashboard/admin";
        }

        if (request.isUserInRole("ADMIN_TRAINEE")) {
            return "dashboard/admin-trainee";
        }

        return "dashboard/employee";
    }

}
