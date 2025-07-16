package com.example.Login.config;

import com.example.Login.service.CustomOAuth2UserService;
import com.example.Login.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Public access
                .requestMatchers(
                    "/", "/register", "/login", "/verify",
                    "/reset-password/**", "/forgot-password",
                    "/css/**", "/js/**", "/images/**",
                    "/webjars/**", "/check-username", "/access-denied"
                ).permitAll()

                // Specific access
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/director/**").hasRole("DIRECTOR")
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "DIRECTOR")

                // ✅ Give access to /user-history endpoint
                .requestMatchers("/user-history").hasAnyRole("USER", "ADMIN", "DIRECTOR")

                // Any other request must be authenticated
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .userInfoEndpoint(user -> user.userService(oAuth2UserService))
                .failureUrl("/login?error=oauth")
                .authorizationEndpoint(auth -> auth
                    .baseUri("/oauth2/authorization")
                )
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/access-denied")
            )
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }
}
