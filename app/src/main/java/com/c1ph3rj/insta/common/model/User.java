package com.c1ph3rj.insta.common.model;

import android.net.Uri;

public class User {
    private String UserName;
    private Uri ProfilePic;
    private String EmailId;
    private String Uuid;
    private String PhoneNumber;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Uri getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(Uri profilePic) {
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
