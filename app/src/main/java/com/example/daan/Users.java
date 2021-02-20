package com.example.daan;

public class Users {
    String Uid,name,email,phoneNo;
    int coin;

    Users()
    {

    }

    public Users(String name, String email, String phoneNo,int coin) {

        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.coin=coin;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
