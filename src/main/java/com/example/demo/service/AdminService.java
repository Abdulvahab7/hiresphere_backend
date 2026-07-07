package com.example.demo.service;

import com.example.demo.dto.DashboardStatsDTO;
import com.example.demo.entity.Role;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public DashboardStatsDTO getDashboardStats() {
        long totalUsers        = userRepository.count();
        long totalEmployers    = userRepository.countByRole(Role.EMPLOYER);
        long totalJobSeekers   = userRepository.countByRole(Role.JOB_SEEKER);
        long totalJobsPosted   = jobRepository.count();
        long totalApplications = applicationRepository.count();
        long activeJobs        = jobRepository.countByActive(true);
        long blockedUsers      = userRepository.countByBlocked(true);

        return new DashboardStatsDTO(
                totalUsers, totalEmployers, totalJobSeekers,
                totalJobsPosted, totalApplications, activeJobs, blockedUsers);
    }
}