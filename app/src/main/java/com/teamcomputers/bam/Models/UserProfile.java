package com.teamcomputers.bam.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {
    private String userID;
    private String memberName;
    private String gender;
    private String department;
    private String designation;
    private String MaritalStatus;
    private String branch;
    private String branchCode;
    private String SBU;
    private String SBUHeadName;
    private String SBUHeadId;
    private String reportingHeadId;
    private String dateOfJoining;
    private String dateOfBirth;
    private String commAddress;
    private String contactNo;
    private String companeyEmail;
    private String personalEmail;
    private String profilePictureUrl;
    private String reportingHeadName;
    private String authToken;
    private String IsHead;
    private String UserImage;
    private String IsPasswordUpdated;

    public UserProfile() {
    }

    public UserProfile(String userID, String memberName, String gender, String department, String designation, String branch, String branchCode, String SBU, String SBUHeadName, String SBUHeadId, String reportingHeadId, String dateOfJoining, String dateOfBirth, String commAddress, String contactNo, String companeyEmail, String personalEmail, String profilePictureUrl, String reportingHeadName, String authToken, String isHead, String userImage, String isPasswordUpdated) {
        this.userID = userID;
        this.memberName = memberName;
        this.gender = gender;
        this.department = department;
        this.designation = designation;
        this.branch = branch;
        this.branchCode = branchCode;
        this.SBU = SBU;
        this.SBUHeadName = SBUHeadName;
        this.SBUHeadId = SBUHeadId;
        this.reportingHeadId = reportingHeadId;
        this.dateOfJoining = dateOfJoining;
        this.dateOfBirth = dateOfBirth;
        this.commAddress = commAddress;
        this.contactNo = contactNo;
        this.companeyEmail = companeyEmail;
        this.personalEmail = personalEmail;
        this.profilePictureUrl = profilePictureUrl;
        this.reportingHeadName = reportingHeadName;
        this.authToken = authToken;
        IsHead = isHead;
        UserImage = userImage;
        IsPasswordUpdated = isPasswordUpdated;
    }

    public UserProfile(String userID, String memberName, String gender, String department, String designation, String maritalStatus, String branch, String branchCode, String SBU, String SBUHeadName, String SBUHeadId, String reportingHeadId, String dateOfJoining, String dateOfBirth, String commAddress, String contactNo, String companeyEmail, String personalEmail, String profilePictureUrl, String reportingHeadName, String authToken, String isHead, String userImage, String isPasswordUpdated) {
        this.userID = userID;
        this.memberName = memberName;
        this.gender = gender;
        this.department = department;
        this.designation = designation;
        this.MaritalStatus = maritalStatus;
        this.branch = branch;
        this.branchCode = branchCode;
        this.SBU = SBU;
        this.SBUHeadName = SBUHeadName;
        this.SBUHeadId = SBUHeadId;
        this.reportingHeadId = reportingHeadId;
        this.dateOfJoining = dateOfJoining;
        this.dateOfBirth = dateOfBirth;
        this.commAddress = commAddress;
        this.contactNo = contactNo;
        this.companeyEmail = companeyEmail;
        this.personalEmail = personalEmail;
        this.profilePictureUrl = profilePictureUrl;
        this.reportingHeadName = reportingHeadName;
        this.authToken = authToken;
        this.IsHead = isHead;
        this.UserImage = userImage;
        this.IsPasswordUpdated = isPasswordUpdated;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        MaritalStatus = maritalStatus;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getSBU() {
        return SBU;
    }

    public void setSBU(String SBU) {
        this.SBU = SBU;
    }

    public String getSBUHeadName() {
        return SBUHeadName;
    }

    public void setSBUHeadName(String SBUHeadName) {
        this.SBUHeadName = SBUHeadName;
    }

    public String getSBUHeadId() {
        return SBUHeadId;
    }

    public void setSBUHeadId(String SBUHeadId) {
        this.SBUHeadId = SBUHeadId;
    }

    public String getReportingHeadId() {
        return reportingHeadId;
    }

    public void setReportingHeadId(String reportingHeadId) {
        this.reportingHeadId = reportingHeadId;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCommAddress() {
        return commAddress;
    }

    public void setCommAddress(String commAddress) {
        this.commAddress = commAddress;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCompaneyEmail() {
        return companeyEmail;
    }

    public void setCompaneyEmail(String companeyEmail) {
        this.companeyEmail = companeyEmail;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getReportingHeadName() {
        return reportingHeadName;
    }

    public void setReportingHeadName(String reportingHeadName) {
        this.reportingHeadName = reportingHeadName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getIsHead() {
        return IsHead;
    }

    public void setIsHead(String isHead) {
        IsHead = isHead;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getIsPasswordUpdated() {
        return IsPasswordUpdated;
    }

    public void setIsPasswordUpdated(String isPasswordUpdated) {
        IsPasswordUpdated = isPasswordUpdated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userID);
        dest.writeString(this.memberName);
        dest.writeString(this.gender);
        dest.writeString(this.department);
        dest.writeString(this.designation);
        dest.writeString(this.MaritalStatus);
        dest.writeString(this.branch);
        dest.writeString(this.branchCode);
        dest.writeString(this.SBU);
        dest.writeString(this.SBUHeadName);
        dest.writeString(this.SBUHeadId);
        dest.writeString(this.reportingHeadId);
        dest.writeString(this.dateOfJoining);
        dest.writeString(this.dateOfBirth);
        dest.writeString(this.commAddress);
        dest.writeString(this.contactNo);
        dest.writeString(this.companeyEmail);
        dest.writeString(this.personalEmail);
        dest.writeString(this.profilePictureUrl);
        dest.writeString(this.reportingHeadName);
        dest.writeString(this.authToken);
        dest.writeString(this.IsHead);
        dest.writeString(this.UserImage);
        dest.writeString(this.IsPasswordUpdated);
    }

    protected UserProfile(Parcel in) {
        this.userID = in.readString();
        this.memberName = in.readString();
        this.gender = in.readString();
        this.department = in.readString();
        this.designation = in.readString();
        this.MaritalStatus = in.readString();
        this.branch = in.readString();
        this.branchCode = in.readString();
        this.SBU = in.readString();
        this.SBUHeadName = in.readString();
        this.SBUHeadId = in.readString();
        this.reportingHeadId = in.readString();
        this.dateOfJoining = in.readString();
        this.dateOfBirth = in.readString();
        this.commAddress = in.readString();
        this.contactNo = in.readString();
        this.companeyEmail = in.readString();
        this.personalEmail = in.readString();
        this.profilePictureUrl = in.readString();
        this.reportingHeadName = in.readString();
        this.authToken = in.readString();
        this.IsHead = in.readString();
        this.UserImage = in.readString();
        this.IsPasswordUpdated = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel source) {
            return new UserProfile(source);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };
}
