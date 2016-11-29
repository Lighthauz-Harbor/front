package com.example.dvidr_000.lighthauzproject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by richentra on 07-Nov-16.
 */

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private String occupation;
    private String bio;
    private String dob;
    private String profPic;
    private int profilePic;
    private String interest;
    private Long createdAt;

    private  List<Integer> idea = new ArrayList<>();

    public  List<Integer> getIdea() {
        return idea;
    }

    private static List<User> users = new ArrayList<>();

    public static List<User> getUsers() {
        return users;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;

    public String getProfPic() {return profPic;}

    public void setProfPic(String profPic) {this.profPic = profPic;}

    public Long getCreatedAt() {return createdAt;}

    public void setCreatedAt(Long createdAt) {this.createdAt = createdAt;}

    public User(String email, String password, String name, String dob, String phone){
        setDob(dob);
        setEmail(email);
        setName(name);
        setPassword(password);
        setPhone(phone);

        setId(users.size());

    }
    public User(String email, String name, Long createdAt, String profPic){
        this.email=email;
        this.name=name;
        this.createdAt=createdAt;
        this.profPic=profPic;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }
}
