package net.dydrich.e_schoolstudente;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by riccardo on 9/19/16.
 */
public class RBCalendar {

    public static final int PRMARY_SCHOOL = 1;
    public static final int MIDDLE_SCHOOL = 2;

    private List<RBActivity> activities;
    private int schoolOrder;
    private Map<String, Object> rbclass;
    private Map<String, Object> teacher;
    private Map<String, Object> subject;

    public RBCalendar() {
    }

    public RBCalendar(List<RBActivity> activities, int schoolOrder, Map<String, Object> rbclass, Map<String, Object> teacher, Map<String, Object> subject) {
        this.activities = activities;
        this.schoolOrder = schoolOrder;
        this.rbclass = rbclass;
        this.teacher = teacher;
        this.subject = subject;
    }

    public List<RBActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<RBActivity> activities) {
        this.activities = activities;
    }

    public int getSchoolOrder() {
        return schoolOrder;
    }

    public void setSchoolOrder(int schoolOrder) {
        this.schoolOrder = schoolOrder;
    }

    public Map<String, Object> getRbclass() {
        return rbclass;
    }

    public void setRbclass(Map<String, Object> rbclass) {
        this.rbclass = rbclass;
    }

    public Map<String, Object> getTeacher() {
        return teacher;
    }

    public void setTeacher(Map<String, Object> teacher) {
        this.teacher = teacher;
    }

    public Map<String, Object> getSubject() {
        return subject;
    }

    public void setSubject(Map<String, Object> subject) {
        this.subject = subject;
    }

    public void loadActivities(String jsonData) {
        if (jsonData != null) {
            try {
                // Hashmap for ListView
                JSONObject jsonObj = new JSONObject(jsonData);

                // user string
                String user = jsonObj.getString("user");
                // Getting JSON user node
                JSONObject student = new JSONObject(user);
                /*
                String id = (String) student.getString(TAG_ID);
                String uniqID = student.getString(TAG_UNIQID);
                String fname = student.getString(TAG_STUDENT_FNAME);
                String lname = student.getString(TAG_STUDENT_LNAME);
                String username = student.getString(TAG_USERNAME);
                String token = student.getString(TAG_TOKEN);
                String school = student.getString(TAG_SCHOOL);
                String classID = student.getString(TAG_CLASS_ID);
                String classDesc = student.getString(TAG_CLASS_DESC);

                // tmp hashmap for single student
                HashMap<String, String> studentValues = new HashMap<String, String>();

                // adding every child node to HashMap key => value
                studentValues.put(TAG_ID, id);
                studentValues.put(TAG_UNIQID, uniqID);
                studentValues.put(TAG_STUDENT_FNAME, fname);
                studentValues.put(TAG_STUDENT_LNAME, lname);
                studentValues.put(TAG_USERNAME, username);
                studentValues.put(TAG_TOKEN, token);
                studentValues.put(TAG_SCHOOL, school);
                studentValues.put(TAG_CLASS_ID, classID);
                studentValues.put(TAG_CLASS_DESC, classDesc);
                */
            } catch (JSONException jx) {

            }
        }
    }
}
