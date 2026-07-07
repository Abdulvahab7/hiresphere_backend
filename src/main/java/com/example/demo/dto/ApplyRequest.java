package com.example.demo.dto;
import lombok.Data;
@Data
public class ApplyRequest {
    private String fullName;
    private String email;
    private String phone;
    private String skills;
    private Integer experience;
    private String resumeUrl;
    private String coverLetter;
    private Double expectedSalary;
    private String noticePeriod;
}
