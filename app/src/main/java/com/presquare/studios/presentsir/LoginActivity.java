package com.presquare.studios.presentsir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.support.design.widget.Snackbar;
import android.widget.TextView;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String USER_NAME = "USER_NAME";

    private static final String LOGIN_URL = "http://presentsir.esy.es/login.php";

    private EditText editTextMobile;

    private EditText editTextPassword;

    private TextView buttonToRegister;

    private TextView header;

    private Button buttonLogin;

    private Animation mAnimation;

    private CoordinatorLayout coordinatorLayout;

    private InputMethodManager input;

    SessionManagement session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Session Management

        session = new SessionManagement(getApplicationContext());

        session.phone();

        session.otp();

        session.select();

        session.route();

        session.upload();

        session.dashboard();


        // Declaring Fonts

        Typeface one = Typeface.createFromAsset(getAssets(), "fonts/Actonia_PERSONAL.ttf");

        Typeface two = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");


        // Setting Content View

        setContentView(R.layout.activity_login);


        // Declaring All Variables

        header = (TextView) findViewById(R.id.header);

        editTextMobile = (EditText) findViewById(R.id.mobile);

        editTextPassword = (EditText) findViewById(R.id.password);

        buttonLogin = (Button) findViewById(R.id.btnLogin);

        buttonToRegister = (TextView) findViewById(R.id.btnLinkToRegisterScreen);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        // Setting Fonts

        buttonLogin.setOnClickListener(this);

        header.setTypeface(one);

        editTextMobile.setTypeface(two);

        editTextPassword.setTypeface(two);

        buttonLogin.setTypeface(two);

        buttonToRegister.setTypeface(two);


        // Animating Header To Slide Up

        slideUp();


        // Hiding Status Bar

        hideStatusBar();


        // Internet Connection Check

        initialCheck();


        // Create Account Clicked

        createClicked();


        // Initialize Keyboard Login

        keyboardLogin();


    }



    private void createClicked() {

        buttonToRegister.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                finish();

            }

        });

    }



    private void initialCheck() {

        if (!isOnline()) {

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Internet Connection!", Snackbar.LENGTH_SHORT);

            snackbar.show();

        }

    }



    private void slideUp() {

        mAnimation = new TranslateAnimation(0, 0, 100, 0);

        mAnimation.setDuration(1000);

        header.setAnimation(mAnimation);

    }



    private void hideStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }

    }



    public boolean isOnline() {

        final ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }



    public void check() {

        if (isOnline())
        {

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Connected To The Internet!", Snackbar.LENGTH_LONG);

            snackbar.show();


        }

        else {

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No Connection Found!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            check();
                        }
                    });

            snackbar.show();
        }

    }



    private void emptycheck() {

        if (editTextMobile.getText().toString().matches("")) {

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Enter Your Mobile Number!", Snackbar.LENGTH_SHORT);

            snackbar.show();

            return;

        }

        if (editTextPassword.getText().toString().matches("")) {

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Enter Your Password!", Snackbar.LENGTH_SHORT);

            snackbar.show();

            return;

        }

        login();

    }



    private void keyboardLogin() {

        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                    input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    input.hideSoftInputFromWindow(editTextPassword.getWindowToken(), 0);

                    emptycheck();

                    return true;

                }

                return false;

            }

        });

    }



    private void login(){

        String username = editTextMobile.getText().toString().trim();

        String password = editTextPassword.getText().toString().trim();

        userLogin(username,password);

    }


    private void userLogin(final String username, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = new ProgressDialog(LoginActivity.this, R.style.MyTheme);
                loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                loading.setCancelable(false);
                loading.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if(s.equalsIgnoreCase("Successfully Logged In!")){

                    Intent intent = new Intent(LoginActivity.this,PhoneNumber.class);
                    finish();
                    intent.putExtra(USER_NAME,username);
                    session.createLoginSession(username);
                    startActivity(intent);

                }

                if(s.equalsIgnoreCase("")){

                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Loading... Try Again!", Snackbar.LENGTH_LONG);

                    snackbar.show();

                }

                else{

                    Snackbar snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_LONG);

                    snackbar.show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("mobile",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username,password);

    }


    @Override
    public void onClick(View v) {
        if(v == buttonLogin){

                if (isOnline()) {

                    emptycheck();

                } else {


                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    check();
                                }
                            });

                    snackbar.show();
                }

        }

    }


}