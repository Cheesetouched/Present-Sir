package com.presquare.studios.presentsir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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

public class Route extends AppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener{


    SessionManagement session;

    public static final String ROUTE_NAME = "ROUTE_NAME";

    private static final String ADD_URL = "http://presentsir.esy.es/addroute.php";

    private TextView schooltext;

    private TextView branchtext;

    private TextView route;

    private Spinner spinner;

    private ArrayList<String> routes;

    private JSONArray routearray;

    private String student;

    private String link;

    private String routeselect;

    private String schoolpri;

    private String branchpri;

    private Button buttonNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Session Management

        session = new SessionManagement(getApplicationContext());


        // Checking For First Run

        firstRun();


        // Declaring Font

        Typeface one= Typeface.createFromAsset(getAssets(),"fonts/Quando-Regular.ttf");


        // Getting School Name And Branch Name Stored In Session

        HashMap<String, String> select = session.getSelectDetails();


        // Getting Mobile Number Stored In Session

        HashMap<String, String> user = session.getUserDetails();


        // Storing Mobile Number, School Name And Branch Name In A Variable

        student = user.get(SessionManagement.KEY_NAME);

        schoolpri = select.get(SessionManagement.KEY_SCHOOL);

        branchpri = select.get(SessionManagement.KEY_BRANCH);


        // Setting Content View

        setContentView(R.layout.activity_route);


        // Declaring All Variables

        buttonNext = (Button) findViewById(R.id.go);

        buttonNext.setOnClickListener(this);

        schooltext = (TextView) findViewById(R.id.textViewSchool);

        branchtext = (TextView) findViewById(R.id.textViewBranch);

        schooltext.setText(Html.fromHtml(schoolpri));

        branchtext.setText(Html.fromHtml(branchpri));

        route = (TextView) findViewById(R.id.textViewRoute);


        //Setting Fonts

        schooltext.setTypeface(one);

        branchtext.setTypeface(one);

        route.setTypeface(one);


        // Initializing Action Bar

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        // Internet Connection Check

        initialCheck();


        // Initializing Array List

        routes = new ArrayList<String>();


        // Initializing Spinner

        spinner = (Spinner) findViewById(R.id.routespinner);

        spinner.setOnItemSelectedListener(this);


        //Getting Spinner Data

        getData();


    }



    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);



    private void firstRun() {

        Boolean isFirst = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirst", false);

        if (isFirst) {

            startActivity(new Intent(Route.this,Upload.class));

            finish();

        }

    }



    private void initialCheck() {

        if (!isOnline()) {

            Toast.makeText(Route.this, "No Internet Connection!", Toast.LENGTH_LONG).show();

        }

    }



    public boolean isOnline() {

        final ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }


    private void getData(){

        String http = "http://presentsir.esy.es/routes.php";

        link = Uri.parse(http)
                .buildUpon()
                .appendQueryParameter("a", schoolpri)
                .appendQueryParameter("b", branchpri)
                .build().toString();

        StringRequest stringRequest = new StringRequest(link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject j = null;
                        try {

                            j = new JSONObject(response);

                            routearray = j.getJSONArray(Config.JSON_ROUTE);

                            getStudents(routearray);

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

                routes.add(json.getString(Config.TAG_ROUTES));

            }

            catch (JSONException e) {

                e.printStackTrace();

            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Route.this, android.R.layout.simple_spinner_dropdown_item, routes);

        spinner.setAdapter(adapter);

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selected = parent.getItemAtPosition(position).toString();

        routeselect = selected;

        route.setText(routeselect);

    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        route.setText("Select Your Route");

    }




    private void addRoute() {

        String studentnumber = student.toString().trim();

        String pass = routeselect;

        add(studentnumber, pass);

    }



    private void add(final String studentnumber, final String pass) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = new ProgressDialog(Route.this,R.style.MyTheme);
                loading.setCancelable(false);
                loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                loading.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if(s.equalsIgnoreCase("Route Added!")){

                    session.createRouteSession();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent i=new Intent(Route.this,Upload.class);
                            i.putExtra(ROUTE_NAME, pass);
                            session.RouteSession(pass);
                            startActivity(i);
                            finish();
                        }
                    }, 1000);
                }

                if(s.equalsIgnoreCase("")){

                    Toast.makeText(Route.this, "Loading... Try Again!", Toast.LENGTH_LONG).show();

                }

                else {

                    Toast.makeText(Route.this, s, Toast.LENGTH_LONG).show();

                }

            }


            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("studentnumber",params[0]);
                data.put("route",params[1]);

                String result = ruc.sendPostRequest(ADD_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(studentnumber, pass);
    }



    @Override
    public void onClick(View v) {
        if(v == buttonNext){

            v.startAnimation(buttonClick);

            if (isOnline()) {

                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirst", true).commit();

                addRoute();

            } else {

                Toast.makeText(Route.this, "Can't Connect To The Internet!", Toast.LENGTH_LONG).show();

            }

        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent back = new Intent(Route.this, Select.class);
                startActivity(back);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}
