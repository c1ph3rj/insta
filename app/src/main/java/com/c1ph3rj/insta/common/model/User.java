package com.c1ph3rj.insta.common.model;

import android.net.Uri;

import com.c1ph3rj.insta.common.model.adpater.PostModel;

import java.util.ArrayList;

public class User {
    private String UserName;
    private String ProfilePic;
    private String EmailId;
    private String Uuid;
    private String PhoneNumber;
    private boolean isNewUser;
    private String currentDeviceDetails;
    private String loginAt;
    private boolean isAccountPrivate;
    private int noOfFollowers;
    private int noOfFollowing;
    private int noOfPost;
    private String aboutUser;
    private ArrayList<UserListModel> followers;
    private ArrayList<UserListModel> following;
    private ArrayList<PostModel> posts;

    public ArrayList<UserListModel> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<UserListModel> followers) {
        this.followers = followers;
    }

    public ArrayList<UserListModel> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<UserListModel> following) {
        this.following = following;
    }

    public ArrayList<PostModel> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<PostModel> posts) {
        this.posts = posts;
    }

    public String getAboutUser() {
        return aboutUser;
    }

    public void setAboutUser(String aboutUser) {
        this.aboutUser = aboutUser;
    }

    public int getNoOfFollowers() {
        return noOfFollowers;
    }

    public void setNoOfFollowers(int noOfFollowers) {
        this.noOfFollowers = noOfFollowers;
    }

    public int getNoOfFollowing() {
        return noOfFollowing;
    }

    public void setNoOfFollowing(int noOfFollowing) {
        this.noOfFollowing = noOfFollowing;
    }

    public int getNoOfPost() {
        return noOfPost;
    }

    public void setNoOfPost(int noOfPost) {
        this.noOfPost = noOfPost;
    }

    public boolean isAccountPrivate() {
        return isAccountPrivate;
    }

    public void setAccountPrivate(boolean accountPrivate) {
        isAccountPrivate = accountPrivate;
    }

    public String getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(String loginAt) {
        this.loginAt = loginAt;
    }

    public String getCurrentDeviceDetails() {
        return currentDeviceDetails;
    }

    public void setCurrentDeviceDetails(String currentDeviceDetails) {
        this.currentDeviceDetails = currentDeviceDetails;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
