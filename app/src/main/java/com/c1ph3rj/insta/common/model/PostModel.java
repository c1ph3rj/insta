package com.c1ph3rj.insta.common.model;

import java.util.ArrayList;

public class PostModel {
    private String postUuid;
    private String fileName;
    private String uploadTime;
    private String fileType;
    private ArrayList<String> tags;
    private String Location;
    private String description;
    private ArrayList<FriendsModel> listOfPeopleLiked;
    private ArrayList<CommentsModel> listOfComments;

    public ArrayList<CommentsModel> getListOfComments() {
        return listOfComments;
    }

    public void setListOfComments(ArrayList<CommentsModel> listOfComments) {
        this.listOfComments = listOfComments;
    }

    public String getPostUuid() {
        return postUuid;
    }

    public void setPostUuid(String postUuid) {
        this.postUuid = postUuid;
    }

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

    public ArrayList<FriendsModel> getListOfPeopleLiked() {
        return listOfPeopleLiked;
    }

    public void setListOfPeopleLiked(ArrayList<FriendsModel> listOfPeopleLiked) {
        this.listOfPeopleLiked = listOfPeopleLiked;
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
