package com.TY_CSE.Project.Controller;

import com.TY_CSE.Project.DTO.WasteUploadDTO;
import com.TY_CSE.Project.Service.WasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FoodWasteGeneratorController {

    private final WasteService wasteService;

    @Autowired
    public FoodWasteGeneratorController(WasteService wasteService) {
        this.wasteService = wasteService;
    }

    @PostMapping("/uploadWaste")
    public ResponseEntity<String> uploadWaste(@ModelAttribute WasteUploadDTO wasteUploadDTO) {
        try {
            wasteService.uploadWaste(wasteUploadDTO);
            return ResponseEntity.ok("Waste uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading waste: " + e.getMessage());
        }
    }
}
