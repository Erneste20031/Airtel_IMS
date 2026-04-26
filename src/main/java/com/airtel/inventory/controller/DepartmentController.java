package com.airtel.inventory.controller;

import com.airtel.inventory.model.Department;
import com.airtel.inventory.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // List all departments
    @GetMapping("/departments")
    public String listDepartments(@RequestParam(required = false) String search,
                                  Model model) {
        List<Department> departments;
        if (search != null && !search.isEmpty()) {
            departments = departmentService.searchDepartments(search);
        } else {
            departments = departmentService.getAllDepartments();
        }
        
        model.addAttribute("departments", departments);
        model.addAttribute("searchValue", search);
        model.addAttribute("activePage", "departments");
        return "dashboard";
    }

    // Show add department form
    @GetMapping("/departments/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("activePage", "add-department");
        return "dashboard";
    }

    // Save new department
    @PostMapping("/departments/save")
    public String saveDepartment(@ModelAttribute Department department, RedirectAttributes redirectAttributes) {
        try {
            departmentService.saveDepartment(department);
            redirectAttributes.addFlashAttribute("successMessage", "Department added successfully!");
            return "redirect:/departments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving department: " + e.getMessage());
            return "redirect:/departments/add";
        }
    }

    // Show edit department form
    @GetMapping("/departments/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        model.addAttribute("department", department);
        model.addAttribute("activePage", "edit-department");
        return "dashboard";
    }

    // Update department
    @PostMapping("/departments/update/{id}")
    public String updateDepartment(@PathVariable Long id, @ModelAttribute Department department, RedirectAttributes redirectAttributes) {
        try {
            department.setId(id);
            departmentService.saveDepartment(department);
            redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully!");
            return "redirect:/departments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating department: " + e.getMessage());
            return "redirect:/departments/edit/" + id;
        }
    }

    // Delete department
    @GetMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteDepartment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting department: " + e.getMessage());
        }
        return "redirect:/departments";
    }
}