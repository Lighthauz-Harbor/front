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
    private String id;
    private String email;
    private String password;
    private String name;
    private String bio;
    private String dob;
    private String profilePic;

    private List<Integer> idea = new ArrayList<>();

    public List<Integer> getIdea() {
        return idea;
    }

    public static List<User> users = new ArrayList<>();



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;

    public User(String email,String password, String name, String dob, String phone){
        setDob(dob);
        setEmail(email);
        setName(name);
        setPassword(password);
        setPhone(phone);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
