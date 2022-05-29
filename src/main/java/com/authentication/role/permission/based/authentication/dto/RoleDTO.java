package com.authentication.role.permission.based.authentication.dto;

import com.authentication.role.permission.based.authentication.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoleDTO {

    private Long id;
    private String name;
    private Collection<Permission> permissions;
}
