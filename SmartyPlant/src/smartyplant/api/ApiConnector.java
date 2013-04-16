package smartyplant.api;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;


public class ApiConnector {
	
	// Using Singleton Design Pattern
	private static ApiConnector instance = new ApiConnector();
	private String API_URL = "";
	
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
	
	public Bitmap downloadImage(String url) throws Exception{
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
	    HttpGet httpget = new HttpGet(API_URL);
	    
	    HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            result= Utils.getInstance().convertStreamToString(instream);
            instream.close();
        }
	    
        return Utils.getInstance().decodeImage(result);
		
	}
	
	public boolean loginIn(String username, String password) throws Exception{
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(API_URL);
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    nameValuePairs.add(new BasicNameValuePair("username", username));
	    nameValuePairs.add(new BasicNameValuePair("password", password));
	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    
	    
	    HttpResponse response = httpclient.execute(httppost);
	    
	    
	    // According to API read response & return true/false 
		return true;
	}
	
	public void logout(){
		// Implement according to API
	}
	
	public boolean register(Map map) throws Exception{
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(API_URL);
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    nameValuePairs.add(new BasicNameValuePair("username", (String) map.get("username")));
	    nameValuePairs.add(new BasicNameValuePair("password", (String) map.get("password")));
	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    
	    
	    HttpResponse response = httpclient.execute(httppost);
	    
	    
	    // According to API read response & return true/false 
		return true;
	}
	
	

}
