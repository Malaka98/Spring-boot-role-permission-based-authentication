package com.authentication.role.permission.based.authentication.repository;

import com.authentication.role.permission.based.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);
}
