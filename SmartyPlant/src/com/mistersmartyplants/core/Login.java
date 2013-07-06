package com.mistersmartyplants.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.SubMenu;
import com.bugsense.trace.BugSenseHandler;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
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
		BugSenseHandler.initAndStartSession(Login.this, "f2391cbb");
		super.onCreate(savedInstanceState);
		globalState.initActionBar(this, R.layout.login);
		appInstance = (SmartyPlantApplication) getApplication();
		jsonParser = new JsonParser();
		initFb(savedInstanceState);

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
				JSONObject requestObj = new JSONObject();
				try {
					requestObj.put("user_name", user_name);
					requestObj.put("password", password);
					String param = requestObj.toString();
					String[] params = {param};
					LoginTask task = new LoginTask();
					task.execute(params);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				

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

	private class LoginTask extends AsyncTask<String, Void, Boolean> {

		// boolean result;
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
		protected Boolean doInBackground(String... params) {

			try {
			
				String url = Constants.METHOD_LOGIN;

//				JSONObject requestObj = new JSONObject();
//				requestObj.put("user_name", user_name);
//				requestObj.put("password", password);
				String loginData = params[0];
				ServerResponse response = jsonParser
						.retrieveServerData(1, Constants.REQUEST_TYPE_POST,
								url, null, loginData, null);
				if (response.getStatus() == 200) {
					JSONObject responsObj = response.getjObj();
					String responseStatus = responsObj.optString("response");
					if (responseStatus.equalsIgnoreCase("success")) {
						String token = responsObj.optString("token");
						appInstance.setAccessToken(token);
					}
					return true;
				} else {

					return false;
				}

				// result = dataConnector.loginIn(user_name, password);
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

	private void initFb(Bundle savedInstanceState) {
	/*	Facebook facebook = new Facebook("625398507471708");
		String[] PERMISSIONS = {"email"};
		facebook.authorize(this, PERMISSIONS, null);*/
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
			}
			if (session == null) {
				OpenRequest op = new OpenRequest(this);
				op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
				op.setCallback(null);
				List<String> permissions = new ArrayList<String>();
				permissions.add("publish_stream");
				permissions.add("email");
				op.setPermissions(permissions);
				
				session = new Session(this);
				session.openForPublish(op);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this)
						.setCallback(statusCallback));
			}
		}

		Button fbLogin = (Button) findViewById(R.id.fb_login);
		fbLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Session session = Session.getActiveSession();
				if (!session.isOpened() && !session.isClosed()) {
					session.openForRead(new Session.OpenRequest(
							(Activity) mContext).setCallback(statusCallback));
				} else {
					Session.openActiveSession((Activity) mContext, true,
							statusCallback);
				}
			}
		});

	}
	
	
	//============ Social Login ===================
	//============ Facebook     ===================
	Session.StatusCallback statusCallback = new Session.StatusCallback() {
	    public void call(Session session, SessionState state, Exception exception) {
	        if (state.isOpened()) {
	            Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
	            	
	                public void onCompleted(GraphUser user, Response response) {
	                    if (response != null) {
	                        // do something with <response> now
	                        try{
	                        	String userID = user.getId();
	                        	String userName = user.getName();
	                        	String profileURL = user.getLink();
	                        	String provider = "facebook";
	                        	String email = (String) user.getProperty("email");
	                        	JSONObject obj = user.getInnerJSONObject();
	                        	
	                        	JSONObject requestObj = new JSONObject();
	                        	requestObj.put("user_name", null);
	                        	requestObj.put("password", null);
	                        	requestObj.put("provider_name", provider);
	                        	requestObj.put("identifier", profileURL);
	                        	requestObj.put("verified_email", email);
	                        	requestObj.put("photo", "");
	                        	requestObj.put("url", "");
	                        	requestObj.put("provider_id", "");
	                        	requestObj.put("preferred_user_name", userName);
	                        	
	                        	String[] params = {requestObj.toString()};
	                        	LoginTask task = new LoginTask();
	                        	task.execute(params);
	                        	
	                        	
	                        	
	                        	
	                        	
	                        Toast.makeText(mContext, user.getFirstName(), Toast.LENGTH_LONG).show();
	                        } catch(Exception e) {
	                             e.printStackTrace();
	                             Log.d("social_login", "Exception e");

	 	                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
	                         }
	                    }
	                    else{
                            Log.d("social_login", "response null");
 	                        Toast.makeText(mContext, "response null" , Toast.LENGTH_LONG).show();
                            

	                    }
	                }
	            });
	        }
	    }
	};

		
	class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// updateView();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,resultCode, data);
	
	}
	
	

}
