package com.example.dainq.smilenotes.model.request.customer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchRequest {
    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("keyword")
    @Expose
    private String keyword;

    @SerializedName("fromeDate")
    @Expose
    private String fromDate;

    @SerializedName("toDate")
    @Expose
    private String toDate;

    @SerializedName("pageNumber")
    @Expose
    private int pageNumber;

    @SerializedName("pageSize")
    @Expose
    private int pageSize;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
