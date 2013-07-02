package com.mistersmartyplants.core;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.bugsense.trace.BugSenseHandler;
import com.mistersmartyplants.model.ServerResponse;
import com.mistersmartyplants.parser.JsonParser;
import com.mistersmartyplants.utility.Constants;
import com.mistersmartyplants.utility.GlobalState;
import com.mistersmartyplants.utility.SmartyPlantApplication;

public class Register extends SherlockActivity {

	String username;
	String password;
	String email;
	EditText username_field;
	EditText password_field;
	EditText confirm_field;
	EditText email_field;
	Context mContext = this;
	SmartyPlantApplication appInstance;
	JsonParser jsonParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    BugSenseHandler.initAndStartSession(Register.this, "8e103589");

		super.onCreate(savedInstanceState);
		GlobalState.getInstance().initActionBar(mContext, R.layout.register);
		appInstance = (SmartyPlantApplication) getApplication();
		jsonParser = new JsonParser();

		username_field = (EditText) findViewById(R.id.user_name_field);
		email_field = (EditText) findViewById(R.id.email_field);
		password_field = (EditText) findViewById(R.id.password_field);
		confirm_field = (EditText) findViewById(R.id.confirm_password_field);

		ImageView create = (ImageView) findViewById(R.id.create);
		create.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				username = username_field.getEditableText().toString();
				email = email_field.getEditableText().toString();
				password = password_field.getEditableText().toString();
				String confirm = confirm_field.getEditableText().toString();

				if (!password.equals(confirm)) {
					Toast.makeText(
							mContext,
							"Password & Confirmation do no match, please retype password",
							3000).show();
				} else {
					try {
						RegTask task = new RegTask();
						task.execute();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

	}

	private class RegTask extends AsyncTask<Void, Void, Boolean> {

		ProgressDialog dialog = null;
		String msg="";
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setTitle(" ");
			dialog.setIcon(R.drawable.icon);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Registering ...");
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				String url = Constants.METHOD_REGISTER;
				JSONObject requestObj = new JSONObject();
				requestObj.put("user_name", username);
				requestObj.put("password", password);
				requestObj.put("email", email);
				String requestData = requestObj.toString();
				
				ServerResponse response = jsonParser
						.retrieveServerData(1,Constants.REQUEST_TYPE_POST, url,
								null, requestData, null);
				if (response.getStatus() == 200) {
					msg = response.getjObj().optString("response");
					if (msg.equalsIgnoreCase("success")){
						return true;
					}
					else
						return false;
				} else {
					return false;
				}
//				result = DataConnector.getInstance().register(username, email,
//						password);
			} catch (Exception e) {
				return false;
			}


		}

		@Override
		protected void onPostExecute(Boolean result) {
			try {
				dialog.dismiss();
			} catch (Exception e) {

			}
			if (result) {
				Toast.makeText(mContext, "Registered Successfully", 3000)
						.show();
				finish();
				startActivity(new Intent(mContext, Login.class));
			}

			else {
				Toast.makeText(mContext, "User already exists", 3000).show();
			}

			

		}

	}

}
