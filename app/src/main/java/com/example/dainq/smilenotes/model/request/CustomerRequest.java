package com.example.dainq.smilenotes.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CustomerRequest {
    @SerializedName("version")
    @Expose
    private int version;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("customerId")
    @Expose
    private String customerId;

    @SerializedName("adaCode")
    @Expose
    private String adaCode;

    @SerializedName("level")
    @Expose
    private int level;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("dateOfBirth")
    @Expose
    private Date dateOfBirth;

    @SerializedName("createDate")
    @Expose
    private Date createDate;

    @SerializedName("job")
    @Expose
    private String job;

    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("problemType")
    @Expose
    private int problemType;

    @SerializedName("solution")
    @Expose
    private String solution;

    @SerializedName("suggestProduct")
    @Expose
    private String suggestProduct;

    @SerializedName("gender")
    @Expose
    private int gender;

    @SerializedName("phone")
    @Expose
    private String phone;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAdaCode() {
        return adaCode;
    }

    public void setAdaCode(String adaCode) {
        this.adaCode = adaCode;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getProblemType() {
        return problemType;
    }

    public void setProblemType(int problemType) {
        this.problemType = problemType;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSuggestProduct() {
        return suggestProduct;
    }

    public void setSuggestProduct(String suggestProduct) {
        this.suggestProduct = suggestProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
