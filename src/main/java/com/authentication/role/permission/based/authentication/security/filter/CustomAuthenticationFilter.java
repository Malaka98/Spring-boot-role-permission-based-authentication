package com.authentication.role.permission.based.authentication.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.authentication.role.permission.based.authentication.dto.LoginDTO;
import com.authentication.role.permission.based.authentication.exception.UnknownException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            @Valid LoginDTO loginDTO = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            log.error("Runtime Error ======> " + e.getMessage());
            throw new UnknownException(e.getMessage());
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        UserDetails user = (UserDetails) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 40 * 60 * 1000))
                .withIssuer(request.getRequestURI())
                .withClaim("roles", roles)
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURI())
                .sign(algorithm);

        Map<String, List<String>> responseBody = new HashMap<>();
        Cookie jwtAccessTokenCookie = new Cookie("access_token", access_token);
        Cookie jwtRefreshTokenCookie = new Cookie("refresh_token", refresh_token);

        responseBody.put("user_role", roles);
        jwtAccessTokenCookie.setMaxAge(86400);
////        jwtAccessTokenCookie.setSecure(true);
        jwtAccessTokenCookie.setHttpOnly(true);
        jwtAccessTokenCookie.setPath("/");
//
        jwtRefreshTokenCookie.setMaxAge(86400);
////        jwtRefreshTokenCookie.setSecure(true);
        jwtRefreshTokenCookie.setHttpOnly(true);
        jwtRefreshTokenCookie.setPath("/");
        response.setContentType(APPLICATION_JSON_VALUE);
        response.addCookie(jwtAccessTokenCookie);
        response.addCookie(jwtRefreshTokenCookie);
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.error(failed.getMessage());
        Map<String, String> resBody = new HashMap<>();
        resBody.put("Unauthorized", failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), resBody);
    }
}
