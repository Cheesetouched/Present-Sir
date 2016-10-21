package com.presquare.studios.presentsir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Select extends AppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener {


    SessionManagement session;

    public static final String SCHOOL_NAME = "SCHOOL_NAME";

    public static final String BRANCH_NAME = "BRANCH_NAME";

    private static final String ADD_URL = "http://presentsir.esy.es/addschool.php";

    private Spinner spinner;

    private ArrayList<String> students;

    private JSONArray result;

    private TextView school;

    private TextView branch;

    private TextView buses;

    private String number;

    private Button buttonNext;

    private ActionBar header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Session Management

        session = new SessionManagement(getApplicationContext());


        // Checking For First Run

        firstRun();


        // Declaring Fonts

        Typeface one= Typeface.createFromAsset(getAssets(),"fonts/Quando-Regular.ttf");


        // Getting Mobile Number Stored In Session

        HashMap<String, String> user = session.getUserDetails();


        // Storing Mobile Number In A String

        number = user.get(SessionManagement.KEY_NAME);


        // Setting Content View

        setContentView(R.layout.activity_select);


        // Declaring All Variables

        buttonNext = (Button) findViewById(R.id.go);

        buttonNext.setOnClickListener(this);

        school = (TextView) findViewById(R.id.textViewSchool);

        branch = (TextView) findViewById(R.id.textViewBranch);

        buses = (TextView) findViewById(R.id.textViewBuses);


        // Setting Fonts

        school.setTypeface(one);

        branch.setTypeface(one);

        buses.setTypeface(one);


        // Hiding Status Bar

        hideStatusBar();

        // Internet Connection Check

        initialCheck();


        //Initializing Array List

        students = new ArrayList<String>();


        //Initializing Spinner

        spinner = (Spinner) findViewById(R.id.schoolspinner);

        spinner.setOnItemSelectedListener(this);


        // Getting Spinner Data

        getData();

    }



    private void firstRun() {

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", false);

        if (isFirstRun) {

            startActivity(new Intent(Select.this,Route.class));

            finish();

        }

    }



    private void hideStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);



        }

    }



    private void initialCheck() {

        if (!isOnline()) {

            Toast.makeText(Select.this, "No Internet Connection!", Toast.LENGTH_LONG).show();


        }

    }



    public boolean isOnline() {

        final ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }



    private void addSchool() {

        String studentnumber = number.toString().trim();

        String School = school.getText().toString();

        String Branch = branch.getText().toString();

        add(studentnumber, School, Branch);

    }



    private void add(final String studentnumber, final String school, final String branch) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = new ProgressDialog(Select.this,R.style.MyTheme);
                loading.setCancelable(false);
                loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                loading.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if(s.equalsIgnoreCase("School Added!")){

                    session.createSelectSession();

                    Intent i = new Intent(Select.this,Route.class);

                    i.putExtra(SCHOOL_NAME, school);

                    i.putExtra(BRANCH_NAME, branch);

                    session.SelectSession(school, branch);

                    startActivity(i);

                }


                if(s.equalsIgnoreCase("")){

                    Toast.makeText(Select.this, "Loading... Try Again!", Toast.LENGTH_LONG).show();

                }

                if(s.equalsIgnoreCase("Please Select Your School And Branch!")){

                    Intent i = getIntent();

                    startActivity(i);

                }

                else {

                    Toast.makeText(Select.this, s, Toast.LENGTH_LONG).show();

                }

            }


            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("studentnumber",params[0]);
                data.put("school",params[1]);
                data.put("branch",params[2]);

                String result = ruc.sendPostRequest(ADD_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(studentnumber, school, branch);
    }



    private void getData(){

        StringRequest stringRequest = new StringRequest(Config.SCHOOLS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject j = null;

                        try {

                            j = new JSONObject(response);

                            result = j.getJSONArray(Config.JSON_ARRAY);

                            getStudents(result);

                        }

                        catch (JSONException e) {

                            e.printStackTrace();

                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }



    private void getStudents(JSONArray j){

        for(int i=0;i<j.length();i++){

            try {

                JSONObject json = j.getJSONObject(i);

                students.add(json.getString(Config.TAG_USERNAME));

            }

            catch (JSONException e) {

                e.printStackTrace();

            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Select.this, android.R.layout.simple_spinner_dropdown_item, students);

        spinner.setAdapter(adapter);

    }


    private String getName(int position){

        String name="";

        try {

            JSONObject json = result.getJSONObject(position);

            name = json.getString(Config.TAG_SCHOOL);

        }

        catch (JSONException e) {

            e.printStackTrace();

        }

        return name;

    }



    private String getCourse(int position){

        String course="";

        try {

            JSONObject json = result.getJSONObject(position);

            course = json.getString(Config.TAG_BRANCH);

        }

        catch (JSONException e) {

            e.printStackTrace();

        }

        return course;
    }



    private String getSession(int position){

        String session="";

        try {

            JSONObject json = result.getJSONObject(position);

            session = json.getString(Config.TAG_BUSES);

        }

        catch (JSONException e) {

            e.printStackTrace();

        }

        return session;

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        school.setText(getName(position));

        branch.setText(getCourse(position));

        buses.setText(getSession(position));

    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        school.setText("");

        branch.setText("");

        buses.setText("");

    }



    @Override
    public void onClick(View v) {
        if(v == buttonNext){

            if (isOnline()) {

                addSchool();

            } else {

                Toast.makeText(Select.this, "Can't Connect To The Internet!", Toast.LENGTH_LONG).show();

            }

        }

    }


}
