package com.c1ph3rj.insta.common.model;

public class UserModel {
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
    private boolean newNotification;
    private boolean newMessage;


    public boolean isNewNotification() {
        return newNotification;
    }

    public void setNewNotification(boolean newNotification) {
        this.newNotification = newNotification;
    }

    public boolean isNewMessage() {
        return newMessage;
    }

    public void setNewMessage(boolean newMessage) {
        this.newMessage = newMessage;
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
