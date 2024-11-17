package com.example.easyexit;

public class notification_data {
    String title,url,faculty,range,description,time;

    public notification_data(){}
    public notification_data(String title, String url, String facultyId, String range, String description,String time) {
        this.title = title;
        this.url = url;
        this.faculty = facultyId;
        this.range = range;
        this.description = description;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String facultyId) {
        this.faculty = facultyId;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
