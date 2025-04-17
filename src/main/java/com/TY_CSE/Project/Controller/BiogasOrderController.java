package com.TY_CSE.Project.Controller;

import com.TY_CSE.Project.DTO.BiogasBookedOrderDTO;
import com.TY_CSE.Project.Model.BiogasCompany;
import com.TY_CSE.Project.Model.User;
import com.TY_CSE.Project.Repository.UserRepository;
import com.TY_CSE.Project.Service.WasteOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class BiogasOrderController {

    @Autowired
    private WasteOrderService wasteOrderService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/bookedOrders")
    public ResponseEntity<List<BiogasBookedOrderDTO>> getAllBookedOrdersForCompany() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));
        BiogasCompany biogasCompany = user.getBiogasCompany();
        if (biogasCompany == null) {
            throw new RuntimeException("biogasCompany not found for user ID " + user.getId());
        }
        List<BiogasBookedOrderDTO> orders = wasteOrderService.getAllBookedOrdersForCompany(biogasCompany.getId());
        return ResponseEntity.ok(orders);
    }
}
