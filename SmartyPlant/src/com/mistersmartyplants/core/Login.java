package com.mistersmartyplants.core;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.SubMenu;
import com.bugsense.trace.BugSenseHandler;
import com.mistersmartyplants.model.ServerResponse;
import com.mistersmartyplants.parser.JsonParser;
import com.mistersmartyplants.utility.Constants;
import com.mistersmartyplants.utility.GlobalState;
import com.mistersmartyplants.utility.SmartyPlantApplication;

public class Login extends SherlockActivity {
	String user_name = "";
	String password = "";
	EditText user_name_field;
	EditText password_field;
	CheckBox remember_me;
	Context mContext = this;

	GlobalState globalState = GlobalState.getInstance();

	SmartyPlantApplication appInstance;
	JsonParser jsonParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BugSenseHandler.initAndStartSession(mContext, "f2391cbb");
		super.onCreate(savedInstanceState);
		globalState.initActionBar(this, R.layout.login);
		appInstance = (SmartyPlantApplication) getApplication();
		jsonParser = new JsonParser();

		user_name_field = (EditText) findViewById(R.id.user_name_field);
		password_field = (EditText) findViewById(R.id.password_field);
		remember_me = (CheckBox) findViewById(R.id.remember_me);
		
		final ImageView sign_in = (ImageView) findViewById(R.id.sign_in);
		sign_in.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				user_name = user_name_field.getEditableText().toString();
				password = password_field.getEditableText().toString();

				if (remember_me.isChecked()) {
					appInstance.setRememberMe(true);
					appInstance.setCredentials(user_name, password);

				} else {
					appInstance.setRememberMe(false);
					appInstance.setCredentials("", "");
				}

				LoginTask task = new LoginTask();
				task.execute();

			}
		});

		ImageView reg = (ImageView) findViewById(R.id.register);
		reg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(mContext, Register.class));

			}
		});

	}

	private class LoginTask extends AsyncTask<Void, Void, Boolean> {

		//boolean result;
		ProgressDialog dialog = null;
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setTitle(" ");
			dialog.setIcon(R.drawable.icon);
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
				Toast.makeText(mContext, "Username or Password Incorrect", 3000)
						.show();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		SubMenu subMenu1 = menu.addSubMenu("Register");

		subMenu1.setIcon(R.drawable.actionbar);
		com.actionbarsherlock.view.MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.actionbar);
		subMenu1Item
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		// getSupportMenuInflater().inflate(R.menu.menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		startActivity(new Intent(mContext, Register.class));
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		BugSenseHandler.closeSession(mContext);

	}

}
