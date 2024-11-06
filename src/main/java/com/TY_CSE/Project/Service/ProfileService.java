package com.TY_CSE.Project.Service;

import com.TY_CSE.Project.DTO.ProfileUpdateDTO;
import com.TY_CSE.Project.Model.FoodWasteGenerator;
import com.TY_CSE.Project.Model.BiogasCompany;
import com.TY_CSE.Project.Model.Role;
import com.TY_CSE.Project.Model.User;
import com.TY_CSE.Project.Repository.FoodWasteGeneratorRepository;
import com.TY_CSE.Project.Repository.BiogasCompanyRepository;
import com.TY_CSE.Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodWasteGeneratorRepository foodWasteGeneratorRepository;

    @Autowired
    private BiogasCompanyRepository biogasCompanyRepository;

    public void updateProfile(ProfileUpdateDTO profileUpdateDTO) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the email from the authentication

        // Fetch user details directly from the repository
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found.");
        }

        User user = optionalUser.get();

        // Check the user's role and update the corresponding entity
        if (user.getRole() == Role.FOOD_WASTE_GENERATOR) {
            // Update FoodWasteGenerator entity
            FoodWasteGenerator generator = user.getFoodWasteGenerator();
            if (generator != null) {
                // If the role is FOOD_WASTE_GENERATOR, use name for generatorName
                generator.setGeneratorName(profileUpdateDTO.getName());
                generator.setContactNumber(profileUpdateDTO.getContactNumber());
                generator.setPhoneNumber(profileUpdateDTO.getPhoneNumber());
                generator.setAddress(profileUpdateDTO.getAddress());
                foodWasteGeneratorRepository.save(generator);
            }
        } else if (user.getRole() == Role.BIOGAS_COMPANY) {
            // Update BiogasCompany entity
            BiogasCompany company = user.getBiogasCompany();
            if (company != null) {
                // If the role is BIOGAS_COMPANY, use name for companyName
                company.setCompanyName(profileUpdateDTO.getName());
                company.setContactPerson(profileUpdateDTO.getContactNumber()); // Adjust contact fields as needed
                company.setPhoneNumber(profileUpdateDTO.getPhoneNumber());
                company.setAddress(profileUpdateDTO.getAddress());
                biogasCompanyRepository.save(company);
            }
        } else {
            throw new RuntimeException("Unsupported role.");
        }
    }
}
