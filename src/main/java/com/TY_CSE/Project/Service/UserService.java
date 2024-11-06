package com.TY_CSE.Project.Service;

import com.TY_CSE.Project.Model.BiogasCompany;
import com.TY_CSE.Project.Model.FoodWasteGenerator;
import com.TY_CSE.Project.Model.Role;
import com.TY_CSE.Project.Model.User;
import com.TY_CSE.Project.Repository.UserRepository;
import com.TY_CSE.Project.Repository.FoodWasteGeneratorRepository;
import com.TY_CSE.Project.Repository.BiogasCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FoodWasteGeneratorRepository foodWasteGeneratorRepository;

    @Autowired
    private BiogasCompanyRepository biogasCompanyRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(String email, String password, Role role) {
        // Create and save the user with encoded password
        User user = new User();
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(role);
        User savedUser = userRepository.save(user);

        // Create and associate the FoodWasteGenerator or BiogasCompany based on role
        if (role == Role.FOOD_WASTE_GENERATOR) {
            FoodWasteGenerator generator = new FoodWasteGenerator();
            generator.setUser(savedUser); // Associate with user
            foodWasteGeneratorRepository.save(generator); // Save empty fields for later editing
        } else if (role == Role.BIOGAS_COMPANY) {
            BiogasCompany company = new BiogasCompany();
            company.setUser(savedUser); // Associate with user
            biogasCompanyRepository.save(company);
        }

        return savedUser;
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
