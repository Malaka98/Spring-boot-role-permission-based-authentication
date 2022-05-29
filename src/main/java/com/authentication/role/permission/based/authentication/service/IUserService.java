package com.authentication.role.permission.based.authentication.service;

import com.authentication.role.permission.based.authentication.dto.PermissionDTO;
import com.authentication.role.permission.based.authentication.dto.RoleDTO;
import com.authentication.role.permission.based.authentication.dto.UserDTO;

public interface IUserService {
    RoleDTO saveRole(RoleDTO role);

    PermissionDTO savePermission(PermissionDTO permission);

    UserDTO saveUser(UserDTO userDTO);
}
