package smartyplant.api;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import smartyplant.modules.Plant;

import android.graphics.Bitmap;
import android.os.AsyncTask;


public class ApiConnector {
	
	// Using Singleton Design Pattern
	private static ApiConnector instance = new ApiConnector();
	private static HttpConnector httpConnector = instance.new HttpConnector();
	private String API_URL = "http://api.mistersmartyplants.com/api/";
	
	public static ApiConnector getInstance(){
		if (instance != null)
			return instance;
		else
			return new ApiConnector();
	}
	
	
	//================== API Functions ===============
	
	
	public boolean uploadImage(File image, String description) throws Exception{
		String encodedImage = Utils.getInstance().encodeImage(image);
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(API_URL);
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    nameValuePairs.add(new BasicNameValuePair("encodedImage", encodedImage));
	    nameValuePairs.add(new BasicNameValuePair("description", description));
	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    
	    
	    HttpResponse response = httpclient.execute(httppost);
	    
	    // According to API read response & return true/false 
		return true;
	}
	
	public ArrayList<Plant> getAllPlants() throws Exception{
		ArrayList<Plant> plants = new ArrayList<Plant>();
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
	    HttpGet httpget = new HttpGet(API_URL+"//showPlants&FLAG=ALL");
	  
	    HttpResponse response = httpclient.execute(httpget);
	    HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            result= Utils.getInstance().convertStreamToString(instream);
            instream.close();
        }
        JSONObject object = new JSONObject(result);
        JSONArray plants_arr = object.getJSONArray("plants");
        for (int i = 0 ; i < plants_arr.length() ; i ++){
        	JSONObject plant_obj = plants_arr.getJSONObject(i);
        	Plant p = new Plant();
        	p.plant_id = plant_obj.getInt("plant_id");
        	p.plant_name = plant_obj.getString("plant_name");
        	p.image_url = plant_obj.getString("image_url");
        	p.identifier_name = plant_obj.getString("identifier_name");
        	p.identifier_twitter_url = plant_obj.getString("identifier_twitter_url");
        	p.identifier_picture_url = plant_obj.getString("identifier_picture_url");
        	p.plant_name_agree_percentage = plant_obj.getInt("plant_name_agree_percentage");
        	
        	plants.add(p);
        }
        return plants;
        
        
		
	}
	
	
	
	
	public void API_login(String username, String password){
		 httpConnector.execute(new Object[]{1,username, password});
		 
	}
	private String loginIn(String username, String password) throws Exception{
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(API_URL+"//login");
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	    nameValuePairs.add(new BasicNameValuePair("user_name", username));
	    nameValuePairs.add(new BasicNameValuePair("password", password));
	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    //httppost.setHeader("Authorization-Token", "");
	    
	    HttpResponse response = httpclient.execute(httppost);
	    HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            result= Utils.getInstance().convertStreamToString(instream);
            instream.close();
        }
	    JSONObject object = new JSONObject(result);
	    if(object.get("uid") != null){
	    	Utils.getInstance().API_TOKEN = object.getString("token");
	    	return Utils.getInstance().API_TOKEN;
	    }
	    	
	    else
	    	return "NULL";
	}
	
	
	public void API_reg(String user_name, String email, String password){
		httpConnector.execute(new Object[]{2,user_name,email, password});
	}
	
	public String register(String user_name, String email, String password) throws Exception{
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(API_URL+"//registration");
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	    nameValuePairs.add(new BasicNameValuePair("user_name", user_name));
	    nameValuePairs.add(new BasicNameValuePair("email", email));
	    nameValuePairs.add(new BasicNameValuePair("password", password));
	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    
	    HttpResponse response = httpclient.execute(httppost);
	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
            InputStream instream = entity.getContent();
            result= Utils.getInstance().convertStreamToString(instream);
            instream.close();
        }
	    JSONObject object = new JSONObject(result);
	    if(object.get("uid") != null)
	    	return "YES";
	    else
	    	return "NULL";
	}
	
	
	private class HttpConnector extends AsyncTask<Object, Void, String>{

		@Override
		protected String doInBackground(Object... params) {
			
			int functionID = (Integer)params[0];
			String param1 = (String)params[1];
			String param2 = (String)params[2];
			
			switch (functionID) {
			case 1:
				//Login
				try {
					return loginIn(param1, param2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 2:
				//Register
				try {
					return register(param1, param2, (String)params[3]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
				
			default:
				break;
			}
			
			return null;
		}
		
	}
	

}
