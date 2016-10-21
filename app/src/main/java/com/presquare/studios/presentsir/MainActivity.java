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
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String REGISTER_URL = "http://presentsir.esy.es/register.php";

    private EditText editTextName;

    private EditText editTextEmail;

    private EditText editTextPassword;

    private EditText editTextMobile;

    private Button buttonRegister;

    private TextView buttonToLogin;

    private TextView header;

    private Animation mAnimation;

    private CoordinatorLayout coordinatorLayout;

    private InputMethodManager input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Declaring Fonts

        Typeface one= Typeface.createFromAsset(getAssets(),"fonts/Actonia_PERSONAL.ttf");

        Typeface two= Typeface.createFromAsset(getAssets(),"fonts/Lato-Light.ttf");


        // Setting Content View

        setContentView(R.layout.activity_main);


        // Declaring All Variables

        header = (TextView) findViewById(R.id.header);

        editTextName = (EditText) findViewById(R.id.name);

        editTextEmail = (EditText) findViewById(R.id.emailad);

        editTextPassword = (EditText) findViewById(R.id.pass);

        editTextMobile = (EditText) findViewById(R.id.mob);

        buttonRegister = (Button) findViewById(R.id.btnRegister);

        buttonToLogin = (TextView) findViewById(R.id.btnLinkToLoginScreen);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        // Setting Fonts

        buttonRegister.setOnClickListener(this);

        header.setTypeface(one);

        editTextName.setTypeface(two);

        editTextEmail.setTypeface(two);

        editTextPassword.setTypeface(two);

        editTextMobile.setTypeface(two);

        buttonRegister.setTypeface(two);

        buttonToLogin.setTypeface(two);


        // Animating Header To Slide Up

        slideUp();


        // Hiding Status Bar

        getter();

        hideStatusBar();


        // Internet Connection Check

        initialCheck();


        // Create Account Clicked

        loginClicked();


        // Initialize Keyboard Signup

        keyboardSignup();

    }




    private void getter() {

        if (Build.VERSION.SDK_INT >= 21) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        }


    }



    private void loginClicked() {

        buttonToLogin.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, LoginActivity.class));

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

        if (editTextName.getText().toString().matches("")) {

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Enter Your Full Name!", Snackbar.LENGTH_SHORT);

            snackbar.show();

            return;

        }

        if (editTextEmail.getText().toString().matches("")) {

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Enter Your Email!", Snackbar.LENGTH_SHORT);

            snackbar.show();

            return;

        }

        if (editTextPassword.getText().toString().matches("")) {

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Enter Your Password!", Snackbar.LENGTH_SHORT);

            snackbar.show();

            return;

        }

        if (editTextMobile.getText().toString().matches("")) {

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Enter Your Password!", Snackbar.LENGTH_SHORT);

            snackbar.show();

            return;

        }

        registerUser();

    }



    private void keyboardSignup() {

        editTextMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {

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



    private void registerUser() {

        String name = editTextName.getText().toString();

        String email = editTextEmail.getText().toString();

        String password = editTextPassword.getText().toString();

        String mobile = editTextMobile.getText().toString();

        register(name,email,password,mobile);

    }



    private void register(final String name, String email, String password, String mobile) {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = new ProgressDialog(MainActivity.this,R.style.MyTheme);
                loading.setCancelable(false);
                loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                loading.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if(s.equalsIgnoreCase("Successfully Registered!")){

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, s, Snackbar.LENGTH_LONG);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();

                    editTextName.getText().clear();

                    editTextEmail.getText().clear();

                    editTextPassword.getText().clear();

                    editTextMobile.getText().clear();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent i=new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, 1000);
                }

                if(s.equalsIgnoreCase("")){

                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Loading... Try Again!", Snackbar.LENGTH_LONG);

                    snackbar.show();

                }

                else {

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, s, Snackbar.LENGTH_LONG);

                    snackbar.show();

                }

            }


            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("email",params[1]);
                data.put("password",params[2]);
                data.put("mobile",params[3]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name,email,password,mobile);
    }



    @Override
    public void onClick(View v) {

        if(v == buttonRegister){

            if (isOnline()) {

                emptycheck();

            }

            else {

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "You Are Not Connected To The Internet!", Snackbar.LENGTH_INDEFINITE)
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