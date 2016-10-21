package com.presquare.studios.presentsir;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManagement {


    SharedPreferences pref;


    Editor editor;


    Context _context;


    int PRIVATE_MODE = 0;


    private static final String PREF_NAME = "PresentSirPref";

    public static final String KEY_NAME = "name";

    public static final String KEY_SCHOOL = "school";

    public static final String KEY_BRANCH = "branch";

    public static final String KEY_ROUTE = "route";

    public static final String KEY_RANDOM = "random";

    public static final String PHONE = "New";

    public static final String ONETIMEPASSWORD = "No";

    public static final String SELECT = "Nope";

    public static final String ROUTE = "Num";

    public static final String UPLOAD = "Na";

    public static final String DASHBOARD = "Nah";

    public static final String MOB_NUM = "mobnum";

    public static final String OTP = "OTP";



    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void createLoginSession(String mobile){

        editor.putBoolean(PHONE, true);

        editor.putString(KEY_NAME, mobile);

        editor.putString(MOB_NUM, mobile);

        editor.commit();

    }



    public void createNumberSession(String mobile, String otp){

        editor.putString(MOB_NUM, mobile);

        editor.putString(OTP, otp);

        editor.putBoolean(PHONE, false);

        editor.putBoolean(ONETIMEPASSWORD, true);

        editor.commit();

    }



    public void createOTPSession(){

        editor.putBoolean(ONETIMEPASSWORD, false);

        editor.putBoolean(SELECT, true);

        editor.commit();

    }



    public void createSelectSession(){

        editor.putBoolean(SELECT, false);

        editor.putBoolean(ROUTE, true);

        editor.commit();

    }



    public void createRouteSession(){

        editor.putBoolean(ROUTE, false);

        editor.putBoolean(UPLOAD, true);

        editor.commit();

    }



    public void createUploadSession(){

        editor.putBoolean(UPLOAD, false);

        editor.putBoolean(DASHBOARD, true);

        editor.commit();

    }




    public void phone(){

        if(this.phoneIn()){

            Intent i = new Intent(_context, PhoneNumber.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

        }


    }



    public void otp(){

        if(this.otpIn()){

            Intent i = new Intent(_context, OTP.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

        }


    }



    public void select(){

        if(this.selectIn()){

            Intent i = new Intent(_context, Select.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

        }


    }



    public void route(){

        if(this.routeIn()){

            Intent i = new Intent(_context, Route.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

        }


    }



    public void upload(){

        if(this.uploadIn()){

            Intent i = new Intent(_context, Upload.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

        }


    }



    public void dashboard(){

        if(this.dashboardIn()){

            Intent i = new Intent(_context, Dashboard.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

        }


    }



    public void resentotpsave(String otp) {

        editor.putString(OTP, otp);

        editor.commit();

    }



    public void SelectSession(String school, String branch){

        editor.putString(KEY_SCHOOL, school);

        editor.putString(KEY_BRANCH, branch);

        editor.commit();

    }



    public void RouteSession(String pass){

        editor.putString(KEY_ROUTE, pass);

        editor.commit();

    }



    public void UploadSession(String random){

        editor.putString(KEY_RANDOM, random);

        editor.commit();

    }



    public HashMap<String, String> getUserDetails(){

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        return user;

    }



    public HashMap<String, String> getSelectDetails(){

        HashMap<String, String> select = new HashMap<String, String>();

        select.put(KEY_SCHOOL, pref.getString(KEY_SCHOOL, null));

        select.put(KEY_BRANCH, pref.getString(KEY_BRANCH, null));

        return select;

    }



    public HashMap<String, String> getUploadDetails(){

        HashMap<String, String> select = new HashMap<String, String>();

        select.put(KEY_RANDOM, pref.getString(KEY_RANDOM, null));

        return select;

    }




    public HashMap<String, String> getMobileDetails(){

        HashMap<String, String> mobile = new HashMap<String, String>();

        mobile.put(MOB_NUM, pref.getString(MOB_NUM, null));

        return mobile;

    }



    public HashMap<String, String> getOTPDetails(){

        HashMap<String, String> otp = new HashMap<String, String>();

        otp.put(OTP, pref.getString(OTP, null));

        return otp;

    }



    public void logoutUser(){

        editor.clear();

        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);

    }



    public boolean phoneIn() {return pref.getBoolean(PHONE, false);}

    public boolean otpIn(){
        return pref.getBoolean(ONETIMEPASSWORD, false);
    }

    public boolean selectIn(){
        return pref.getBoolean(SELECT, false);
    }

    public boolean routeIn(){
        return pref.getBoolean(ROUTE, false);
    }

    public boolean uploadIn(){
        return pref.getBoolean(UPLOAD, false);
    }

    public boolean dashboardIn(){
        return pref.getBoolean(DASHBOARD, false);
    }

}