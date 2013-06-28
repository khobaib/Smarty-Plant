package com.mistersmartyplants.utility;

public class Constants {
    
    public static final int REQUEST_TYPE_GET = 1;
    public static final int REQUEST_TYPE_POST = 2;
    
    public static final String FIRST_TIME = "first_time_run";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String REMEMBER_ME = "remember_me";
    
    //==== API METHODS =====
    public static final String API_URL = "http://api.mistersmartyplants.com/api/";
    public static final String METHOD_LOGIN = API_URL+"login";
    public static final String METHOD_REGISTER = API_URL+"registration";
    public static final String METHOD_PLANTS_UNSOLVED = API_URL+"plant/unsolved";
    public static final String METHOD_PLANTS_ALL_MINE = API_URL+"plant/allmine";
    public static final String METHOD_VOTE = API_URL+"vote";
    public static final String METHOD_UPLOAD = API_URL+"plant";


 
    
    public static final int RESPONSE_STATUS_CODE_SUCCESS = 200;

}
