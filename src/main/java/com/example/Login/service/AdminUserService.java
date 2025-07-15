package com.example.Login.service;

import com.example.Login.dto.UserDto;
import com.example.Login.model.User;
import com.example.Login.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserDto convertToDto(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                roleNames,
                user.isEnabled()
        );
    }
}