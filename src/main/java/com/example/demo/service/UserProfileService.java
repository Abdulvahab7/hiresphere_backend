package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserProfile;
import com.example.demo.repository.UserProfileRepository;
import com.example.demo.repository.UserRepository;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfile getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    UserProfile profile = new UserProfile();
                    profile.setUser(user);
                    return userProfileRepository.save(profile);
                });
    }

    @Transactional
    public UserProfile updateProfile(@NotBlank String username,@NotBlank UserProfile updateData, String fullName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (fullName != null && !fullName.isBlank()) {
            user.setFullName(fullName);
            userRepository.save(user);
        }

        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user);
                    return p;
                });

        if (updateData.getAbout() != null)       profile.setAbout(updateData.getAbout());
        if (updateData.getSkills() != null)      profile.setSkills(updateData.getSkills());
        if (updateData.getResumeUrl() != null)   profile.setResumeUrl(updateData.getResumeUrl());
        if (updateData.getCompanyName() != null) profile.setCompanyName(updateData.getCompanyName());

        return userProfileRepository.save(profile);
    }
}
