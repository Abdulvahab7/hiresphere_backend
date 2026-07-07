package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {
    private long totalUsers;
    private long totalEmployers;
    private long totalJobSeekers;
    private long totalJobsPosted;
    private long totalApplications;
    private long activeJobs;
    private long blockedUsers;
}
