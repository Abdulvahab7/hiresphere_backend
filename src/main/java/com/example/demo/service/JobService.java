package com.example.demo.service;

import com.example.demo.dto.JobResponse;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public JobService(JobRepository jobRepository,
                      UserRepository userRepository,
                      ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    public Job createJob(Job job, String username) {
        User employer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        job.setEmployer(employer);
        return jobRepository.save(job);
    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::toJobResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> getJobsByEmployer(String username) {
        User employer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return jobRepository.findByEmployerId(employer.getId())
                .stream()
                .map(this::toJobResponse)
                .collect(Collectors.toList());
    }

    public JobResponse updateJob(Long jobId, Job update, String username) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        User employer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new RuntimeException("Unauthorized to edit this job");
        }

        job.setTitle(update.getTitle());
        job.setDescription(update.getDescription());
        job.setLocation(update.getLocation());
        job.setSalary(update.getSalary());
        job.setRequiredSkills(update.getRequiredSkills());
        job.setExperienceRequired(update.getExperienceRequired());

        Job saved = jobRepository.save(job);
        return toJobResponse(saved);
    }

    @Transactional
    public void deleteJob(Long jobId) {
        applicationRepository.deleteByJobId(jobId);
        jobRepository.deleteById(jobId);
    }

    public List<JobResponse> searchJobs(String keyword) {
        return jobRepository
                .findByTitleContainingIgnoreCaseOrRequiredSkillsContainingIgnoreCaseOrLocationContainingIgnoreCase(
                        keyword, keyword, keyword)
                .stream()
                .map(this::toJobResponse)
                .collect(Collectors.toList());
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    private JobResponse toJobResponse(Job job) {
        long count = applicationRepository.countByJobId(job.getId());
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getRequiredSkills(),
                job.getLocation(),
                job.getSalary(),
                job.getExperienceRequired(),
                job.getCreatedAt(),
                Boolean.TRUE.equals(job.getActive()),
                count
        );
    }
}
