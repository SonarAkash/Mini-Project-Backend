package com.TY_CSE.Project.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AvailableWaste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Type of waste (e.g., vegetable, fruit, etc.)
    private int quantity; // Quantity in kg or appropriate unit
    private LocalDate expiryDate;
    private String wasteCondition; // Condition of the waste
    private String description; // Description of the waste
    private String storageConditions; // Storage conditions

    @ElementCollection(fetch = FetchType.EAGER) // Indicates that this field will be stored in a separate table
    @CollectionTable(name = "available_waste_tags", joinColumns = @JoinColumn(name = "available_waste_id"))
    @Column(name = "tag") // Name of the column in the tags table
    private List<String> tags; // Tags for categorization

    @ElementCollection // Indicates that this field will also be stored in a separate table
    @CollectionTable(name = "available_waste_images", joinColumns = @JoinColumn(name = "available_waste_id"))
    @Column(name = "image_path") // Name of the column in the images table
    private List<String> imagePaths; // List of image files

    // Foreign key to link to the food waste generator
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generator_id", nullable = false)
    private FoodWasteGenerator generator;
}
