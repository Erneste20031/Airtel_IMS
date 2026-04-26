package com.airtel.inventory.service;

import com.airtel.inventory.model.AssignmentHistory;
import com.airtel.inventory.model.Device;
import com.airtel.inventory.model.Employee;
import com.airtel.inventory.repository.AssignmentHistoryRepository;
import com.airtel.inventory.repository.DeviceRepository;
import com.airtel.inventory.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentHistoryRepository assignmentRepository;
    
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    // Get all assignments
    public List<AssignmentHistory> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    // Get assignment by ID
    public AssignmentHistory getAssignmentById(Long id) {
        return assignmentRepository.findById(id).orElse(null);
    }

    // Get active assignments (not returned)
    public List<AssignmentHistory> getActiveAssignments() {
        return assignmentRepository.findActiveAssignments();
    }

    // Assign device to employee
    @Transactional
    public AssignmentHistory assignDevice(Long deviceId, Long employeeId, String conditionWhenAssigned, String notes) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        // Check if device is already assigned
        if (assignmentRepository.isDeviceAssigned(deviceId)) {
            throw new RuntimeException("Device is already assigned to someone");
        }
        
        // Update device status to "Assigned" (status_id = 2)
        device.setStatus(device.getStatus());
        deviceRepository.save(device);
        
        // Create assignment
        AssignmentHistory assignment = new AssignmentHistory();
        assignment.setDevice(device);
        assignment.setEmployee(employee);
        assignment.setAssignedDate(LocalDateTime.now());
        assignment.setConditionWhenAssigned(conditionWhenAssigned);
        assignment.setNotes(notes);
        
        return assignmentRepository.save(assignment);
    }

    // Return device
    @Transactional
    public AssignmentHistory returnDevice(Long deviceId, String conditionWhenReturned, String returnNotes) {
        AssignmentHistory assignment = assignmentRepository.getCurrentAssignmentByDevice(deviceId);
        if (assignment == null) {
            throw new RuntimeException("No active assignment found for this device");
        }
        
        // Update assignment
        assignment.setReturnedDate(LocalDateTime.now());
        assignment.setConditionWhenReturned(conditionWhenReturned);
        assignment.setReturnNotes(returnNotes);
        
        // Update device status to "Available" (status_id = 1)
        Device device = assignment.getDevice();
        device.setStatus(device.getStatus());
        deviceRepository.save(device);
        
        return assignmentRepository.save(assignment);
    }

    // Search assignments
    public List<AssignmentHistory> searchAssignments(String search) {
        if (search == null || search.isEmpty()) {
            return getAllAssignments();
        }
        return assignmentRepository.searchAssignments(search);
    }

    // Check if device is assigned
    public boolean isDeviceAssigned(Long deviceId) {
        return assignmentRepository.isDeviceAssigned(deviceId);
    }

    // Get current assignment for device
    public AssignmentHistory getCurrentAssignment(Long deviceId) {
        return assignmentRepository.getCurrentAssignmentByDevice(deviceId);
    }
}