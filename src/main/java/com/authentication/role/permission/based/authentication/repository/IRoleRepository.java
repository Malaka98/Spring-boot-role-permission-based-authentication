package com.authentication.role.permission.based.authentication.repository;

import com.authentication.role.permission.based.authentication.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
}
