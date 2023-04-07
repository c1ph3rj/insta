package com.c1ph3rj.insta.common.model;

import java.util.ArrayList;

public class CommentsModel {
    private String commentsUuid;
    private String comment;
    private String timestamp;
    private FriendsModel commentedUserDetails;
    private ArrayList<CommentsModel> listOfReplyComments;

    public String getCommentsUuid() {
        return commentsUuid;
    }

    public void setCommentsUuid(String commentsUuid) {
        this.commentsUuid = commentsUuid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public FriendsModel getCommentedUserDetails() {
        return commentedUserDetails;
    }

    public void setCommentedUserDetails(FriendsModel commentedUserDetails) {
        this.commentedUserDetails = commentedUserDetails;
    }

    public ArrayList<CommentsModel> getListOfReplyComments() {
        return listOfReplyComments;
    }

    public void setListOfReplyComments(ArrayList<CommentsModel> listOfReplyComments) {
        this.listOfReplyComments = listOfReplyComments;
    }
}
