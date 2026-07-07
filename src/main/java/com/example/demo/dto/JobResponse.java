package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String requiredSkills;
    private String location;
    private Long salary;
    private Integer experienceRequired;
    private Date createdAt;
    private boolean active;
    private long applicationsCount;
}
