package com.presquare.studios.presentsir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    private Toolbar bar;

    private Button present;

    private Button absent;

    private TextView date;

    private EditText institution;

    private EditText route;

    private EditText busnumber;

    private EditText expected;

    private EditText eta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        bar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(bar);

        getSupportActionBar().setTitle("Rishabh Singh");

        absent = (Button) findViewById(R.id.absent);

        absent.setOnClickListener(this);

        present = (Button) findViewById(R.id.present);

        present.setOnClickListener(this);

        date = (TextView) findViewById(R.id.date);

        institution = (EditText) findViewById(R.id.institution);

        route = (EditText) findViewById(R.id.route);

        busnumber = (EditText) findViewById(R.id.busnumber);

        expected = (EditText) findViewById(R.id.expected);

        eta = (EditText) findViewById(R.id.eta);

        institution.setEnabled(false);

        route.setEnabled(false);

        busnumber.setEnabled(false);

        expected.setEnabled(false);

        eta.setEnabled(false);

        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());

        date.setText(s);


    }

    public void onClick(View view) {

        if (view == present) {

            Toast.makeText(this,"You have been marked Present",Toast.LENGTH_SHORT).show();

        }

        if (view == absent) {

            Toast.makeText(this,"You have been marked Absent",Toast.LENGTH_SHORT).show();

        }

    }
}