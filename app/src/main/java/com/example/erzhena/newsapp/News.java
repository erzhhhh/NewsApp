package com.example.erzhena.newsapp;

public class News {
    private String mTitle;
    private String mDescription;
    private String mDateValue;
    private String mURL;
    private String mThumbnail;

    public News(String vTitle, String vDescription, String vDateValue, String vURL, String vThumb){
        mTitle = vTitle;
        mDescription = vDescription;
        mDateValue = vDateValue;
        mURL = vURL;
        mThumbnail = vThumb;
    }

    public String getmTitle() {return mTitle;}

    public String getmDescription() {return mDescription;}

    public String getmDateValue() {return mDateValue;}

    public String getmURL() {return mURL;}

    public String getmThumbnail() {return mThumbnail;}

}