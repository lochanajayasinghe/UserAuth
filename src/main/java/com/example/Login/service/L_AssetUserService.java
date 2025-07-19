// com.example.Login.service.L_AssetUserService
package com.example.Login.service;
import com.example.Login.dto.L_UserHistoryDto;

import com.example.Login.model.AssetUser;
import com.example.Login.repository.H_AssetRepository;
import com.example.Login.repository.L_AssetUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class L_AssetUserService {
    private final L_AssetUserRepository assetUserRepository;
    private final com.example.Login.repository.H_AssetRepository assetRepository;
    private final com.example.Login.repository.M_LocationRepository locationRepository;
    private final com.example.Login.repository.M_RoomRepository roomRepository;

    public L_AssetUserService(
        L_AssetUserRepository assetUserRepository,
        com.example.Login.repository.H_AssetRepository assetRepository,
        com.example.Login.repository.M_LocationRepository locationRepository,
        com.example.Login.repository.M_RoomRepository roomRepository
    ) {
        this.assetUserRepository = assetUserRepository;
        this.assetRepository = assetRepository;
        this.locationRepository = locationRepository;
        this.roomRepository = roomRepository;
    }

    // Asset auto-suggest
    public List<com.example.Login.model.Asset> suggestAssets(String query) {
        return assetRepository.findByAssetIdContainingIgnoreCaseOrNameContainingIgnoreCase(query, query);
    }

    // User auto-suggest
    public List<com.example.Login.dto.UserSuggestDto> suggestUsers(String query) {
        List<AssetUser> users = assetUserRepository.findByUserNameContainingIgnoreCase(query);
        List<com.example.Login.dto.UserSuggestDto> dtos = new java.util.ArrayList<>();
        for (AssetUser user : users) {
            com.example.Login.dto.UserSuggestDto dto = new com.example.Login.dto.UserSuggestDto();
            dto.setUserName(user.getUserName());
            dto.setJobRole(user.getJobRole());
            dtos.add(dto);
        }
        return dtos;
    }

    // Add new user history
    @Transactional
    public boolean addAssetUserHistory(com.example.Login.dto.AddUserHistoryDto dto) {
        try {
            // Find or create Asset
            com.example.Login.model.Asset asset = assetRepository.findById(dto.getAssetId()).orElse(null);
            if (asset == null) {
                asset = new com.example.Login.model.Asset();
                asset.setAssetId(dto.getAssetId());
                asset.setName(dto.getAssetName());
                asset.setBrand(dto.getAssetBrand());
                asset.setModel(dto.getAssetModel());
                assetRepository.save(asset);
            }

            // Find or create Location by department name
            com.example.Login.model.Location location = null;
            List<com.example.Login.model.Location> locations = locationRepository.findAll();
            for (com.example.Login.model.Location loc : locations) {
                if (loc.getDepartmentName() != null && loc.getDepartmentName().equalsIgnoreCase(dto.getDepartmentName())) {
                    location = loc;
                    break;
                }
            }
            if (location == null) {
                location = new com.example.Login.model.Location();
                location.setDepartmentName(dto.getDepartmentName());
                location = locationRepository.save(location);
            }

            // Find or create Room by room name
            com.example.Login.model.Room room = roomRepository.findByRoomName(dto.getRoomName()).orElse(null);
            if (room == null) {
                room = new com.example.Login.model.Room();
                room.setRoomName(dto.getRoomName());
                room.setLocation(location);
                room = roomRepository.save(room);
            }

            // Save AssetUser with references
            AssetUser assetUser = new AssetUser();
            assetUser.setAsset(asset);
            assetUser.setUserName(dto.getUserName());
            assetUser.setJobRole(dto.getJobRole());
            assetUser.setUserDescription(dto.getUserDescription());
            assetUser.setLocation(location);
            assetUser.setStartDate(dto.getStartDate());
            assetUser.setEndDate(dto.getEndDate());
            assetUserRepository.save(assetUser);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<L_UserHistoryDto> getAllUserHistoryDtos() {
        return assetUserRepository.findAllUserHistoryDtos();
    }

    public List<AssetUser> getUserHistoryByUserName(String userName) {
        return assetUserRepository.findByUserName(userName);
    }

    public List<AssetUser> getAssetHistory(String assetId) {
        return assetUserRepository.findByAsset_AssetId(assetId);
    }

    public AssetUser saveAssetUser(AssetUser assetUser) {
        return assetUserRepository.save(assetUser);
    }

    public AssetUser getUserHistoryById(Long id) {
        return assetUserRepository.findById(id).orElse(null);
    }

}

