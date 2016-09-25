package net.dydrich.e_schoolstudente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class CalendarActivity extends AppCompatActivity {

    private RBCalendar rbCalendar;
    private SharedPreferences prefs;
    private GetJSONData jsonData;
    private ListView calendarListView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("On CalendarActivity");
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = getSharedPreferences("user.prefs", MODE_PRIVATE);
        String cls = prefs.getString("classDesc", "");
        System.out.println("Classe: " + cls);
        getSupportActionBar().setTitle("Classe " + cls);

        rbCalendar = new RBCalendar(null, RBCalendar.MIDDLE_SCHOOL, null, null, null);
        loadCalendar();

    }

    private void loadCalendar() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            jsonData = new GetJSONData();
            jsonData.execute("");
        } else {
            // TODO: network connection error
            System.out.println("No network connection");
        }
    }

    private String getActivities() {
        return "";
    }

    public class GetJSONData extends AsyncTask<String, Void, ArrayList<JSONObject>> {

        GetJSONData() {
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(String... params) {
            String url = "https://www.rbachis.net/rclasse/mobile/get_calendar.php";
            WebRequest webreq = new WebRequest();

            HashMap<String, String> parameters = new HashMap<>();

            parameters.put("cls", prefs.getString("classID", ""));

            // Making a request to url and getting response
            String contentAsString = webreq.makeWebServiceCall(url, WebRequest.POSTRequest, parameters);
            String status = "ko";
            System.out.println("Activities:" + contentAsString);
            ArrayList<JSONObject> objAdapter = new ArrayList<>();
            try {
                JSONObject jsonObj = new JSONObject(contentAsString);
                status = jsonObj.getString("status");
                List<RBActivity> rbActivities = new LinkedList<RBActivity>();
                String data = jsonObj.getString("data");
                System.out.println("Data on GETJSONDATA: " + data);
                JSONObject objData = new JSONObject(data);
                String[] keys;
                Iterator<String> keysNames = objData.keys();
                while (keysNames.hasNext()) {
                    JSONArray jsonData = objData.getJSONArray(keysNames.next());
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject act = new JSONObject(jsonData.getString(i));
                        objAdapter.add(i, act);
                    }
                }

            } catch (JSONException jex) {
                return null;
            }

            return objAdapter;
        }

        protected void onPostExecute(ArrayList<JSONObject> data) {
            if (data != null) {
                RBActivitiesAdapter rbAdapt = new RBActivitiesAdapter(getApplicationContext(), data);
                calendarListView = (ListView) findViewById(R.id.calendarListView);
                calendarListView.setAdapter(rbAdapt);
            }
            else {
                System.out.println("ArrayList is null");
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

    public RBCalendar getRbCalendar() {
        return rbCalendar;
    }

    public void setRbCalendar(RBCalendar rbCalendar) {
        this.rbCalendar = rbCalendar;
    }

    private class RBActivitiesAdapter extends ArrayAdapter<JSONObject> {
        RBActivitiesAdapter(Context context, ArrayList<JSONObject> jsonObjs) {
            super(context, 0, jsonObjs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            JSONObject act = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_row, parent, false);
            }
            // Lookup view for data population
            TextView startDate = (TextView) convertView.findViewById(R.id.dayRow);
            TextView actDesc = (TextView) convertView.findViewById(R.id.actText);

            try {
                DateFormat inDate = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
                DateFormat outDate = new SimpleDateFormat("EEEE d MMMM", Locale.ITALY);
                Date d = inDate.parse(act.getString("data_inizio"));

                startDate.setText(outDate.format(d));
                actDesc.setText(Html.fromHtml("<strong>"+act.getString("mat")+"</strong>: "+act.getString("descrizione")));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
