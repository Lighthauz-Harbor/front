package com.example.dvidr_000.lighthauzproject;

/**
 * Created by richentra on 31-Dec-16.
 */

public class Comment{
    private String name;
    private String id;
    private String profilePic;
    private String text;
    private Long timestamp;

    Comment(String id, String name, String profilePic, String text, Long timestamp){
        this.id=id;
        this.name=name;
        this.profilePic=profilePic;
        this.text=text;
        this.timestamp=timestamp;
    }

    public String getName() {return name;}
    public String getId() {return id;}
    public String getProfilePic() {return profilePic;}
    public String getText() {return text;}
    public Long getTimestamp() {return timestamp;}
}