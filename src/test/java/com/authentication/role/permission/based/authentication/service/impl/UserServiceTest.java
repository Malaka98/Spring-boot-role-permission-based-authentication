package com.authentication.role.permission.based.authentication.service.impl;

import com.authentication.role.permission.based.authentication.dto.RoleDTO;
import com.authentication.role.permission.based.authentication.dto.UserDTO;
import com.authentication.role.permission.based.authentication.entity.Permission;
import com.authentication.role.permission.based.authentication.entity.Role;
import com.authentication.role.permission.based.authentication.repository.IPermissionRepository;
import com.authentication.role.permission.based.authentication.repository.IRoleRepository;
import com.authentication.role.permission.based.authentication.security.ApplicationRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
class UserServiceTest {

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IPermissionRepository iPermissionRepository;

    @Autowired
    private UserService userService;

    @Test
    void saveRole() {

        Permission read_permission = iPermissionRepository.findById(1L).orElse(null);
        Permission write_permission = iPermissionRepository.findById(2L).orElse(null);

        if (Objects.nonNull(read_permission) && Objects.nonNull(write_permission)) {
            userService.saveRole(RoleDTO.builder()
                    .id(1L)
                    .name(ApplicationRole.USER.name())
                    .permissions(List.of(read_permission))
                    .build());
            userService.saveRole(RoleDTO.builder()
                    .id(2L)
                    .name(ApplicationRole.SUPPER_USER.name())
                    .permissions(List.of(read_permission, write_permission))
                    .build());
        } else {
            throw new RuntimeException("Object can't be null");
        }

    }

    @Test
    void savePermission() {
        iPermissionRepository.save(Permission.builder()
                .id(1L)
                .accessPermission("READ")
                .build());

        iPermissionRepository.save(Permission.builder()
                .id(2L)
                .accessPermission("WRITE")
                .build());
    }

    @Test
    void saveUser() {
        Role role = roleRepository.findByName(ApplicationRole.USER.name());
        Role role2 = roleRepository.findByName(ApplicationRole.SUPPER_USER.name());
        log.info("Role=============>" + role.getName());
        log.info("Role2=============>" + role2.getName());
        Collection<RoleDTO> roles = new ArrayList<>();

        roles.add(RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .permissions(role.getPermissions())
                .build());
        roles.add(RoleDTO.builder()
                .id(role2.getId())
                .name(role2.getName())
                .permissions(role2.getPermissions())
                .build());

        UserDTO user = userService.saveUser(UserDTO.builder()
                .name("Sanduni")
                .username("root")
                .password("123")
                .email("sanduni@gmail.com")
                .address("abc")
                .role(roles)
                .build());

        UserDTO user2 = userService.saveUser(UserDTO.builder()
                .name("Malaka")
                .username("rootx")
                .password("123")
                .email("rootx@gmail.com")
                .address("abc")
                .role(roles)
                .build());

        UserDTO user3 = userService.saveUser(UserDTO.builder()
                .name("abc")
                .username("abc")
                .password("123")
                .email("abc@gmail.com")
                .address("abc")
                .role(List.of(RoleDTO.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .permissions(role.getPermissions())
                        .build()))
                .build());

        assertInstanceOf(UserDTO.class, user);
        assertNotNull(user);
        assertInstanceOf(UserDTO.class, user2);
        assertNotNull(user2);
        assertInstanceOf(UserDTO.class, user3);
        assertNotNull(user3);
    }
}