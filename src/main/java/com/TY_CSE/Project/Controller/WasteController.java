package com.TY_CSE.Project.Controller;


import com.TY_CSE.Project.DTO.BookWasteRequestDTO;
import com.TY_CSE.Project.Model.*;
import com.TY_CSE.Project.Repository.UserRepository;
import com.TY_CSE.Project.Repository.WasteOrderRepository;
import com.TY_CSE.Project.Repository.AvailableWasteRepository;
import com.TY_CSE.Project.Repository.BiogasCompanyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class WasteController {

    @Autowired
    private WasteOrderRepository wasteOrderRepository;

    @Autowired
    private AvailableWasteRepository availableWasteRepository;

    @Autowired
    private BiogasCompanyRepository biogasCompanyRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/bookWaste/{wasteId}")
    public ResponseEntity<String> bookWaste(@PathVariable Long wasteId,
                                            @RequestBody BookWasteRequestDTO bookWasteRequest) {
        // Access quantity from the BookWasteRequest object
        int quantity = bookWasteRequest.getQuantity();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the email from the authentication

        // Fetch user details directly from the repository
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        User user = optionalUser.get();
        BiogasCompany biogasCompany = null;

        if(user.getRole() == Role.FOOD_WASTE_GENERATOR){
            return ResponseEntity.status(409).body("Conflict : food waste generator can't book item ");
        }else {
            biogasCompany = user.getBiogasCompany();
        }

        // Retrieve the waste entry from AvailableWaste by wasteId
        AvailableWaste availableWaste = availableWasteRepository.findById(wasteId)
                .orElseThrow(() -> new IllegalArgumentException("Waste item not found"));

        if (availableWaste.getQuantity() < quantity) {
            return ResponseEntity.status(400).body("Not enough waste available to book.");
        }

        // Create the waste order
        WasteOrder wasteOrder = new WasteOrder();
        wasteOrder.setBiogasCompany(biogasCompany);
        wasteOrder.setAvailableWaste(availableWaste);
        wasteOrder.setOrderDate(LocalDateTime.now());
        wasteOrder.setStatus("BOOKED");
        wasteOrder.setQuantity(quantity); // Set quantity in the order

        // Save the waste order in the database
        wasteOrderRepository.save(wasteOrder);

        // Update the available waste quantity (reduce by the booked quantity)
        availableWaste.setQuantity(availableWaste.getQuantity() - quantity);

        // If the quantity is now 0, remove the waste entry from the table
        if (availableWaste.getQuantity() == 0) {
            availableWasteRepository.delete(availableWaste);
        } else {
            availableWasteRepository.save(availableWaste);
        }

        return ResponseEntity.ok("Waste booked successfully.");
    }
}

