package com.secondary.aiche;

public class Module{
    private String image;
    private String detail;
    private String time;
    private String title;
    private String link;
    private String timestamp;


    public Module(String timestamp,String image, String detail, String time, String title, String link) {
        this.image = image;
        this.detail = detail;
        this.time = time;
        this.title = title;
        this.link = link;
        this.timestamp = timestamp;
    }

    public Module() {
    }


    public String getTimeStamp(){return timestamp;}

    public String getImage() {
        return image;
    }

    public String getDetail() {
        return detail;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}