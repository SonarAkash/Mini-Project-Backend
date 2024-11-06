package com.TY_CSE.Project.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AvailableWasteDTO {
    private Long id;
    private String type;
    private int quantity;
    private LocalDate expiryDate;
    private String wasteCondition;
    private String description;
    private String storageConditions;
    private List<String> tags;
    private List<String> imagePaths;
}
