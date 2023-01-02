package com.example.easyexit;

public class UserData2 {
    private String Time;
    private String OutTime;
    private String Rollno;
    private String Reason;
    private String Number;
    private String Status;

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public UserData2(String time, String rollno, String reason, String number, String outtime, String status) {
        Time = time;
        Rollno = rollno;
        Reason = reason;
        Number = number;
        OutTime = outtime;
        Status = status;
    }
    public UserData2()
    {

    }
    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
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
}
