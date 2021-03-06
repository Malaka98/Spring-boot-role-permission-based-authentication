package com.authentication.role.permission.based.authentication.security;

import com.authentication.role.permission.based.authentication.security.filter.CustomAuthenticationFilter;
import com.authentication.role.permission.based.authentication.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class ApiWebSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final AuthenticationManager authenticationManager;

    @Bean
    @Order(1)
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.cors().configurationSource(corsConfigurationSource);
        http.csrf().disable();
        http.formLogin().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.addFilter(customAuthenticationFilter);
        http.addFilterAfter(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user").permitAll()
                .antMatchers(HttpMethod.GET, "/api/role").permitAll()
//                .antMatchers(String.valueOf(List.of("/api/accident_type_count_report", "/api/accident_type_percentage",
//                        "/api/accident_type_percentage", "/api/get_card_details", "/api/validate_dashboard",
//                        "/api/get_all_accident", "/api/change_status"))).hasAnyAuthority("POLICE_USER", "INSURANCE_USER")
                .anyRequest().authenticated();

        return http.build();

    }
}
