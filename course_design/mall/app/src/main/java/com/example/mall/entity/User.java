package com.example.mall.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String logo;
    private String gender;
    private String tel;
    private String addr;
    private String address;
    private String postcode;
    private String headpic;

    public User() {
    }

    public User(String username, String password, String logo, String gender, String tel, String addr, String address, String postcode, String headpic) {
        this.username = username;
        this.password = password;
        this.logo = logo;
        this.gender = gender;
        this.tel = tel;
        this.addr = addr;
        this.address = address;
        this.postcode = postcode;
        this.headpic = headpic;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", logo='" + logo + '\'' +
                ", gender='" + gender + '\'' +
                ", tel='" + tel + '\'' +
                ", addr='" + addr + '\'' +
                ", address='" + address + '\'' +
                ", postcode='" + postcode + '\'' +
                ", headpic='" + headpic + '\'' +
                '}';
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
