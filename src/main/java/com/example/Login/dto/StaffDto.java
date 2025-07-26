package com.example.Login.dto;

public class StaffDto {
    private String userId;
    private String userName;
    private String jobRole;
    private String userDescription;

    public StaffDto() {}

    public StaffDto(String userId, String userName, String jobRole, String userDescription) {
        this.userId = userId;
        this.userName = userName;
        this.jobRole = jobRole;
        this.userDescription = userDescription;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getUserDescription() { return userDescription; }
    public void setUserDescription(String userDescription) { this.userDescription = userDescription; }
}
