package com.mistersmartyplants.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mistersmartyplants.model.ServerResponse;
import com.mistersmartyplants.utility.Constants;

public class JsonParser {

    static InputStream          is   = null;
    static JSONObject           jObj = null;
    static JSONArray           jArr = null;

    static String               json = "";

    private static final String TAG  = JsonParser.class.getSimpleName();

    // constructor
    public JsonParser() {

    }

    public ServerResponse retrieveServerData(int resposeType, int reqType, String url, List<NameValuePair> urlParams, String content, String appToken) {
        Log.d(TAG, "in retrieveServerData method");
        Log.d("APPTOKEN", "token = " + appToken);           // debug flag added, dont remove it.

        int status = 0;

//        String editTokenVal = null;
        StringBuilder sb = null;
        //        if (appToken != null) {
        //            sb = new StringBuilder();
        //            sb.append("token:\"" + appToken);
        //            editTokenVal = sb.toString();
        //            Log.d(TAG, "string token in header = " + editTokenVal);
        //        }

        if (urlParams != null) {
            String paramString = URLEncodedUtils.format(urlParams, "utf-8");
            url += "?" + paramString;            
        }
        Log.d(TAG, "GET url = " + url);

        // Making HTTP request
        try {
            // request method is GET
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;

            if(reqType == Constants.REQUEST_TYPE_GET){
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Content-Type", "application/json");
                httpGet.setHeader("Accept", "application/json");
                if (appToken != null){
                    httpGet.setHeader("Authorization-Token", appToken);
                }
                //                    httpGet.setHeader("Authorization", editTokenVal);

                httpResponse = httpClient.execute(httpGet);

            } else if(reqType == Constants.REQUEST_TYPE_POST){
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept", "application/json");
                if (appToken != null){
                    httpPost.setHeader("Authorization-Token", appToken);
                }
                //                    httpPost.setHeader("Authorization", editTokenVal);
                if(content != null)
                {
                StringEntity se = new StringEntity(content);
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(se);
                }
                httpResponse = httpClient.execute(httpPost);                
            }

            status = httpResponse.getStatusLine().getStatusCode();
            Log.d(TAG, "STAUS = " + status);

            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Log.d(TAG, "trying to read input stream.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            Log.d(TAG, "sb = " + sb.toString());
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
        	switch (resposeType) {
			case 1:
	            jObj = new JSONObject(json);
	            return new ServerResponse(jObj, status);
				
			case 2:
				jArr = new JSONArray(json);
	            return new ServerResponse(jArr, status);
			
			case 3:
				return new ServerResponse(json, status);
				
			default:
				break;
			}
        } catch (JSONException e) {
        	Log.e("JSON Parser", "Error parsing data " + e.toString());

            
        }
    	return null;


        // return ServerResponse
    }
}
