package com.example.demo.controller;

import com.example.demo.entity.UserProfile;
import com.example.demo.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<UserProfile> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userProfileService.getProfile(authentication.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfile> updateProfile(
            @RequestBody Map<String, Object> payload,
            Authentication authentication) {

        String fullName    = (String) payload.get("fullName");
        String about       = (String) payload.get("about");
        String skills      = (String) payload.get("skills");
        String resumeUrl   = (String) payload.get("resumeUrl");
        String companyName = (String) payload.get("companyName");

        UserProfile updateData = new UserProfile();
        updateData.setAbout(about);
        updateData.setSkills(skills);
        updateData.setResumeUrl(resumeUrl);
        updateData.setCompanyName(companyName);

        return ResponseEntity.ok(
                userProfileService.updateProfile(authentication.getName(), updateData, fullName));
    }
}
