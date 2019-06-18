package com.Abdulrohman.TopDownload;

public class RssFeed {
    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setRelesData(String relesData) {
        this.relesData = relesData;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getRelesData() {
        return relesData;
    }

    public String getSummary() {
        return summary;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    private String name;
    private String artist;
    private  String relesData;
    private String summary;
    private String imgUrl;

    @Override
    public String toString() {
        return name +'\n'+artist+'\n'+ relesData+'\n'+imgUrl;
    }
}
