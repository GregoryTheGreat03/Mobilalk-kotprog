package com.application.yogaandmeditation;

public class OnlineSession {
    private String id;
    private String creatorName;
    private String creatorId;
    private String title;
    private String description;
    private String forGender;
    private String price;
    private int imageResource;

    public OnlineSession() {
    }

    public OnlineSession(String creatorName, String creatorId, String title, String description, String forGender, String price, int imageResource) {
        this.creatorName = creatorName;
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.forGender = forGender;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getForGender() {
        return forGender;
    }

    public void setForGender(String forGender) {
        this.forGender = forGender;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
