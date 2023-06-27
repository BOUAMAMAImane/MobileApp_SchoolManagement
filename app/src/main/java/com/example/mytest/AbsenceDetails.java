package com.example.mytest;

public class AbsenceDetails {
    private int id;
    private String session;
    private String date;
    private String penalty;
    private String justification;

    public AbsenceDetails(String session, String date, String penalty, String justification) {
        this.id = id;
        this.session = session;
        this.date = date;
        this.penalty = penalty;
        this.justification = justification;
    }

    public int getId() {
        return id;
    }

    public String getSession() {
        return session;
    }

    public String getDate() {
        return date;
    }

    public String getPenalty() {
        return penalty;
    }

    public String getJustification() {
        return justification;
    }
}
