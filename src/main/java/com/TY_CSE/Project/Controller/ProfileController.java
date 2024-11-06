package com.TY_CSE.Project.Controller;

import com.TY_CSE.Project.DTO.ProfileUpdateDTO;
import com.TY_CSE.Project.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody ProfileUpdateDTO profileUpdateDTO) {
        profileService.updateProfile(profileUpdateDTO);
        return ResponseEntity.ok("Profile updated successfully.");
    }
}
