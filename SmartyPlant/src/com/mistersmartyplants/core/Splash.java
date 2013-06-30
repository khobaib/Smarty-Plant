package com.mistersmartyplants.core;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.mistersmartyplants.model.ServerResponse;
import com.mistersmartyplants.parser.JsonParser;
import com.mistersmartyplants.utility.Constants;
import com.mistersmartyplants.utility.SmartyPlantApplication;

public class Splash extends Activity{
	
	SmartyPlantApplication appInstance;
	String user_name = "";
	String password = "";
	Context mContext = this;
	JsonParser jsonParser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		jsonParser = new JsonParser();
		appInstance = (SmartyPlantApplication)getApplication();

		if (appInstance.isRememberMe()) {
			user_name = appInstance.getUserName();
			password = appInstance.getPassword();
			LoginTask task = new LoginTask();
			task.execute();
		}
		else
		{
			Handler handler = new Handler();
			Runnable r = new Runnable() {
				@Override
				public void run() {
					finish();
					startActivity(new Intent(mContext, Login.class));
				}
			};
			handler.postDelayed(r, 2500);
			
		}
		
		
	}
	

	private class LoginTask extends AsyncTask<Void, Void, Boolean> {

		//boolean result;
		ProgressDialog dialog = null;
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setTitle(" ");

			dialog.setIcon(R.drawable.logo);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Logging In");
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				String url = Constants.METHOD_LOGIN;
				
				JSONObject requestObj = new JSONObject();
				requestObj.put("user_name", user_name);
				requestObj.put("password", password);
				String loginData = requestObj.toString();
				ServerResponse response = jsonParser.retrieveServerData(1,
						Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
				if (response.getStatus() == 200) {
					JSONObject responsObj = response.getjObj();
					String responseStatus = responsObj.optString("response");
					if (responseStatus.equalsIgnoreCase("success")){
						String token = responsObj.optString("token");
						appInstance.setAccessToken(token);
					}
					return true;
				}
				else {
					
					return false;
				}

				//result = dataConnector.loginIn(user_name, password);
				// result =true;
			} catch (Exception e) {

			return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {

			try {
				dialog.dismiss();
				dialog = null;
			} catch (Exception e) {

			}
			if (result) {
				finish();
				startActivity(new Intent(mContext, HomeScreen.class));
			} else {
				Toast.makeText(mContext, "Login Failed", 3000).show();
				finish();
				startActivity(new Intent(mContext, Login.class));

			}
		}

	}


}
