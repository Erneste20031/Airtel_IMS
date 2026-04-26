package com.airtel.inventory.repository;

import com.airtel.inventory.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {
    
    List<Issue> findByStatus(String status);
    List<Issue> findByPriority(String priority);
    
    @Query("SELECT i FROM Issue i LEFT JOIN FETCH i.device LEFT JOIN FETCH i.employee ORDER BY i.issueDate DESC")
    List<Issue> findAllWithDetails();
    
    @Query("SELECT i FROM Issue i LEFT JOIN FETCH i.device LEFT JOIN FETCH i.employee WHERE i.id = :id")
    Issue findByIdWithDetails(@Param("id") int id);
    
    @Query("SELECT COUNT(i) FROM Issue i WHERE i.status NOT IN ('Resolved', 'Closed')")
    long countOpenIssues();
    
    @Query("SELECT i FROM Issue i WHERE i.status NOT IN ('Resolved', 'Closed') ORDER BY i.issueDate DESC")
    List<Issue> findOpenIssues();
    
    @Query("SELECT i FROM Issue i WHERE " +
           "LOWER(i.issueDescription) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.issueType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.priority) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.status) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.device.assetTag) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.device.brand) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.employee.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Issue> searchIssues(@Param("search") String search);
}