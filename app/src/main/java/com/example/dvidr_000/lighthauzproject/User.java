package com.example.dvidr_000.lighthauzproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by richentra on 07-Nov-16.
 */

public class User implements Parcelable{
    private String id;
    private String email;
    private String name;
    private String bio;
    private String dob;
    private String profPic;
    private Long createdAt;

    public String getProfPic() {return profPic;}

    public void setProfPic(String profPic) {this.profPic = profPic;}

    public Long getCreatedAt() {return createdAt;}

    public void setCreatedAt(Long createdAt) {this.createdAt = createdAt;}

    public User(String id, String email, String name, Long createdAt, String profPic){
        this.id=id;
        this.email=email;
        this.name=name;
        this.createdAt=createdAt;
        this.profPic=profPic;
    }

    public User(String id, String name, String profPic){
        this.id=id;
        this.name=name;
        this.profPic=profPic;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
