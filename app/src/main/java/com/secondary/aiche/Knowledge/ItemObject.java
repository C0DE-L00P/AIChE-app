package com.secondary.aiche.Knowledge;


//Knowledge Item

public class ItemObject
{
    private int courseName;
    private String courseImg;

    public ItemObject(int name, String image)
    {
        this.courseName = name;
        this.courseImg = image;
    }

    public int getName()
    {
        return courseName;
    }

    public void setName(int name)
    {
        this.courseName = name;
    }

    public String getImage()
    {
        return courseImg;
    }

    public void setImage(String image)
    {
        this.courseImg = image;
    }
}
