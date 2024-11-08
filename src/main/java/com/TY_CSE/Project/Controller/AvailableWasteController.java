package com.TY_CSE.Project.Controller;

import com.TY_CSE.Project.DTO.AvailableWasteDTO;
import com.TY_CSE.Project.Model.AvailableWaste;
import com.TY_CSE.Project.Model.User;
import com.TY_CSE.Project.Repository.AvailableWasteRepository;
import com.TY_CSE.Project.Service.AvailableWasteService;
import com.TY_CSE.Project.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AvailableWasteController {

    @Autowired
    private AvailableWasteService availableWasteService;


    @Value("${upload.dir}")
    private String uploadDir;
    @Autowired
    private AvailableWasteRepository availableWasteRepository;

    @GetMapping("/available-waste")
    public ResponseEntity<List<AvailableWasteDTO>> getAllAvailableWaste() {
        List<AvailableWaste> availableWastes = availableWasteService.getAllAvailableWaste();
        if (availableWastes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Returns 204 if no data is found
        }

        List<AvailableWasteDTO> dtoList = availableWastes.stream()
                .map(availableWasteService::convertToDTO)
                .collect(Collectors.toList());

        List<AvailableWasteDTO> newDTOList = new LinkedList<>();
        for(int i=0; i<dtoList.size(); i++){
            AvailableWasteDTO waste = dtoList.get(i);
            if(waste.getQuantity() != 0){
                newDTOList.add(waste);
            }
        }
        return ResponseEntity.ok(newDTOList); // Returns 200 with data if available
    }

    @GetMapping("/uploads/{filename}")
    public ResponseEntity<FileSystemResource> getImage(@PathVariable String filename) {
        File file = new File(uploadDir + "/" + filename); // Concatenate the upload directory with the filename
        if (file.exists()) {
            return ResponseEntity.ok(new FileSystemResource(file)); // Return the file resource
        }
        return ResponseEntity.notFound().build(); // Return 404 if the file doesn't exist
    }

    @GetMapping("/available-waste/{id}")
    public ResponseEntity<AvailableWasteDTO> getAvailableWaste(@PathVariable Long id) {
        Optional<AvailableWaste> optionalAvailableWaste = availableWasteRepository.findById(id);
        if (optionalAvailableWaste.isEmpty()) {
            throw new RuntimeException("Waste item not found.");
        }

        AvailableWaste availableWaste = optionalAvailableWaste.get();
        AvailableWasteDTO dto = availableWasteService.convertToDTO(availableWaste);
        return ResponseEntity.ok(dto); // Returns 200 with data if available
    }


}
