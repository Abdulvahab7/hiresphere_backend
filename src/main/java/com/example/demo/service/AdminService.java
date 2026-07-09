package com.example.demo.service;

import com.example.demo.dto.AdminUserDTO;
import com.example.demo.dto.DashboardStatsDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    // ================= Dashboard =================

    public DashboardStatsDTO getDashboardStats() {

        long totalUsers        = userRepository.count();
        long totalEmployers    = userRepository.countByRole(Role.EMPLOYER);
        long totalJobSeekers   = userRepository.countByRole(Role.JOB_SEEKER);
        long totalJobsPosted   = jobRepository.count();
        long totalApplications = applicationRepository.count();
        long activeJobs        = jobRepository.countByActive(true);
        long blockedUsers      = userRepository.countByBlocked(true);

        return new DashboardStatsDTO(
                totalUsers,
                totalEmployers,
                totalJobSeekers,
                totalJobsPosted,
                totalApplications,
                activeJobs,
                blockedUsers
        );
    }

    // ================= User Management =================

    public List<AdminUserDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private AdminUserDTO convertToDTO(User user) {

        return new AdminUserDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole(),
                user.isBlocked()
        );
    }

}
