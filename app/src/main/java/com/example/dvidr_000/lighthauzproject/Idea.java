package com.example.dvidr_000.lighthauzproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by richentra on 11-Nov-16.
 */

public class Idea implements Parcelable{

    private String id;
    private String title;
    private String category;
    private String description;
    private String pic;
    private int  publicity;
    private String  background;
    private String  problem;
    private String  solution;
    private String  valueProposition;
    private String  customerSegment;
    private String  customerRelationship;
    private String  channel;
    private String  keyActivities;
    private String  keyResources;
    private String  keyPartner;
    private String  costStructure;
    private String  revenueStream;
    private String  strength;
    private String  weakness;
    private String  opportunities;
    private String  threat;
    private String  extraLink;
    private Long createDate;
    private Long lastEdited;

    private static List<Idea> ideas = new ArrayList<>();

    public Idea(String id, String title, String description, String pic, String category){
        this.id=id;
        this.title=title;
        this.description=description;
        this.pic=pic;
        this.category=category;
    }

    public Idea(String id, String title, String pic){
        this.id=id;
        this.title=title;
        this.pic=pic;
    }

    public Idea(
            String title,
            String category,
            String  description,
            Long createDate,
             int  publicity,
             String  background,
             String  problem,
             String  solution,
             String  valueProposition,
             String  customerSegment,
             String  customerRelationship,
             String  channel,
             String  keyActivities,
             String  keyResources,
             String  keyPartner,
             String  costStructure,
             String  revenueStream,
             String  strength,
             String  weakness,
             String  opportunities,
             String  threat,
            String extraLink,
             String pic){
        this.setTitle(title);
        this.setCategory(category);
        this.setBackground(background);
        this.setChannel(channel);
        this.setCostStructure(costStructure);
        this.setCustomerRelationship(customerRelationship);
        this.setCustomerSegment(customerSegment);
        this.setDescription(description);
        this.setKeyActivities(keyActivities);
        this.setKeyPartner(keyPartner);
        this.setKeyResources(keyResources);
        this.setOpportunities(opportunities);
        this.setProblem(problem);
        this.setPublicity(publicity);
        this.setWeakness(weakness);
        this.setValueProposition(valueProposition);
        this.setThreat(threat);
        this.setStrength(strength);
        this.setSolution(solution);
        this.setRevenueStream(revenueStream);
        this.createDate=createDate;
        this.extraLink=extraLink;
        this.pic=pic;
    }

    public static List<Idea> getIdeas() {
        return ideas;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getPic() {return pic;}

    public void setPic(String pic) {this.pic = pic;}

    public Long getCreateDate() {
        return createDate;
    }

    public Long getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Long lastEdited) {
        this.lastEdited = lastEdited;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getPublicity() {
        return publicity;
    }

    public void setPublicity(int publicity) {
        this.publicity = publicity;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getValueProposition() {
        return valueProposition;
    }

    public void setValueProposition(String valueProposition) {
        this.valueProposition = valueProposition;
    }

    public String getCustomerSegment() {
        return customerSegment;
    }

    public void setCustomerSegment(String customerSegment) {
        this.customerSegment = customerSegment;
    }

    public String getCustomerRelationship() {
        return customerRelationship;
    }

    public void setCustomerRelationship(String customerRelationship) {
        this.customerRelationship = customerRelationship;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getKeyActivities() {
        return keyActivities;
    }

    public void setKeyActivities(String keyActivities) {
        this.keyActivities = keyActivities;
    }

    public String getKeyResources() {
        return keyResources;
    }

    public void setKeyResources(String keyResources) {
        this.keyResources = keyResources;
    }

    public String getKeyPartner() {
        return keyPartner;
    }

    public void setKeyPartner(String keyPartner) {
        this.keyPartner = keyPartner;
    }

    public String getCostStructure() {
        return costStructure;
    }

    public void setCostStructure(String costStructure) {
        this.costStructure = costStructure;
    }

    public String getRevenueStream() {
        return revenueStream;
    }

    public void setRevenueStream(String revenueStream) {
        this.revenueStream = revenueStream;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    public String getOpportunities() {
        return opportunities;
    }

    public void setOpportunities(String opportuniities) {
        this.opportunities = opportuniities;
    }

    public String getThreat() {
        return threat;
    }

    public void setThreat(String threat) {
        this.threat = threat;
    }

    public String getExtraLink() {return extraLink;}

    public void setExtraLink(String extraLink) {this.extraLink = extraLink;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
