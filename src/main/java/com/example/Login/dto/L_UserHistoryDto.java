// com.example.Login.dto.L_UserHistoryDto
package com.example.Login.dto;

import java.util.Date;

public interface L_UserHistoryDto {
    String getUserName();
    String getJobRole();
    String getUserDescription();
    String getAssetId();
    String getAssetName();
    String getDepartmentName();
    Date getStartDate();
    Date getEndDate();
}

