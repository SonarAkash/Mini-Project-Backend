package com.TY_CSE.Project.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateDTO {

    private String name; // Either generatorName or companyName based on the role
    private String contactNumber;
    private String phoneNumber;
    private String address;
}
