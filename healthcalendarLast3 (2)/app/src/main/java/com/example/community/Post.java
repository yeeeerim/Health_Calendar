package com.example.community;

import java.io.File;

public class Post {
    public String userID; //작성자id
    public int postNum; //글번호
    public String category; //말머리
    public String title; //제목
    public String mainText; //본문
    public File file; //첨부파일
    public int goodCnt = 0; //잘했어요 수
    public int hateCnt = 0; //노력해요 수

    /* 생성자 */
    public Post(){}
    public Post(String title, String category, int postNum, String userID){
        this.postNum = postNum;
        this.title = title;
        this.category = category;
        this.userID = userID;
    }
    public Post(String title, int postNum, String mainText, String category, String userID, int goodCnt, int hateCnt){
        this.title = title;
        this.postNum = postNum;
        this.category = category;
        this.mainText = mainText;
        this.userID = userID;
        this.goodCnt = goodCnt;
        this.hateCnt = hateCnt;
    }


    /* SET/GET */

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setGoodCnt(int goodCnt) {
        this.goodCnt = goodCnt;
    }

    public void setHateCnt(int hateCnt) {
        this.hateCnt = hateCnt;
    }

    public String getUserID() {
        return userID;
    }

    public int getPostNum() {
        return postNum;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getMainText() {
        return mainText;
    }

    public File getFile() {
        return file;
    }

    public int getGoodCnt() {
        return goodCnt;
    }

    public int getHateCnt() {
        return hateCnt;
    }
}
