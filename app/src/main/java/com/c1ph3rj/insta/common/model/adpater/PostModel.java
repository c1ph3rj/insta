package com.c1ph3rj.insta.common.model.adpater;

import com.c1ph3rj.insta.common.model.UserListModel;

import java.util.ArrayList;

public class PostModel {
    private String fileName;
    private String uploadTime;
    private String fileType;
    private ArrayList<String> tags;
    private String Location;
    private String description;
    private ArrayList<UserListModel> listOfPeople;

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<UserListModel> getListOfPeople() {
        return listOfPeople;
    }

    public void setListOfPeople(ArrayList<UserListModel> listOfPeople) {
        this.listOfPeople = listOfPeople;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
