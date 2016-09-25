package net.dydrich.e_schoolstudente;

import java.util.Date;
import java.util.Map;

/**
 * Created by riccardo on 9/19/16.
 */
public abstract class RBActivity {

    protected int ID;
    protected Date startDate;
    protected String description;
    protected int type;
    protected Date insertDateTime;
    protected Map<String, String> owner;
    protected Map<String, String> schoolClass;

    public final static int CLASS_ACTIVITY = 1;
    public static int HOMEWORK = 2;
    public static int EVENT = 3;

    public RBActivity() {
    }

    public RBActivity(int ID) {
        this.ID = ID;
    }

    public RBActivity(int ID, Date startDate, String description, int type, Date insertDateTime, Map<String, String> owner) {
        this.ID = ID;
        this.startDate = startDate;
        this.description = description;
        this.type = type;
        this.insertDateTime = insertDateTime;
        this.owner = owner;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getInsertDateTime() {
        return insertDateTime;
    }

    public void setInsertDateTime(Date insertDateTime) {
        this.insertDateTime = insertDateTime;
    }

    public Map<String, String> getOwner() {
        return owner;
    }

    public void setOwner(Map<String, String> owner) {
        this.owner = owner;
    }

    public Map<String, String> getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(Map<String, String> schoolClass) {
        this.schoolClass = schoolClass;
    }
}
