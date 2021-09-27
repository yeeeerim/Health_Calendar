package com.example.community;

public class Comment {
    public int commentNum;
    public String userID;
    public String commentText;
    public int postNum;

    Comment(){}
    Comment(String commentText, int commentNum){
        this.commentText = commentText;
        this.commentNum = commentNum;
    }
    Comment(String userID, String commentText, int postNum, int commentNum){
        this.userID = userID;
        this.commentText = commentText;
        this.postNum = postNum;
        this.commentNum = commentNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getUserID() {
        return userID;
    }

    public String getCommentText() {
        return commentText;
    }

    public int getPostNum() {
        return postNum;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }
}
