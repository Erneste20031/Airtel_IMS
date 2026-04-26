package com.airtel.inventory.controller;

import com.airtel.inventory.model.AssignmentHistory;
import com.airtel.inventory.repository.DeviceRepository;
import com.airtel.inventory.repository.EmployeeRepository;
import com.airtel.inventory.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    // Show assign device form
    @GetMapping("/assignments/assign")
    public String showAssignForm(Model model) {
        // Get only available devices (not assigned, status_id = 1)
        List<com.airtel.inventory.model.Device> availableDevices = deviceRepository.findByStatusId(1L);
        model.addAttribute("devices", availableDevices);
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("activePage", "assign");
        return "dashboard";
    }

    // Process assign device
    @PostMapping("/assignments/assign-device")
    public String assignDevice(@RequestParam Long deviceId,
                               @RequestParam Long employeeId,
                               @RequestParam String conditionWhenAssigned,
                               @RequestParam(required = false) String notes,
                               RedirectAttributes redirectAttributes) {
        try {
            assignmentService.assignDevice(deviceId, employeeId, conditionWhenAssigned, notes);
            redirectAttributes.addFlashAttribute("successMessage", "Device assigned successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error assigning device: " + e.getMessage());
        }
        return "redirect:/assignments/history";
    }

    // Show return device form
    @GetMapping("/assignments/return")
    public String showReturnForm(Model model) {
        // Get only assigned devices (status_id = 2)
        List<com.airtel.inventory.model.Device> assignedDevices = deviceRepository.findByStatusId(2L);
        model.addAttribute("devices", assignedDevices);
        model.addAttribute("activePage", "return");
        return "dashboard";
    }

    // Process return device
    @PostMapping("/assignments/return-device")
    public String returnDevice(@RequestParam Long deviceId,
                               @RequestParam String conditionWhenReturned,
                               @RequestParam(required = false) String returnNotes,
                               RedirectAttributes redirectAttributes) {
        try {
            assignmentService.returnDevice(deviceId, conditionWhenReturned, returnNotes);
            redirectAttributes.addFlashAttribute("successMessage", "Device returned successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error returning device: " + e.getMessage());
        }
        return "redirect:/assignments/history";
    }

    // Show assignment history
    @GetMapping("/assignments/history")
    public String assignmentHistory(@RequestParam(required = false) String search,
                                    Model model) {
        List<AssignmentHistory> assignments;
        if (search != null && !search.isEmpty()) {
            assignments = assignmentService.searchAssignments(search);
        } else {
            assignments = assignmentService.getAllAssignments();
        }
        
        model.addAttribute("assignments", assignments);
        model.addAttribute("searchValue", search);
        model.addAttribute("activePage", "history");
        return "dashboard";
    }
}