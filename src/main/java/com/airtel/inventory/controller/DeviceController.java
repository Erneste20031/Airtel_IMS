package com.airtel.inventory.controller;

import com.airtel.inventory.model.Device;
import com.airtel.inventory.repository.DeviceStatusRepository;
import com.airtel.inventory.repository.DeviceTypeRepository;
import com.airtel.inventory.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;

@Controller
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private DeviceTypeRepository deviceTypeRepository;
    
    @Autowired
    private DeviceStatusRepository deviceStatusRepository;

    @GetMapping("/devices")
    public String listDevices(@RequestParam(required = false) String search,
                              Model model) {
        List<Device> devices;
        if (search != null && !search.isEmpty()) {
            devices = deviceService.searchDevices(search);
        } else {
            devices = deviceService.getAllDevices();
        }
        
        model.addAttribute("devices", devices);
        model.addAttribute("searchValue", search);
        model.addAttribute("activePage", "devices");
        model.addAttribute("deviceTypes", deviceTypeRepository.findAll());
        model.addAttribute("deviceStatuses", deviceStatusRepository.findAll());
        return "dashboard";
    }

    @GetMapping("/devices/add")
    public String showAddForm(Model model) {
        model.addAttribute("device", new Device());
        model.addAttribute("deviceTypes", deviceTypeRepository.findAll());
        model.addAttribute("deviceStatuses", deviceStatusRepository.findAll());
        model.addAttribute("activePage", "add-device");
        return "dashboard";
    }

    @PostMapping("/devices/save")
    public String saveDevice(@ModelAttribute Device device, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== SAVING DEVICE ===");
            System.out.println("Asset Tag: " + device.getAssetTag());
            System.out.println("Serial Number: " + device.getSerialNumber());
            
            device.setCreatedAt(LocalDate.now());
            device.setUpdatedAt(LocalDate.now());
            Device saved = deviceService.saveDevice(device);
            System.out.println("Saved with ID: " + saved.getId());
            
            redirectAttributes.addFlashAttribute("successMessage", "Device added successfully!");
            return "redirect:/devices";
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving device: " + e.getMessage());
            return "redirect:/devices/add";
        }
    }

    @GetMapping("/devices/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Device device = deviceService.getDeviceById(id);
        model.addAttribute("device", device);
        model.addAttribute("deviceTypes", deviceTypeRepository.findAll());
        model.addAttribute("deviceStatuses", deviceStatusRepository.findAll());
        model.addAttribute("activePage", "edit-device");
        return "dashboard";
    }

    @PostMapping("/devices/update/{id}")
    public String updateDevice(@PathVariable Long id, @ModelAttribute Device device, RedirectAttributes redirectAttributes) {
        try {
            device.setId(id);
            device.setUpdatedAt(LocalDate.now());
            deviceService.saveDevice(device);
            redirectAttributes.addFlashAttribute("successMessage", "Device updated successfully!");
            return "redirect:/devices";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating device: " + e.getMessage());
            return "redirect:/devices/edit/" + id;
        }
    }

    @GetMapping("/devices/delete/{id}")
    public String deleteDevice(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            deviceService.deleteDevice(id);
            redirectAttributes.addFlashAttribute("successMessage", "Device deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting device: " + e.getMessage());
        }
        return "redirect:/devices";
    }
}