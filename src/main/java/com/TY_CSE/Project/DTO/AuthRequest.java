package com.TY_CSE.Project.DTO;

import com.TY_CSE.Project.Model.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String email;
    private String password;
    private Role role;
}
