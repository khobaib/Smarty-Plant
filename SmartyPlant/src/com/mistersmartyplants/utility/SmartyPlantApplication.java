package com.mistersmartyplants.utility;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
        Editor editor = User.edit();
        editor.putString(Constants.ACCESS_TOKEN, token).commit();
        
    }
    
    public void setTwitterLoggedIn(Boolean isLoggedIn){
        Editor editor = User.edit();
        editor.putBoolean(Constants.TWITTER_IS_LOGGED_IN, isLoggedIn).commit();
        
    }
    
    public void setTwitterAccessToken(String token){
        Editor editor = User.edit();
        editor.putString(Constants.PREF_KEY_OAUTH_TOKEN, token).commit();
    }
    
    public void setTwitterAccessTokenSecret(String token){
        Editor editor = User.edit();
        editor.putString(Constants.PREF_KEY_OAUTH_SECRET, token).commit();
    }

    public void setFbLoginParams(String params){
    	Editor editor = User.edit();
        editor.putString(Constants.FB_LOGIN_PARAMS, params).commit();
    }
    
    public void setTwLoginParams(String params){
    	Editor editor = User.edit();
        editor.putString(Constants.TW_LOGIN_PARAMS, params).commit();
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
   
    public boolean isTwiiterLoggedIn(){
        Boolean isLogggedIn = User.getBoolean(Constants.TWITTER_IS_LOGGED_IN, false);
        return isLogggedIn;
    }
    
    public String getTwitterAccessToken(){
        String token = User.getString(Constants.PREF_KEY_OAUTH_TOKEN, null);
        return token;
    }
    
    public String getTwitterSecret(){
        String token = User.getString(Constants.PREF_KEY_OAUTH_SECRET, null);
        return token;
    }
    
    public String getFbLoginParams(){
    	String params = User.getString(Constants.FB_LOGIN_PARAMS, null);
        return params;
    }
    
    public String getTwLoginParams(){
    	String params = User.getString(Constants.TW_LOGIN_PARAMS, null);
        return params;
    }
    
    public void clearFbLoginParam(){
    	Editor editor = User.edit();
        editor.remove(Constants.FB_LOGIN_PARAMS).commit();
    }
    
    public void clearTwLoginParam(){
    	Editor editor = User.edit();
        editor.remove(Constants.TW_LOGIN_PARAMS).commit();
    }
    
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
  
          }
          return false;
    }

}
