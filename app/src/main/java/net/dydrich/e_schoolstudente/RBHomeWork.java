package net.dydrich.e_schoolstudente;

import java.util.Date;
import java.util.Map;

/**
 * Created by riccardo on 9/19/16.
 */
class RBHomeWork extends RBActivity {

    protected int year;
    protected Map<String, String> subject;
    protected String notes;

    public RBHomeWork(int ID, int year, Map<String, String> subject, String notes) {
        super(ID);
        this.year = year;
        this.subject = subject;
        this.notes = notes;
    }

    public RBHomeWork(int ID, Date startDate, String description, int type, Date insertDateTime, Map<String, String> owner, int year, Map<String, String> subject, String notes) {
        super(ID, startDate, description, type, insertDateTime, owner);
        this.year = year;
        this.subject = subject;
        this.notes = notes;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Map<String, String> getSubject() {
        return subject;
    }

    public void setSubject(Map<String, String> subject) {
        this.subject = subject;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
