package com.TY_CSE.Project.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WasteBookingSummaryDTO {
    private Long id;
    private String type;  // Changed from title to type
    private int quantity;
    private LocalDateTime bookingDate;
    private String status;

    // Getters and setters
}
