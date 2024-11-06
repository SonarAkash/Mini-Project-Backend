package com.TY_CSE.Project.Service;

import com.TY_CSE.Project.DTO.WasteUploadDTO;
import com.TY_CSE.Project.Model.AvailableWaste;
import com.TY_CSE.Project.Model.FoodWasteGenerator;
import com.TY_CSE.Project.Model.User;
import com.TY_CSE.Project.Repository.AvailableWasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class WasteService {

    @Autowired
    private AvailableWasteRepository availableWasteRepository;

    @Value("${upload.dir}")
    private String uploadDir; // This will get the value from application.properties

    @Autowired
    private UserService userRepository;

    public void uploadWaste(WasteUploadDTO wasteUploadDTO) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        // Ensure the upload directory exists
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (MultipartFile file : wasteUploadDTO.getImages()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + "\\" + fileName);
            Files.copy(file.getInputStream(), filePath);
            imageUrls.add(fileName); // Only add the filename, not the full path
        }


        AvailableWaste availableWaste = new AvailableWaste();
        availableWaste.setType(wasteUploadDTO.getType());
        availableWaste.setQuantity(wasteUploadDTO.getQuantity());
        availableWaste.setExpiryDate(wasteUploadDTO.getExpiryDate());
        availableWaste.setWasteCondition(wasteUploadDTO.getWasteCondition());
        availableWaste.setImagePaths(imageUrls); // Save URLs
        availableWaste.setDescription(wasteUploadDTO.getDescription());
        availableWaste.setStorageConditions(wasteUploadDTO.getStorageConditions());
        availableWaste.setTags(wasteUploadDTO.getTags());

        // User and generator association as before
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));
        FoodWasteGenerator generator = user.getFoodWasteGenerator();
        if (generator == null) {
            throw new RuntimeException("FoodWasteGenerator not found for user ID " + user.getId());
        }
        availableWaste.setGenerator(generator);

        availableWasteRepository.save(availableWaste);
    }


}
