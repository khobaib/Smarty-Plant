package com.mistersmartyplants.utility;

public class Constants {
    
    public static final int REQUEST_TYPE_GET = 1;
    public static final int REQUEST_TYPE_POST = 2;
    
    public static final String FIRST_TIME = "first_time_run";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String USERNAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String REMEMBER_ME = "remember_me";
	public static final String PREFS_NAME = "MyPrefsFile";

    //==== API METHODS =====
    public static final String API_URL = "http://api.mistersmartyplants.com/api/";
    public static final String METHOD_LOGIN = API_URL+"login";
    public static final String METHOD_REGISTER = API_URL+"registration";
    public static final String METHOD_PLANTS_UNSOLVED = API_URL+"plant/unsolved";
    public static final String METHOD_PLANTS_ALL_MINE = API_URL+"plant/allmine";
    public static final String METHOD_VOTE = API_URL+"vote";
    public static final String METHOD_PLANT = API_URL+"plant";
    public static final String METHOD_SINGLE_PLANT = API_URL+"singleplant";
    public static final int RESPONSE_STATUS_CODE_SUCCESS = 200;
    
    //==== Twitter API =====
    public static String TWITTER_CONSUMER_KEY = "fKjCEIFtih9CgBQELuxD7A";
    public static String TWITTER_CONSUMER_SECRET = "e0mLYheNSTcREuq9VeqJ3Lp45TpEkoSuap5CtWiAgo";
    public static String TWITTER_PREFERENCE_NAME = "twitter_oauth";
    public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
    public static final String TWITTER_CALLBACK_URL = "oauth://smartyplants";
    public static final String URL_TWITTER_AUTH = "auth_url";
    public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    public static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    public static final String TWITTER_IS_LOGGED_IN = "twitter_logged_in";
    public static final String TW_LOGIN_PARAMS = "tw_login_params";
    
    
    //==== Fb API ====
    public static final String FB_LOGIN_PARAMS = "fb_login_params";
    
    
}
