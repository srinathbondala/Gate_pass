package com.example.easyexit;

public class UserData1 {
    private String Name;
    private String Email;
    private String PhoneNumber;
    private String Password;
    private String Time;
    private String Rollno;
    private String Year;
    private String Branch;
    private String Facaltyno;

    public String getFacaltyno() {
        return Facaltyno;
    }

    public void setFacaltyno(String facaltyno) {
        Facaltyno = facaltyno;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public UserData1(String name, String email, String phoneNumber, String password, String time, String rollno, String year, String branch,String facaltyno) {
        Name = name;
        Email = email;
        PhoneNumber = phoneNumber;
        Password = password;
        Time = time;
        Rollno = rollno;
        Year = year;
        Branch = branch;
        Facaltyno = facaltyno;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getRollno() {
        return Rollno;
    }

    public void setRollno(String rollno) {
        Rollno = rollno;
    }

    public UserData1() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}