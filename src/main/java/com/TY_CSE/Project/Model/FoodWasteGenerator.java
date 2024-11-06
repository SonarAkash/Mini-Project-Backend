package com.TY_CSE.Project.Model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "food_waste_generator")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FoodWasteGenerator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String generatorName; // Assuming the generator might have a name
    private String contactNumber;
    private String phoneNumber;
    private String address;

    // One-to-One relationship with User
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
