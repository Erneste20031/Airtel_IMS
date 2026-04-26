package com.airtel.inventory.repository;

import com.airtel.inventory.model.AssignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentHistoryRepository extends JpaRepository<AssignmentHistory, Long> {
    
    // Find active assignments (not returned)
    @Query("SELECT a FROM AssignmentHistory a WHERE a.returnedDate IS NULL")
    List<AssignmentHistory> findActiveAssignments();
    
    // Find assignments by device
    List<AssignmentHistory> findByDeviceId(Long deviceId);
    
    // Find assignments by employee
    List<AssignmentHistory> findByEmployeeId(Long employeeId);
    
    // Check if device is currently assigned
    @Query("SELECT COUNT(a) > 0 FROM AssignmentHistory a WHERE a.device.id = :deviceId AND a.returnedDate IS NULL")
    boolean isDeviceAssigned(@Param("deviceId") Long deviceId);
    
    // Get current assignment for a device
    @Query("SELECT a FROM AssignmentHistory a WHERE a.device.id = :deviceId AND a.returnedDate IS NULL")
    AssignmentHistory getCurrentAssignmentByDevice(@Param("deviceId") Long deviceId);
    
    // Search assignments
    @Query("SELECT a FROM AssignmentHistory a WHERE " +
           "LOWER(a.device.assetTag) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.device.serialNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.employee.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.employee.employeeId) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<AssignmentHistory> searchAssignments(@Param("search") String search);
}