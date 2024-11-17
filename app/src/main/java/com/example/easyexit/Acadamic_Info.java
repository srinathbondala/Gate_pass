package com.example.easyexit;

import java.util.List;

public class Acadamic_Info {
    private String Year;
    private int semister;
    private List<subject_details> subjects;
    float SGPA;
    float CGPA;
    float attedance;

    public Acadamic_Info(){}
    public Acadamic_Info(String year, int semister, List<subject_details> subjects, float SGPA, float CGPA, float attedance) {
        Year = year;
        this.semister = semister;
        this.subjects = subjects;
        this.SGPA = SGPA;
        this.CGPA = CGPA;
        this.attedance = attedance;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public int getSemister() {
        return semister;
    }

    public void setSemister(int semister) {
        this.semister = semister;
    }

    public List<subject_details> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<subject_details> subjects) {
        this.subjects = subjects;
    }

    public float getSGPA() {
        return SGPA;
    }

    public void setSGPA(float SGPA) {
        this.SGPA = SGPA;
    }

    public float getCGPA() {
        return CGPA;
    }

    public void setCGPA(float CGPA) {
        this.CGPA = CGPA;
    }
}
