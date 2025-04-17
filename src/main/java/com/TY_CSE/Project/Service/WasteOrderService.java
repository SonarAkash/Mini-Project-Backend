package com.TY_CSE.Project.Service;

import com.TY_CSE.Project.DTO.BiogasBookedOrderDTO;
import com.TY_CSE.Project.DTO.WasteBookingSummaryDTO;
import com.TY_CSE.Project.Model.WasteOrder;
import com.TY_CSE.Project.Repository.WasteOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WasteOrderService {
    @Autowired
    private WasteOrderRepository wasteOrderRepository;

    public List<WasteBookingSummaryDTO> getBookingsForGenerator(Long generatorId) {
        List<WasteOrder> orders = wasteOrderRepository.findByAvailableWasteGeneratorId(generatorId);

        return orders.stream().map(order -> {
            WasteBookingSummaryDTO dto = new WasteBookingSummaryDTO();
            dto.setId(order.getId());
            dto.setType(order.getAvailableWaste().getType());  // Accessing type from AvailableWaste
            dto.setQuantity(order.getQuantity());
            dto.setBookingDate(order.getOrderDate());
            dto.setStatus(order.getStatus());
            System.out.println(dto.toString());
            return dto;
        }).collect(Collectors.toList());
    }

    public boolean markOrderAsCompleted(Long orderId, Long generatorId) {
        // Fetch the order and confirm its status
        WasteOrder order = wasteOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Verify that the order is associated with this generator and is currently "Booked"
        if (order.getAvailableWaste().getGenerator().getId().equals(generatorId) &&
                "BOOKED".equals(order.getStatus())) {

            // Update status to "Completed"
            order.setStatus("COMPLETED");
            wasteOrderRepository.save(order);
            return true; // Successfully updated
        }

        return false; // Either not booked or not associated with the generator
    }

    public List<BiogasBookedOrderDTO> getAllBookedOrdersForCompany(Long biogasCompanyId) {
        List<WasteOrder> orders = wasteOrderRepository.findByBiogasCompanyId(biogasCompanyId);

        return orders.stream().map(order -> {
            BiogasBookedOrderDTO dto = new BiogasBookedOrderDTO();
            dto.setOrderId(order.getId());
            dto.setType(order.getAvailableWaste().getType());
            dto.setTags(order.getAvailableWaste().getTags());
            dto.setOrderDate(order.getOrderDate());
            dto.setGeneratorName(order.getAvailableWaste().getGenerator().getGeneratorName());
            dto.setQuantity(order.getQuantity());
            dto.setContactNumber(order.getAvailableWaste().getGenerator().getContactNumber());
            dto.setStatus(order.getStatus());

            // Set the first image if available
            if (!order.getAvailableWaste().getImagePaths().isEmpty()) {
                dto.setImagePath(order.getAvailableWaste().getImagePaths().get(0));
            }

            return dto;
        }).collect(Collectors.toList());
    }

}

