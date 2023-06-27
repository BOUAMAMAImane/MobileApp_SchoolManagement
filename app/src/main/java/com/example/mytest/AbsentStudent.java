package com.example.mytest;
public class AbsentStudent {
    private String cne;
    private String penalty;
    private boolean justified;
    private String session;
    private String sessionDate;

    public AbsentStudent( String penalty, boolean justified, String session, String sessionDate) {
        this.cne = cne;
        this.penalty = penalty;
        this.justified = justified;
        this.session = session;
        this.sessionDate = sessionDate;
    }

    public String getCNE() {
        return cne;
    }

    public String getPenalty() {
        return penalty;
    }

    public boolean isJustified() {
        return justified;
    }

    public String getSession() {
        return session;
    }

    public String getSessionDate() {
        return sessionDate;
    }

}
