package com.presquare.studios.presentsir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;

public class UserProfile extends AppCompatActivity {


    SessionManagement session;

    Button btnLogout;

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        session = new SessionManagement(getApplicationContext());

        btnLogout = (Button) findViewById(R.id.btnLogout);

        HashMap<String, String> user = session.getUserDetails();

        String name = user.get(SessionManagement.KEY_NAME);

        textView = (TextView) findViewById(R.id.textViewUserName);

        textView.setText(Html.fromHtml("Name: <b>" + name + "</b>"));

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                session.logoutUser();

            }
        });

    }

}