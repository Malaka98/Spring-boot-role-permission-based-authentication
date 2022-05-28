package com.authentication.role.permission.based.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginDTO {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
