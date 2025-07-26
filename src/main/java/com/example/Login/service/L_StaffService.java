package com.example.Login.service;

import com.example.Login.dto.StaffDto;
import com.example.Login.model.AssetUser;
import com.example.Login.repository.L_StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class L_StaffService {
    private final L_StaffRepository staffRepository;

    public L_StaffService(L_StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public List<StaffDto> getAllStaff() {
        return staffRepository.findAll().stream()
            .map(user -> new StaffDto(
                user.getUserId(),
                user.getUserName(),
                user.getJobRole(),
                user.getUserDescription()
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public boolean addStaff(StaffDto dto) {
        try {
            // Check if userId already exists
            boolean exists = staffRepository.existsByUserId(dto.getUserId());
            if (exists) {
                return false;
            }
            AssetUser user = new AssetUser();
            user.setUserId(dto.getUserId());
            user.setUserName(dto.getUserName());
            user.setJobRole(dto.getJobRole());
            user.setUserDescription(dto.getUserDescription());
            staffRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
