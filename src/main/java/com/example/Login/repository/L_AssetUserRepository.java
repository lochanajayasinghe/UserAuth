// com.example.Login.repository.L_AssetUserRepository
package com.example.Login.repository;

import com.example.Login.dto.L_UserHistoryDto;
import com.example.Login.model.AssetUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface L_AssetUserRepository extends JpaRepository<AssetUser, Long> {
    List<AssetUser> findByUserName(String userName);
    List<AssetUser> findByAsset_AssetId(String assetId);
    List<AssetUser> findAllByOrderByUserNameAscStartDateDesc();


    // Add to L_AssetUserRepository
@Query("SELECT au.userName as userName, au.jobRole as jobRole, au.userDescription as userDescription, " +
       "a.assetId as assetId, a.name as assetName, l.departmentName as departmentName, " +
       "au.startDate as startDate, au.endDate as endDate " +
       "FROM AssetUser au JOIN au.asset a JOIN au.location l " +
       "WHERE au.userName = :userName")
List<L_UserHistoryDto> findUserHistoryDtoByUserName(String userName);


}

