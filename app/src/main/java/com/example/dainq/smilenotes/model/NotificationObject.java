package com.example.dainq.smilenotes.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class NotificationObject extends RealmObject {
    @PrimaryKey
    private int id;
    private int idcustomer;
    private int idproduct;
    private int idmeeting;

    private Date date;
    private Date datevalue;
    private String content;
    private int type;
    private boolean isread;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdcustomer() {
        return idcustomer;
    }

    public void setIdcustomer(int idcustomer) {
        this.idcustomer = idcustomer;
    }

    public int getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(int idproduct) {
        this.idproduct = idproduct;
    }

    public int getIdmeeting() {
        return idmeeting;
    }

    public void setIdmeeting(int idmeeting) {
        this.idmeeting = idmeeting;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDatevalue() {
        return datevalue;
    }

    public void setDatevalue(Date datevalue) {
        this.datevalue = datevalue;
    }

    public boolean isread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
