package com.mistersmartyplants.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
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
import com.facebook.model.GraphUser;
import com.mistersmartyplants.model.ServerResponse;
import com.mistersmartyplants.parser.JsonParser;
import com.mistersmartyplants.utility.AlertDialogManager;
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
	ProgressDialog fbLoginDialog = null;

	GlobalState globalState = GlobalState.getInstance();

	SmartyPlantApplication appInstance;
	JsonParser jsonParser;

	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BugSenseHandler.initAndStartSession(Login.this, "f2391cbb");
		super.onCreate(savedInstanceState);
		globalState.initActionBar(this, R.layout.login);
		appInstance = (SmartyPlantApplication) getApplication();
		jsonParser = new JsonParser();
		initFacebook(savedInstanceState);
		initTwitter();

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
					Log.d("smarty_login", param);
					String[] params = { param };
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
		
		TextView forgotPass = (TextView)findViewById(R.id.forget_pass);
		forgotPass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String url = "http://www.mistersmartyplants.com/Account/ChangePassword.aspx";
				 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	                startActivity(browserIntent);
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

				// JSONObject requestObj = new JSONObject();
				// requestObj.put("user_name", user_name);
				// requestObj.put("password", password);
				String loginData = params[0];
				ServerResponse response = jsonParser
						.retrieveServerData(1, Constants.REQUEST_TYPE_POST,
								url, null, loginData, null);
				if (response.getStatus() == 200) {
					JSONObject responsObj = response.getjObj();
					String responseStatus = responsObj.optString("response");
					if (responseStatus.equalsIgnoreCase("success")) {
						String token = responsObj.optString("token");
						String uid = responsObj.optString("uid");
						String uname = responsObj.optString("user_name");
						Log.d("smarty_uid", "name : "+uname + " uid : "+uid );
					
						appInstance.setAccessToken(token);
						return true;
					}
					else
						return false;
					
					
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
				String deviceModel = appInstance.getDeviceModel();
				if(deviceModel == null || deviceModel.equals(""))
					appInstance.setDeviceModel();
				Log.d("helal", appInstance.getDeviceModel());
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
		/*
		 * Facebook facebook = new Facebook("625398507471708"); String[]
		 * PERMISSIONS = {"email"}; facebook.authorize(this, PERMISSIONS, null);
		 */
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
			}
		}

		Button fbLogin = (Button) findViewById(R.id.fb_login);
		fbLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Session session = Session.getActiveSession();
				if (session == null) {
					OpenRequest op = new OpenRequest((Activity) mContext);
					op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
					op.setCallback(null);
					List<String> permissions = new ArrayList<String>();
					permissions.add("publish_stream");
					permissions.add("email");
					op.setPermissions(permissions);

					session = new Session(mContext);
					session.openForPublish(op);
					Session.setActiveSession(session);

				}
				if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
					session.openForRead(new Session.OpenRequest((Activity) mContext)
							.setCallback(statusCallback));
				}
				
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

	// ============ Social Login ===================
	// ============ Facebook ===================
	private void initFacebook(Bundle savedInstanceState){
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }
        Button fbLogin = (Button) findViewById(R.id.fb_login);
        fbLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String savedParams = appInstance.getFbLoginParams();
				if(savedParams!= null && !savedParams.equalsIgnoreCase(""))
				{
					String[] params = {savedParams};
					Log.d("smarty", savedParams);
					LoginTask task = new LoginTask();
					task.execute(params);
				}
				else
					onClickLogin();
			}
		});
	}
	
	private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
    }
	
	
	Session.StatusCallback statusCallback = new Session.StatusCallback() {

		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				
				fbLoginDialog = new ProgressDialog(mContext);
				fbLoginDialog.setTitle(" ");
				fbLoginDialog.setIcon(R.drawable.icon);
				fbLoginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				fbLoginDialog.setCancelable(false);
				fbLoginDialog.setMessage("Authenticating Facebook");
				fbLoginDialog.show();
				
				Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

							public void onCompleted(GraphUser user,Response response) {
								
								fbLoginDialog.dismiss();
								fbLoginDialog = null;
								
								if (response != null) {
									// do something with <response> now
									try {
										String userID = user.getId();
										String userName = user.getName();
										String profileURL = user.getLink();
										String provider = "facebook";
										String email = (String) user.getProperty("email");
										JSONObject obj = user.getInnerJSONObject();

										JSONObject requestObj = new JSONObject();
										requestObj.put("user_name", null);
										requestObj.put("password", null);
										requestObj.put("provider_name",provider);
										requestObj.put("identifier", profileURL);
										requestObj.put("verified_email", email);
										requestObj.put("photo", "");
										requestObj.put("url", "");
										requestObj.put("provider_id", "");
										requestObj.put("preferred_user_name",userName);
										
										appInstance.setFbLoginParams(requestObj.toString());
										String[] params = { requestObj.toString() };
										LoginTask task = new LoginTask();
										task.execute(params);

									} catch (Exception e) {
										e.printStackTrace();
										Log.d("social_login", "Exception e");

									}
								} else {
									Log.d("social_login", "response null");

								}
							}
						});
			}
		}
	};

	 @Override
	    public void onStart() {
	        super.onStart();
	        Session.getActiveSession().addCallback(statusCallback);
	    }

	    @Override
	    public void onStop() {
	        super.onStop();
	        Session.getActiveSession().removeCallback(statusCallback);
	    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,resultCode, data);

	}

	// ============================== Twitter ============================
	private void initTwitter() {
		Button twitterButton = (Button) findViewById(R.id.tw_login);
		twitterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String savedParams = appInstance.getTwLoginParams();
				if(savedParams!= null && !savedParams.equalsIgnoreCase(""))
				{
					String[] params = {savedParams};
					LoginTask task = new LoginTask();
					task.execute(params);
					return;
				}
				
				loginToTwitter();
			}
		});


		Uri uri = getIntent().getData();
		if (uri != null
				&& uri.toString().startsWith(Constants.TWITTER_CALLBACK_URL)) {
			TwitterloginTask task = new TwitterloginTask();
			task.execute();
		}

	}

	private String[] authenticateViaTwitter(Intent data) {
		Uri uri = data.getData();
		
		
		if (uri != null
				&& uri.toString().startsWith(Constants.TWITTER_CALLBACK_URL)) {
			String verifier = uri
					.getQueryParameter(Constants.URL_TWITTER_OAUTH_VERIFIER);
			try {
				AccessToken accessToken = globalState.twitter
						.getOAuthAccessToken(globalState.TwitterRequestToken,
								verifier);
				// store access token
				appInstance.setTwitterAccessToken(accessToken.getToken());
				appInstance.setTwitterAccessTokenSecret(accessToken
						.getTokenSecret());
				appInstance.setTwitterLoggedIn(true);
				long userID = accessToken.getUserId();
				User user = globalState.twitter.showUser(userID);
				String username = user.getName();
				userID = user.getId();
				String provider = "twitter";
				String email = user.getScreenName()+"@twitter.com";
				JSONObject requestObj = new JSONObject();
				requestObj.put("user_name", null);
				requestObj.put("password", null);
				requestObj.put("provider_name", provider);
				requestObj.put("identifier", userID);
				requestObj.put("verified_email", email);
				requestObj.put("photo", "");
				requestObj.put("url", "");
				requestObj.put("provider_id", "");
				requestObj.put("preferred_user_name", username);
				appInstance.setTwLoginParams(requestObj.toString());
				
				String[] params = { requestObj.toString() };
				return params;

			} catch (Exception e) {

				return null;
			}
		}
		return null;
	}

	private void loginToTwitter() {
		// Check if already logged in

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
		Configuration configuration = builder.build();
		TwitterFactory factory = new TwitterFactory(configuration);
		globalState.twitter = factory.getInstance();

		TwitterInitTask twitterLogin = new TwitterInitTask();
		twitterLogin.execute();

	}

	private class TwitterInitTask extends AsyncTask<Void, Void, Boolean> {
		Button twButton = (Button) findViewById(R.id.tw_login);
		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			twButton.setClickable(false);
			dialog = new ProgressDialog(mContext);
			dialog.setTitle(" ");
			dialog.setIcon(R.drawable.icon);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Loading Twitter");
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				globalState.TwitterRequestToken = globalState.twitter
						.getOAuthRequestToken(Constants.TWITTER_CALLBACK_URL);
			} catch (TwitterException e) {
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			try {
				dialog.dismiss();
				dialog = null;
			} catch (Exception e) {

			}
			twButton.setClickable(true);
			if (result) {
				Intent intent = (new Intent(Intent.ACTION_VIEW,
						Uri.parse(globalState.TwitterRequestToken
								.getAuthenticationURL())));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
				//finish();
				startActivity(intent);
			
				// startActivityForResult(new
				// Intent(Intent.ACTION_VIEW,Uri.parse(requestToken.getAuthenticationURL())),
				// 2);
			} else
				Toast.makeText(mContext, "Failed to login into Twitter",
						Toast.LENGTH_LONG).show();
		}
	}

	private class TwitterloginTask extends AsyncTask<Void, Void, String[]> {
		ProgressDialog dialog = null;
		Button twButton = (Button) findViewById(R.id.tw_login);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			twButton.setClickable(false);
			dialog = new ProgressDialog(mContext);
			dialog.setTitle(" ");
			dialog.setIcon(R.drawable.icon);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Authenticating Twitter");
			dialog.show();

		}

		@Override
		protected String[] doInBackground(Void... params) {

			return authenticateViaTwitter(getIntent());

		}

		@Override
		protected void onPostExecute(String[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			super.onPostExecute(result);
			try {
				dialog.dismiss();
				dialog = null;
			} catch (Exception e) {

			}
			twButton.setClickable(true);
			if (result != null) {
				LoginTask task = new LoginTask();
				task.execute(result);
			} else {
				// Toast.makeText(mContext, "Failed to Authenticate Twitter",
				// Toast.LENGTH_LONG).show();
			}
		}
	}

}
