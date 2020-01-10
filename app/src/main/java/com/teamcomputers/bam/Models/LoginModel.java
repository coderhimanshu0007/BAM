package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("MemberName")
    @Expose
    private String memberName;
    @SerializedName("Department")
    @Expose
    private String department;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("Branch")
    @Expose
    private String branch;
    @SerializedName("SBU")
    @Expose
    private String sBU;
    @SerializedName("SBUHeadName")
    @Expose
    private String sBUHeadName;
    @SerializedName("SBUHeadId")
    @Expose
    private String sBUHeadId;
    @SerializedName("ReportingHeadId")
    @Expose
    private String reportingHeadId;
    @SerializedName("DateOfJoining")
    @Expose
    private String dateOfJoining;
    @SerializedName("DateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("CommAddress")
    @Expose
    private String commAddress;
    @SerializedName("ContactNo")
    @Expose
    private String contactNo;
    @SerializedName("CompaneyEmail")
    @Expose
    private String companeyEmail;
    @SerializedName("PersonalEmail")
    @Expose
    private String personalEmail;
    @SerializedName("Bank_Name")
    @Expose
    private String bankName;
    @SerializedName("Account_No")
    @Expose
    private String accountNo;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("SBUCode")
    @Expose
    private String sBUCode;
    @SerializedName("Department_Code")
    @Expose
    private String departmentCode;
    @SerializedName("Mobile_Phone_No_")
    @Expose
    private String mobilePhoneNo;
    @SerializedName("Location_Code")
    @Expose
    private String locationCode;
    @SerializedName("First_Name")
    @Expose
    private String firstName;
    @SerializedName("Qualification")
    @Expose
    private String qualification;
    @SerializedName("UserTmcPassword")
    @Expose
    private Object userTmcPassword;
    @SerializedName("UserNewPassword")
    @Expose
    private Object userNewPassword;
    @SerializedName("AuthToken")
    @Expose
    private Object authToken;
    @SerializedName("EmpDesignation")
    @Expose
    private String empDesignation;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("ConfirmationDate")
    @Expose
    private String confirmationDate;
    @SerializedName("MaritalStatus")
    @Expose
    private String maritalStatus;
    @SerializedName("ESIC")
    @Expose
    private String eSIC;
    @SerializedName("IsConfirmed")
    @Expose
    private Object isConfirmed;
    @SerializedName("ProbationStatus")
    @Expose
    private Object probationStatus;

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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSBU() {
        return sBU;
    }

    public void setSBU(String sBU) {
        this.sBU = sBU;
    }

    public String getSBUHeadName() {
        return sBUHeadName;
    }

    public void setSBUHeadName(String sBUHeadName) {
        this.sBUHeadName = sBUHeadName;
    }

    public String getSBUHeadId() {
        return sBUHeadId;
    }

    public void setSBUHeadId(String sBUHeadId) {
        this.sBUHeadId = sBUHeadId;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getSBUCode() {
        return sBUCode;
    }

    public void setSBUCode(String sBUCode) {
        this.sBUCode = sBUCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getMobilePhoneNo() {
        return mobilePhoneNo;
    }

    public void setMobilePhoneNo(String mobilePhoneNo) {
        this.mobilePhoneNo = mobilePhoneNo;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Object getUserTmcPassword() {
        return userTmcPassword;
    }

    public void setUserTmcPassword(Object userTmcPassword) {
        this.userTmcPassword = userTmcPassword;
    }

    public Object getUserNewPassword() {
        return userNewPassword;
    }

    public void setUserNewPassword(Object userNewPassword) {
        this.userNewPassword = userNewPassword;
    }

    public Object getAuthToken() {
        return authToken;
    }

    public void setAuthToken(Object authToken) {
        this.authToken = authToken;
    }

    public String getEmpDesignation() {
        return empDesignation;
    }

    public void setEmpDesignation(String empDesignation) {
        this.empDesignation = empDesignation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(String confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getESIC() {
        return eSIC;
    }

    public void setESIC(String eSIC) {
        this.eSIC = eSIC;
    }

    public Object getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Object isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public Object getProbationStatus() {
        return probationStatus;
    }

    public void setProbationStatus(Object probationStatus) {
        this.probationStatus = probationStatus;
    }


}
