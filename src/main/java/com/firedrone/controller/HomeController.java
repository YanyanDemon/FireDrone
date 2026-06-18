package com.firedrone.controller;

import com.firedrone.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DashboardService dashboardService;

    public HomeController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping({"/home", "/dashboard"})
    public String dashboard(Model model) {
        model.addAllAttributes(dashboardService.getDashboardData());
        return "dashboard";
    }
}
