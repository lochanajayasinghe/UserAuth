package com.example.Login.dto;

import java.util.Date;

public class AddUserHistoryDto {
    private String assetId;
    private String assetName;
    private String assetBrand;
    private String assetModel;
    private String userName;
    private String jobRole;
    private String userDescription;
    private String departmentName;
    private String roomName;
    private Date startDate;
    private Date endDate;

    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }
    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }
    public String getAssetBrand() { return assetBrand; }
    public void setAssetBrand(String assetBrand) { this.assetBrand = assetBrand; }
    public String getAssetModel() { return assetModel; }
    public void setAssetModel(String assetModel) { this.assetModel = assetModel; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }
    public String getUserDescription() { return userDescription; }
    public void setUserDescription(String userDescription) { this.userDescription = userDescription; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}
