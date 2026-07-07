package com.example.demo.service;

import com.example.demo.dto.ApplyRequest;
import com.example.demo.entity.Application;
import com.example.demo.entity.ApplicationStatus;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserProfileRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                               JobRepository jobRepository,
                               UserRepository userRepository,
                               UserProfileRepository userProfileRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public Application applyForJob(Long jobId, String username, ApplyRequest request) {
        User seeker = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (applicationRepository.existsByJobIdAndSeekerId(jobId, seeker.getId())) {
            throw new RuntimeException("Already applied");
        }

        int matchScore = calculateMatchScore(job.getRequiredSkills(), request.getSkills());

        Application application = new Application();
        application.setJob(job);
        application.setSeeker(seeker);
        application.setMatchScore(matchScore);
        application.setFullName(request.getFullName());
        application.setEmail(request.getEmail());
        application.setPhone(request.getPhone());
        application.setSkills(request.getSkills());
        application.setExperience(request.getExperience());
        application.setResumeUrl(request.getResumeUrl());
        application.setCoverLetter(request.getCoverLetter());
        application.setExpectedSalary(request.getExpectedSalary());
        application.setNoticePeriod(request.getNoticePeriod());

        return applicationRepository.save(application);
    }

    public List<Application> getSeekerApplications(String username) {
        User seeker = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return applicationRepository.findBySeekerId(seeker.getId());
    }

    public List<Application> getJobApplications(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public Application updateStatus(@NonNull Long applicationId, ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(status);
        return applicationRepository.save(application);
    }

    public void withdrawApplication(@NonNull Long applicationId, String username) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        User seeker = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!application.getSeeker().getId().equals(seeker.getId())) {
            throw new RuntimeException("Unauthorized to withdraw this application");
        }

        applicationRepository.deleteById(applicationId);
    }

    /**
     * Calculates the percentage of required skills covered by the applicant's skills.
     * Splits requiredSkills by comma and checks coverage against applicantSkills.
     * Returns an integer percentage 0–100.
     */
    private int calculateMatchScore(String requiredSkills, String applicantSkills) {
        if (requiredSkills == null || requiredSkills.isBlank()) return 0;
        if (applicantSkills == null || applicantSkills.isBlank()) return 0;

        String[] required = Arrays.stream(requiredSkills.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        String applicantLower = applicantSkills.toLowerCase();

        if (required.length == 0) return 0;

        long matched = Arrays.stream(required)
                .filter(applicantLower::contains)
                .count();

        return (int) Math.round((matched * 100.0) / required.length);
    }
}
