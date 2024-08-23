package com.example.easyexit;

public class subject_details {
    String subject_code;
    String subject_name;
    float subject_attedance;
    char subject_grade;
    int subject_internal;
    int subject_credits;

    public subject_details(){}
    public subject_details(String subject_code, String subject_name, float subject_attedance, char subject_grade, int subject_internal, int subject_credits) {
        this.subject_code = subject_code;
        this.subject_name = subject_name;
        this.subject_attedance = subject_attedance;
        this.subject_grade = subject_grade;
        this.subject_internal = subject_internal;
        this.subject_credits = subject_credits;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public float getSubject_attedance() {
        return subject_attedance;
    }

    public void setSubject_attedance(float subject_attedance) {
        this.subject_attedance = subject_attedance;
    }

    public char getSubject_grade() {
        return subject_grade;
    }

    public void setSubject_grade(char subject_grade) {
        this.subject_grade = subject_grade;
    }

    public int getSubject_internal() {
        return subject_internal;
    }

    public void setSubject_internal(int subject_internal) {
        this.subject_internal = subject_internal;
    }

    public int getSubject_credits() {
        return subject_credits;
    }

    public void setSubject_credits(int subject_credits) {
        this.subject_credits = subject_credits;
    }
}
