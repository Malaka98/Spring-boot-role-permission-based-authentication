package com.authentication.role.permission.based.authentication.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {

    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String address;
    private Collection<RoleDTO> role;
}
