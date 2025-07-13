package com.example.Login.config;

import com.example.Login.service.CustomOAuth2UserService;
import com.example.Login.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Added for method-level security
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler; // Import for custom success handler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enable method-level security for @PreAuthorize annotations in controllers
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;

    // Inject the custom authentication success handler
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity, consider enabling for production
            .authorizeHttpRequests(auth -> auth
                // Publicly accessible pages (no authentication required)
                .requestMatchers(
                    "/", "/register", "/login", "/verify",
                    "/reset-password/**", "/forgot-password",
                    "/css/**", "/js/**", "/images/**",
                    "/webjars/**", "/check-username", "/access-denied" // Ensure access-denied is public
                ).permitAll()
                // Role-specific access rules:
                .requestMatchers("/admin/**").hasRole("ADMIN") // Only ADMIN can access paths under /admin/
                .requestMatchers("/director/**").hasRole("DIRECTOR") // Only DIRECTOR can access paths under /director/
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "DIRECTOR") // User pages accessible by all three roles
                // Any other request requires authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // Your custom login page
                // Use the custom success handler for form-based login
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/login?error=true") // Redirect on login failure
                .permitAll() // Allow everyone to access the login form
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL to trigger logout
                .logoutSuccessUrl("/login?logout=true") // Redirect after successful logout
                .invalidateHttpSession(true) // Invalidate session
                .deleteCookies("JSESSIONID") // Delete session cookie
                .permitAll() // Allow everyone to logout
            )
            .oauth2Login(oauth -> oauth
                .loginPage("/login") // Your custom login page for OAuth2
                // Use the custom success handler for OAuth2 login as well
                .successHandler(customAuthenticationSuccessHandler)
                .userInfoEndpoint(user -> user.userService(oAuth2UserService)) // Custom OAuth2 user service
                .failureUrl("/login?error=oauth") // Redirect on OAuth2 login failure
                .authorizationEndpoint(auth -> auth
                    .baseUri("/oauth2/authorization") // Base URI for OAuth2 authorization
                )
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/access-denied") // Page to show when access is denied
            )
            .authenticationProvider(authenticationProvider()); // Set custom authentication provider

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Use BCryptPasswordEncoder with strength 12
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Set your custom UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // Set the password encoder
        authProvider.setHideUserNotFoundExceptions(false); // Show specific error for user not found
        return authProvider;
    }
}
