package com.example.mytest;
public class AbsentSubject {
    private String session;
    private String sessionDate;
    private String justification;
    private String cne;
    private String penalty;
    private String state;

    public AbsentSubject(String session, String sessionDate, String justification, String cne, String penalty, String state) {
        this.session = session;
        this.sessionDate = sessionDate;
        this.justification = justification;
        this.cne = cne;
        this.penalty = penalty;
        this.state = state;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getCne() {
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
