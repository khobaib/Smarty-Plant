package com.mistersmartyplants.utility;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SmartyPlantApplication extends Application {
    
    private static Context context;
    protected SharedPreferences User;
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        User = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }
    
    
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getAppContext() {
        return context;        
    }
    
    
    public void setFirstTime(Boolean firstTimeFlag){
        Editor editor = User.edit();
        editor.putBoolean(Constants.FIRST_TIME, firstTimeFlag);
        editor.commit();        
    }
    
    public void setCredentials(String email, String password){
        Editor editor = User.edit();
        editor.putString(Constants.USERNAME, email);
        editor.putString(Constants.PASSWORD, password);
        editor.commit();
    }
    
    public void setRememberMe(Boolean rememberMeFlag){
        Editor editor = User.edit();
        editor.putBoolean(Constants.REMEMBER_ME, rememberMeFlag);
        editor.commit();        
    }
    
    public void setAccessToken(String token){
    	GlobalState.getInstance().API_TOKEN = token;
        Editor editor = User.edit();
        editor.putString(Constants.ACCESS_TOKEN, token).commit();
        
    }
    
    
    public boolean isFirstTime(){
        Boolean firstTimeFlag = User.getBoolean(Constants.FIRST_TIME, true);
        return firstTimeFlag;
    }
    
    public String getUserName(){
        String email = User.getString(Constants.USERNAME, null);
        return email;
    }
    
    public String getPassword(){
        String pass = User.getString(Constants.PASSWORD, null);
        return pass;
    }
    
    public boolean isRememberMe(){
        Boolean rememberMeFlag = User.getBoolean(Constants.REMEMBER_ME, false);
        return rememberMeFlag;
    }
    
    public String getAccessToken(){
        String token = User.getString(Constants.ACCESS_TOKEN, null);
        return token;
    }

}
