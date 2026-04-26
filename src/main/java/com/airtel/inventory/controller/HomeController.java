package com.airtel.inventory.controller;

import com.airtel.inventory.service.DeviceService;
import com.airtel.inventory.service.EmployeeService;
import com.airtel.inventory.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private IssueService issueService;

    // Landing page (accessible without login)
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("totalDevices", deviceService.getDeviceCount());
        model.addAttribute("totalEmployees", employeeService.getEmployeeCount());
        model.addAttribute("assignedDevices", deviceService.getAssignedDevicesCount());
        model.addAttribute("openIssues", issueService.getOpenIssuesCount());
        return "dashboard";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("activePage", "reports");
        return "dashboard";
    }
}