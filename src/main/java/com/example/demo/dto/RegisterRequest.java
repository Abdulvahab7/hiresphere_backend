package com.example.demo.dto;
import com.example.demo.entity.Role;
import lombok.Data;
@Data
public class RegisterRequest {
    private String fullName;
    private String username;
    private String password;
    private Role role;
    private String companyName;
}
