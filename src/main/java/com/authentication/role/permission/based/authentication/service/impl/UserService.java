package com.authentication.role.permission.based.authentication.service.impl;


import com.authentication.role.permission.based.authentication.entity.User;
import com.authentication.role.permission.based.authentication.exception.UnknownException;
import com.authentication.role.permission.based.authentication.repository.IUserRepository;
import com.authentication.role.permission.based.authentication.security.CustomUserDetails;
import com.authentication.role.permission.based.authentication.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements IUserService, UserDetailsService {

    //    private final IRoleRepository roleRepository;
    private final IUserRepository iUserRepository;
//    private final PasswordEncoder passwordEncoder;


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


}