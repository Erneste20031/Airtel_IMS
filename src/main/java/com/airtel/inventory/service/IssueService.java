package com.airtel.inventory.service;

import com.airtel.inventory.model.Issue;
import com.airtel.inventory.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    public List<Issue> getAllIssues() {
        return issueRepository.findAllWithDetails();
    }

    public Issue getIssueById(int id) {
        return issueRepository.findByIdWithDetails(id);
    }

    @Transactional
    public Issue saveIssue(Issue issue) {
        if (issue.getId() == 0) {
            issue.setIssueDate(LocalDateTime.now());
            issue.setStatus("Reported");
        }
        return issueRepository.save(issue);
    }

    public void deleteIssue(int id) {
        issueRepository.deleteById(id);
    }

    public List<Issue> searchIssues(String search) {
        if (search == null || search.isEmpty()) {
            return getAllIssues();
        }
        return issueRepository.searchIssues(search);
    }

    public List<Issue> getIssuesByStatus(String status) {
        return issueRepository.findByStatus(status);
    }

    public List<Issue> getIssuesByPriority(String priority) {
        return issueRepository.findByPriority(priority);
    }

    public List<Issue> getOpenIssues() {
        return issueRepository.findOpenIssues();
    }

    public long getOpenIssuesCount() {
        return issueRepository.countOpenIssues();
    }

    @Transactional
    public Issue updateIssueStatus(int id, String status, String resolutionNotes, Integer resolvedBy) {
        Issue issue = getIssueById(id);
        if (issue == null) {
            throw new RuntimeException("Issue not found");
        }
        
        issue.setStatus(status);
        if (resolutionNotes != null && !resolutionNotes.isEmpty()) {
            issue.setResolutionNotes(resolutionNotes);
        }
        
        if (status.equals("Resolved") || status.equals("Closed")) {
            issue.setResolvedDate(LocalDateTime.now());
            issue.setResolvedBy(resolvedBy);
        }
        
        return issueRepository.save(issue);
    }
}