package com.example.dvidr_000.lighthauzproject;

/**
 * Created by richentra on 31-Dec-16.
 */

public class News {
    private String userId;
    private String ideaId;
    private String type;
    private String name;
    private String title;
    private String category;
    private String description;
    private String pic;
    private String profPic;
    private Long time;

    public News(String type, String userId, String ideaId, String name, String profPic, String title, String category, String description, String pic, Long time ){
        this.userId=userId;
        this.ideaId=ideaId;
        this.name=name;
        this.title=title;
        this.category=category;
        this.description=description;
        this.pic=pic;
        this.profPic=profPic;
        this.time=time;
        this.type=type;
    }

    public String getUserId() {
        return userId;
    }

    public String getIdeaId() {
        return ideaId;
    }

    public String getType() {
        return type;
    }

    public String getName() {return name;}

    public String getTitle() {return title;}

    public String getCategory() {return category;}

    public String getDescription() {return description;}

    public String getPic() {return pic;}

    public String getProfPic() {return profPic;}

    public Long getTime() {return time;}
}