package com.TY_CSE.Project.Service;

import com.TY_CSE.Project.DTO.AvailableWasteDTO;
import com.TY_CSE.Project.Model.AvailableWaste;
import com.TY_CSE.Project.Repository.AvailableWasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailableWasteService {

    @Autowired
    private AvailableWasteRepository availableWasteRepository;

    // Method to get all AvailableWaste entries
    public List<AvailableWaste> getAllAvailableWaste() {
        return availableWasteRepository.findAll();
    }

    public AvailableWasteDTO convertToDTO(AvailableWaste availableWaste) {
        AvailableWasteDTO dto = new AvailableWasteDTO();
        dto.setId(availableWaste.getId());
        dto.setType(availableWaste.getType());
        dto.setQuantity(availableWaste.getQuantity());
        dto.setExpiryDate(availableWaste.getExpiryDate());
        dto.setWasteCondition(availableWaste.getWasteCondition());
        dto.setDescription(availableWaste.getDescription());
        dto.setStorageConditions(availableWaste.getStorageConditions());
        dto.setTags(availableWaste.getTags());
        dto.setImagePaths(availableWaste.getImagePaths());
        return dto;
    }
}
