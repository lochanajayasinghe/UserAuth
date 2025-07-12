package com.example.Login.service;

import com.example.Login.model.Role;
import com.example.Login.model.User;
import com.example.Login.repository.RoleRepository;
import com.example.Login.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        // Check if user already exists by email
        User user = userRepository.findByEmail(email)
            .orElseGet(() -> {
                // If not, create a new user
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setFullName(name);
                // Generate a unique username from email
                // Added a small sanitization for the base username
                newUser.setUsername(generateUniqueUsername(email));
                newUser.setEnabled(true); // OAuth2 users are enabled by default as email is verified by Google
                newUser.setPassword("oauth2_user_no_password"); // Set a dummy password for non-traditional login

                // Assign default role (e.g., ROLE_USER)
                Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_USER' not found. Please ensure this role exists in your database."));
                newUser.setRoles(Collections.singleton(userRole));

                // Save the new user to the database
                return userRepository.save(newUser);
            });

        // Convert user's roles to Spring Security GrantedAuthorities
        Set<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toSet());

        // Return a DefaultOAuth2User with collected information
        // "email" is used as the userNameAttributeName for Google
        return new DefaultOAuth2User(
            authorities,
            attributes,
            "email"
        );
    }

    // Corrected method to generate a unique username
    private String generateUniqueUsername(String email) {
        // Sanitize base username: remove special characters, keep only alphanumeric
        String baseUsername = email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "");
        if (baseUsername.isEmpty()) {
            baseUsername = "user"; // Fallback if email prefix is empty after sanitization
        }

        String username = baseUsername;
        int counter = 1;

        // Check for username existence using existsByUsername
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + "_" + counter;
            counter++;
        }
        return username;
    }
}