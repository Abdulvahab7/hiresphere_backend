package com.example.demo.controller;

import com.example.demo.dto.JobResponse;
import com.example.demo.entity.Job;
import com.example.demo.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYER') or hasAuthority('ADMIN')")
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job,
                                         Authentication authentication) {
        return ResponseEntity.ok(jobService.createJob(job, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/employer")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<List<JobResponse>> getEmployerJobs(Authentication authentication) {
        return ResponseEntity.ok(jobService.getJobsByEmployer(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        // wrap in a JobResponse with applicationsCount
        List<JobResponse> all = jobService.getAllJobs();
        return all.stream()
                .filter(jr -> jr.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long id,
                                                  @RequestBody Job job,
                                                  Authentication authentication) {
        return ResponseEntity.ok(jobService.updateJob(id, job, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok("Job deleted successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobResponse>> searchJobs(@RequestParam String keyword) {
        return ResponseEntity.ok(jobService.searchJobs(keyword));
    }
}
