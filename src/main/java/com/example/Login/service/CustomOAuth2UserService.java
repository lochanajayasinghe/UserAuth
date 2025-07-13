package com.example.Login.service;

import com.example.Login.model.Role;
import com.example.Login.model.User;
import com.example.Login.repository.RoleRepository;
import com.example.Login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value; // Import for @Value
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

    // Inject configured admin emails from application.properties
    @Value("${app.security.admin-emails:}") // Default to empty string if not found
    private String adminEmailsString;

    // Inject configured director emails from application.properties
    @Value("${app.security.director-emails:}") // Default to empty string if not found
    private String directorEmailsString;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Helper method to get roles from a comma-separated string
    private Set<String> getEmailSet(String emailString) {
        if (emailString == null || emailString.trim().isEmpty()) {
            return Collections.emptySet();
        }
        return Arrays.stream(emailString.split(","))
                     .map(String::trim)
                     .map(String::toLowerCase) // Convert to lowercase for case-insensitive comparison
                     .collect(Collectors.toSet());
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

        String userEmailLowerCase = email.toLowerCase(); // Use lowercase for comparison

        // Check if user already exists by email
        User user = userRepository.findByEmail(userEmailLowerCase)
            .orElseGet(() -> {
                // If not, create a new user
                User newUser = new User();
                newUser.setEmail(userEmailLowerCase);
                newUser.setFullName(name);
                newUser.setUsername(generateUniqueUsername(userEmailLowerCase)); // Generate unique username
                newUser.setEnabled(true); // OAuth2 users are enabled by default as email is verified by provider
                newUser.setPassword("oauth2_user_no_password"); // Set a dummy password for non-traditional login

                // Determine role based on configured emails for new OAuth2 users
                Set<Role> assignedRoles = new HashSet<>();
                Set<String> adminEmails = getEmailSet(adminEmailsString);
                Set<String> directorEmails = getEmailSet(directorEmailsString);

                if (adminEmails.contains(userEmailLowerCase)) {
                    Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                            .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_ADMIN' not found."));
                    assignedRoles.add(adminRole);
                } else if (directorEmails.contains(userEmailLowerCase)) {
                    Role directorRole = roleRepository.findByName("ROLE_DIRECTOR")
                            .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_DIRECTOR' not found."));
                    assignedRoles.add(directorRole);
                } else {
                    // Default to ROLE_USER for all other emails
                    Role userRole = roleRepository.findByName("ROLE_USER")
                            .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_USER' not found."));
                    assignedRoles.add(userRole);
                }
                newUser.setRoles(assignedRoles); // Assign the determined role(s)

                // Save the new user to the database
                return userRepository.save(newUser);
            });

        // Convert user's roles to Spring Security GrantedAuthorities
        Set<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toSet());

        // Return a DefaultOAuth2User with collected information
        return new DefaultOAuth2User(
            authorities,
            attributes,
            "email" // "email" is used as the userNameAttributeName for Google
        );
    }

    // Corrected method to generate a unique username (from your previous code)
    private String generateUniqueUsername(String email) {
        String baseUsername = email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "");
        if (baseUsername.isEmpty()) {
            baseUsername = "user";
        }

        String username = baseUsername;
        int counter = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + "_" + counter;
            counter++;
        }
        return username;
    }
}
