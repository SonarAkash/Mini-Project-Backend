package com.TY_CSE.Project.DTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WasteUploadDTO {
    private String type; // Type of food waste
    private int quantity; // Quantity of food waste
    private LocalDate expiryDate; // Expiry date of food waste
    private String wasteCondition; // Condition of the food waste
    private List<MultipartFile> images; // List of image files
    private String description; // Description of the food waste
    private String storageConditions; // Storage conditions for the waste
    private List<String> tags; // Tags for categorization
}
