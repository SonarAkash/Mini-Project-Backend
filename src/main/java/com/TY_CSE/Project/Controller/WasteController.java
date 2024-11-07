package com.TY_CSE.Project.Controller;


import com.TY_CSE.Project.DTO.BookWasteRequestDTO;
import com.TY_CSE.Project.DTO.WasteBookingSummaryDTO;
import com.TY_CSE.Project.Model.*;
import com.TY_CSE.Project.Repository.UserRepository;
import com.TY_CSE.Project.Repository.WasteOrderRepository;
import com.TY_CSE.Project.Repository.AvailableWasteRepository;
import com.TY_CSE.Project.Repository.BiogasCompanyRepository;

import com.TY_CSE.Project.Service.WasteOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
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
        wasteOrder.setGeneratorId(availableWaste.getGenerator().getId());

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


    @Autowired
    private WasteOrderService wasteOrderService;

    @GetMapping("/generator/bookings")
    public ResponseEntity<List<WasteBookingSummaryDTO>> getBookingsForGenerator(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty() || !optionalUser.get().getRole().equals(Role.FOOD_WASTE_GENERATOR)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = optionalUser.get();
        Long generatorId = user.getFoodWasteGenerator().getId();

        List<WasteBookingSummaryDTO> bookings = wasteOrderService.getBookingsForGenerator(generatorId);
        return ResponseEntity.ok(bookings);
    }

    @PatchMapping("/generator/complete-order/{orderId}")
    public ResponseEntity<String> completeOrder(@PathVariable Long orderId, Authentication authentication) {
        // Get the generator ID from the authenticated user
        String email = authentication.getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty() || !optionalUser.get().getRole().equals(Role.FOOD_WASTE_GENERATOR)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied.");
        }

        User user = optionalUser.get();
        Long generatorId = user.getFoodWasteGenerator().getId();

        // Attempt to mark the order as completed
        boolean isUpdated = wasteOrderService.markOrderAsCompleted(orderId, generatorId);
        if (isUpdated) {
            return ResponseEntity.ok("Order marked as completed."); // Success response for JS
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Order could not be completed.");
        }
    }

}

