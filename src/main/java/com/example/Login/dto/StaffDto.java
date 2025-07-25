package com.example.Login.dto;

public class StaffDto {
    private String userName;
    private String jobRole;
    private String userDescription;

    public StaffDto() {}
    public StaffDto(String userName, String jobRole, String userDescription) {
        this.userName = userName;
        this.jobRole = jobRole;
        this.userDescription = userDescription;
    }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }
    public String getUserDescription() { return userDescription; }
    public void setUserDescription(String userDescription) { this.userDescription = userDescription; }
}
