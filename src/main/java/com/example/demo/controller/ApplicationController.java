package com.example.demo.controller;

import com.example.demo.dto.ApplyRequest;
import com.example.demo.entity.Application;
import com.example.demo.entity.ApplicationStatus;
import com.example.demo.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply/{jobId}")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<Application> applyForJob(@PathVariable Long jobId,
                                                    @RequestBody ApplyRequest request,
                                                    Authentication auth) {
        return ResponseEntity.ok(
                applicationService.applyForJob(jobId, auth.getName(), request));
    }

    @GetMapping("/seeker")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<List<Application>> getSeekerApplications(Authentication auth) {
        return ResponseEntity.ok(applicationService.getSeekerApplications(auth.getName()));
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<List<Application>> getJobApplications(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getJobApplications(jobId));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<Application> updateStatus(@PathVariable Long id,
                                                     @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<String> withdrawApplication(@PathVariable Long id,
                                                       Authentication auth) {
        applicationService.withdrawApplication(id, auth.getName());
        return ResponseEntity.ok("Application deleted successfully.");
    }
}
