package com.example.dainq.smilenotes.model.response.customer;

import com.example.dainq.smilenotes.model.request.customer.CustomerRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchData {
    @SerializedName("pageNumber")
    @Expose
    private int pageNumber;

    @SerializedName("pageSize")
    @Expose
    private int pageSize;

    @SerializedName("pageItems")
    @Expose
    private List<CustomerRequest> pageItems;

    @SerializedName("totalItems")
    @Expose
    private int totalItems;

    @SerializedName("pagesAvailable")
    @Expose
    private int pagesAvailable;

    @SerializedName("time")
    @Expose
    private int time;

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

    public List<CustomerRequest> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<CustomerRequest> pageItems) {
        this.pageItems = pageItems;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getPagesAvailable() {
        return pagesAvailable;
    }

    public void setPagesAvailable(int pagesAvailable) {
        this.pagesAvailable = pagesAvailable;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
