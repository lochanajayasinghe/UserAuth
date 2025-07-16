package com.example.Login.config; // You can place this in 'config' or a new 'handler' package

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Get the roles (authorities) of the authenticated user
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/login?error=true"; // Default fallback URL if no role matches

        // Check for specific roles and redirect accordingly.
        // Order matters here: check for higher-privileged roles first if there's overlap.
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            redirectUrl = "/admin/home"; // Redirect to admin dashboard
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_DIRECTOR"))) {
            redirectUrl = "/director/home"; // Redirect to director dashboard
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            redirectUrl = "/user/home"; // Redirect to user dashboard
        }

        // Perform the redirection
        response.sendRedirect(redirectUrl);
    }
}

