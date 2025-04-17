package com.TY_CSE.Project.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BiogasBookedOrderDTO {
    private Long orderId; // Unique ID for each order
    private String type;
    private List<String> tags;
    private LocalDateTime orderDate;
    private String generatorName;
    private int quantity;
    private String contactNumber;
    private String imagePath; // Only one image path
    private String status;
}
