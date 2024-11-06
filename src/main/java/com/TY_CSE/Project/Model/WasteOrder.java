package com.TY_CSE.Project.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WasteOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key to link to the biogas company
    @ManyToOne
    @JoinColumn(name = "biogas_company_id")
    private BiogasCompany biogasCompany;

    // Foreign key to link to the available waste
    @ManyToOne
    @JoinColumn(name = "available_waste_id")
    private AvailableWaste availableWaste;

    private LocalDateTime orderDate;
    private String status; // e.g., booked, completed
    private int quantity;

}
