package com.example.daan;

import android.net.Uri;
import android.view.Display;

public class Model {
    String ItemName,category,description,location,contact,Uid;
    String url;

    Model()
    {

    }

    public Model(String itemName, String category, String description, String location, String contact,String Uid,String url) {
        ItemName = itemName;
        this.category = category;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.Uid=Uid;
       this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
