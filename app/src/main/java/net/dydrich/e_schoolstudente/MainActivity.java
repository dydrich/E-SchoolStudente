package net.dydrich.e_schoolstudente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int GET_JSON_DATA = 1;

    // JSON Node names
    private static final String TAG_USER_INFO = "user";
    private static final String TAG_ID = "uid";
    private static final String TAG_UNIQID = "uniqID";
    private static final String TAG_STUDENT_FNAME = "fname";
    private static final String TAG_STUDENT_LNAME = "lname";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_TOKEN = "token";
    private static final String TAG_SCHOOL = "school";
    private static final String TAG_CLASS_ID = "classID";
    private static final String TAG_CLASS_DESC = "classDesc";
    private static final String TAG_AREA = "area";

    protected String tmpJSONString;

    private NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("user.prefs", MODE_PRIVATE);

        String token = prefs.getString("token", "");

        if (!connect()) {
            Toast atoast = Toast.makeText(getApplicationContext(), "Connessione assente", Toast.LENGTH_LONG);
            atoast.setGravity(Gravity.CENTER, 0, 0);
            atoast.show();
        }

        if (token.equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Non hai ancora effettuato il login";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, GET_JSON_DATA);
        }
        else {
            signInWithToken(token);
        }
    }

    private boolean connect() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    protected void signInWithToken(String tok) {
        if (networkInfo != null && networkInfo.isConnected()) {
            String stringUrl = getString(R.string.login_url);
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            LoginWithTokenTask mAuthTask = new LoginWithTokenTask(tok);
            mAuthTask.execute((String) stringUrl);
        } else {
            // TODO: network connection error
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == GET_JSON_DATA) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                SharedPreferences prefs = getSharedPreferences("user.prefs", MODE_PRIVATE);
                String content = data.getStringExtra("result");
                HashMap<String, String> preferences = ParseJSON(content);
                Iterator<String> prefsIterator = preferences.keySet().iterator();
                SharedPreferences.Editor shed = prefs.edit();
                while(prefsIterator.hasNext()){
                    String key = prefsIterator.next();
                    shed.putString(key, preferences.get(key));
                }
                shed.commit();
                Intent cal = new Intent(this, CalendarActivity.class);
                cal.putExtra("netInfo", networkInfo);
                startActivity(cal);
            }
            else {
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong", duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Wrong request???", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private HashMap<String, String> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                JSONObject jsonObj = new JSONObject(json);

                // user string
                String user = jsonObj.getString("user");
                // Getting JSON user node
                JSONObject student = new JSONObject(user);

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

                return studentValues;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP request");
            return null;
        }
    }

    public class LoginWithTokenTask extends AsyncTask<String, Void, Boolean> {

        private final String myToken;

        LoginWithTokenTask(String token) {
            myToken = token;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String url = "https://www.rbachis.net/rclasse/mobile/authenticate.php";
            WebRequest webreq = new WebRequest();

            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("token", myToken);
            parameters.put("area", "2");

            // Making a request to url and getting response
            String contentAsString = webreq.makeWebServiceCall(url, WebRequest.POSTRequest, parameters);
            String status = "ko";
            System.out.println("Login:" + contentAsString);
            try {
                JSONObject jsonObj = new JSONObject(contentAsString);
                status = jsonObj.getString("status");
            } catch (JSONException jex) {
                return false;
            }

            if (status.equals("ok")) {
                Intent cal = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(cal);
                return true;
            }
            else {
                System.out.println(contentAsString);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (!success.equals("error")) {
                finish();
            } else {
                // todo authentication error
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);
        }

        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }
}
