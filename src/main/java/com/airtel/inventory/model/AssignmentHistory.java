package com.airtel.inventory.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_history")
public class AssignmentHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
    
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(name = "assigned_date", nullable = false)
    private LocalDateTime assignedDate;
    
    @Column(name = "returned_date")
    private LocalDateTime returnedDate;
    
    @Column(name = "assigned_by")
    private Long assignedBy;
    
    @Column(name = "returned_by")
    private Long returnedBy;
    
    @Column(name = "condition_when_assigned")
    private String conditionWhenAssigned;
    
    @Column(name = "condition_when_returned")
    private String conditionWhenReturned;
    
    private String notes;
    
    @Column(name = "return_notes")
    private String returnNotes;
    
    // Constructors
    public AssignmentHistory() {
        this.assignedDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Device getDevice() {
        return device;
    }
    
    public void setDevice(Device device) {
        this.device = device;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }
    
    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }
    
    public LocalDateTime getReturnedDate() {
        return returnedDate;
    }
    
    public void setReturnedDate(LocalDateTime returnedDate) {
        this.returnedDate = returnedDate;
    }
    
    public Long getAssignedBy() {
        return assignedBy;
    }
    
    public void setAssignedBy(Long assignedBy) {
        this.assignedBy = assignedBy;
    }
    
    public Long getReturnedBy() {
        return returnedBy;
    }
    
    public void setReturnedBy(Long returnedBy) {
        this.returnedBy = returnedBy;
    }
    
    public String getConditionWhenAssigned() {
        return conditionWhenAssigned;
    }
    
    public void setConditionWhenAssigned(String conditionWhenAssigned) {
        this.conditionWhenAssigned = conditionWhenAssigned;
    }
    
    public String getConditionWhenReturned() {
        return conditionWhenReturned;
    }
    
    public void setConditionWhenReturned(String conditionWhenReturned) {
        this.conditionWhenReturned = conditionWhenReturned;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getReturnNotes() {
        return returnNotes;
    }
    
    public void setReturnNotes(String returnNotes) {
        this.returnNotes = returnNotes;
    }
}