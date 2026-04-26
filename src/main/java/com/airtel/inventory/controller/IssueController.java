package com.airtel.inventory.controller;

import com.airtel.inventory.model.Issue;
import com.airtel.inventory.repository.DeviceRepository;
import com.airtel.inventory.repository.EmployeeRepository;
import com.airtel.inventory.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class IssueController {

    @Autowired
    private IssueService issueService;
    
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/issues")
    public String listIssues(@RequestParam(required = false) String search,
                             Model model) {
        List<Issue> issues;
        if (search != null && !search.isEmpty()) {
            issues = issueService.searchIssues(search);
        } else {
            issues = issueService.getAllIssues();
        }
        
        model.addAttribute("issues", issues);
        model.addAttribute("searchValue", search);
        model.addAttribute("activePage", "issues");
        return "dashboard";
    }

    @GetMapping("/issues/report")
    public String showReportForm(Model model) {
        model.addAttribute("issue", new Issue());
        model.addAttribute("devices", deviceRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("activePage", "report-issue");
        return "dashboard";
    }

    @PostMapping("/issues/save")
    public String saveIssue(@RequestParam int deviceId,
                            @RequestParam int employeeId,
                            @RequestParam String issueType,
                            @RequestParam String priority,
                            @RequestParam String issueDescription,
                            RedirectAttributes redirectAttributes) {
        try {
            Issue issue = new Issue();
            issue.setDevice(deviceRepository.findById((long)deviceId).orElse(null));
            issue.setEmployee(employeeRepository.findById((long)employeeId).orElse(null));
            issue.setIssueType(issueType);
            issue.setPriority(priority);
            issue.setIssueDescription(issueDescription);
            issue.setReportedBy(1);
            
            issueService.saveIssue(issue);
            redirectAttributes.addFlashAttribute("successMessage", "Issue reported successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error reporting issue: " + e.getMessage());
        }
        return "redirect:/issues";
    }

    @GetMapping("/issues/update/{id}")
    public String showUpdateForm(@PathVariable int id, Model model) {
        Issue issue = issueService.getIssueById(id);
        model.addAttribute("issue", issue);
        model.addAttribute("activePage", "update-issue");
        return "dashboard";
    }

    @PostMapping("/issues/update-status/{id}")
    public String updateIssueStatus(@PathVariable int id,
                                    @RequestParam String status,
                                    @RequestParam(required = false) String resolutionNotes,
                                    RedirectAttributes redirectAttributes) {
        try {
            issueService.updateIssueStatus(id, status, resolutionNotes, 1);
            redirectAttributes.addFlashAttribute("successMessage", "Issue updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating issue: " + e.getMessage());
        }
        return "redirect:/issues";
    }
}