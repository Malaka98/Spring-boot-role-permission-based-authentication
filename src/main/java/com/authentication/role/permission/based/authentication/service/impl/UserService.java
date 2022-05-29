package com.authentication.role.permission.based.authentication.service.impl;


import com.authentication.role.permission.based.authentication.dto.PermissionDTO;
import com.authentication.role.permission.based.authentication.dto.RoleDTO;
import com.authentication.role.permission.based.authentication.dto.UserDTO;
import com.authentication.role.permission.based.authentication.entity.Permission;
import com.authentication.role.permission.based.authentication.entity.Role;
import com.authentication.role.permission.based.authentication.entity.User;
import com.authentication.role.permission.based.authentication.exception.UnknownException;
import com.authentication.role.permission.based.authentication.repository.IPermissionRepository;
import com.authentication.role.permission.based.authentication.repository.IRoleRepository;
import com.authentication.role.permission.based.authentication.repository.IUserRepository;
import com.authentication.role.permission.based.authentication.security.CustomUserDetails;
import com.authentication.role.permission.based.authentication.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements IUserService, UserDetailsService {

    private final IRoleRepository roleRepository;
    private final IPermissionRepository iPermissionRepository;
    private final IUserRepository iUserRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            User user = iUserRepository.findUserByUsername(username);
            if (user == null) {
                log.error("User not found in the database");
                throw new UsernameNotFoundException("User not found in the database");
            } else {
                log.info("User found in the database");
            }

            return new CustomUserDetails(user);
        } catch (Exception ex) {
            throw new UnknownException(ex.getMessage() + " ⚠⚠⚠");
        }
    }


    @Override
    public RoleDTO saveRole(RoleDTO role) {

        roleRepository.save(Role.builder()
                .name(role.getName())
                .permissions(role.getPermissions())
                .build());

        return role;
    }

    @Override
    public PermissionDTO savePermission(PermissionDTO permission) {

        iPermissionRepository.save(Permission.builder()
                .accessPermission(permission.getAccessPermission())
                .build());


        return permission;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {

        Collection<Role> roles = new ArrayList<>();
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userDTO.getRole().forEach(roleDTO -> {

            roles.add(Role.builder()
                    .id(roleDTO.getId())
                    .name(roleDTO.getName())
                    .permissions(roleDTO.getPermissions())
                    .build());
        });

        iUserRepository.save(User.builder()
                .name(userDTO.getName())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .role(roles)
                .build());

        return userDTO;
    }
}