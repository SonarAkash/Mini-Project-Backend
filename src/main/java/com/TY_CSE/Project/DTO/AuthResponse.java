package com.TY_CSE.Project.DTO;

import com.TY_CSE.Project.Model.Role;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String jwt;
    private Role role;
    private String msg;

    public AuthResponse(String jwt, Role role) {
        this.jwt = jwt;
        this.role = role;
    }
}
