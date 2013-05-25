package smartyplant.Network;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smartyplant.Utils.GlobalState;
import smartyplant.modules.Plant;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class DataConnector {

	private static DataConnector instance = new DataConnector();
	private String API_URL = "http://api.mistersmartyplants.com/api/";

	public static DataConnector getInstance() {
		return instance;
	}

	public boolean loginIn(String username, String password) throws Exception {
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(API_URL + "/login");

		JSONObject json = new JSONObject();
		json.put("user_name", username);
		json.put("password", password);
		json.put("social_token", null);

		StringEntity se = new StringEntity(json.toString());
		httppost.setEntity(se);
		httppost.setHeader("Content-Type", "application/json");

		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			result = convertStreamToString(instream);
			instream.close();
		}
		JSONObject object = new JSONObject(result);
		String token = object.optString("token");
		if (!token.equals("") && !token.equals("null") && token != null) {
			GlobalState.getInstance().API_TOKEN = object.getString("token");
			return true;
		}

		else
			return false;
	}

	public int register(String user_name, String email, String password)
			throws Exception {
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(API_URL + "registration");
		JSONObject json = new JSONObject();
		json.put("user_name", user_name);
		json.put("email", email);
		json.put("password", password);
		httppost.setEntity(new StringEntity(json.toString()));
		httppost.setHeader("Content-Type", "application/json");

		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			result = convertStreamToString(instream);
			instream.close();
		}
		if (result.contains("Success"))
			return 1;
		else if (result.contains("User"))
			return 2;
		else
			return 3;
	}

	public ArrayList<Plant> getPlants(String type) throws Exception {
		ArrayList<Plant> plants = new ArrayList<Plant>();
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(API_URL + "//plant/" + type);

		httpget.setHeader("Authorization-Token",
				GlobalState.getInstance().API_TOKEN);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			result = convertStreamToString(instream);
			instream.close();
		}
		JSONArray arr = new JSONArray(result);
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			Plant p = new Plant();
			p.plant_id = obj.getInt("plant_id");
			p.plant_name = obj.getString("plant_name");
			p.image_url = "http://mistersmartyplants.com"
					+ obj.getString("image_url").replaceAll("~", "");
			// p.image_drawable = drawableFromUrl(p.image_url);

			p.identifier_name = obj.getString("identifier_name");
			p.identifier_twitter_url = obj.getString("identifier_twitter_url");
			p.identifier_picture_url = "http://mistersmartyplants.com"
					+ obj.getString("identifier_picture_url").substring(2);
			// p.identifier_picture_drawable =
			// drawableFromUrl(p.identifier_picture_url);
			String num = obj.getString("plant_name_agree_percentage")
					.replaceAll("%", "");

			int prc = Integer.parseInt(num);
			p.plant_name_agree_prc = prc;
			plants.add(p);
		}

		return plants;
	}

	public String uploadImage(String country, String state,
			String city, String region, String desc) throws Exception {

		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(API_URL + "//plant");

		JSONArray arr = new JSONArray();
		for (int i = 0; i < GlobalState.getInstance().currentBitmaps.size(); i++) {
			JSONObject json = new JSONObject();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			Bitmap bm = GlobalState.getInstance().currentBitmaps.get(i);	
			bm.compress(Bitmap.CompressFormat.PNG, 90, baos); 
			byte[] b = baos.toByteArray();
			String base64 = Base64.encodeToString(b, Base64.DEFAULT);
			json.put("base64String", base64.replaceAll("\n", ""));
			json.put("description", desc);
			json.put("country", country);
			json.put("state", state);
			json.put("city", city);
			json.put("region", region);
			arr.put(json);
		}
		StringEntity se = new StringEntity(arr.toString());
		httppost.setEntity(se);

		httppost.setHeader("Authorization-Token",
				GlobalState.getInstance().API_TOKEN);
		httppost.setHeader("Content-Type", "application/json");

		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			result = convertStreamToString(instream);
			instream.close();
		}
		return result;

	}

	
	public String voteForPlant(String name, int id) throws Exception{
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(API_URL + "vote");

			JSONObject json = new JSONObject();
			json.put("plant_id", id);
			json.put("plant_name", name);
			json.put("ip_address", "127.0.0.1");
		
		StringEntity se = new StringEntity(json.toString());
		httppost.setEntity(se);

		httppost.setHeader("Authorization-Token",
				GlobalState.getInstance().API_TOKEN);
		httppost.setHeader("Content-Type", "application/json");

		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			result = convertStreamToString(instream);
			instream.close();
		}
		return result;

	}
	// ====
	public String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public Drawable drawableFromUrl(String url) {
		try {
			Bitmap x;
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			connection.connect();
			InputStream input = connection.getInputStream();
			x = BitmapFactory.decodeStream(input);
			return new BitmapDrawable(x);

		} catch (Exception e) {
			return null;
		}
	}
	
	

}
