package com.mistersmartyplants.socialnetwork;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mistersmartyplants.core.R;
import com.mistersmartyplants.utility.Constants;
import com.mistersmartyplants.utility.SmartyPlantApplication;

public class TwitterHelper {
	
	SmartyPlantApplication appInstance;
	Twitter twitter;
	RequestToken twitterRequestToken;

	
	public TwitterHelper(SmartyPlantApplication app) {
		appInstance = app;
	}
	
	private class initTwitterTask extends AsyncTask<Void, Void, Boolean>
	{
		ProgressDialog dialog = null;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(appInstance.getApplicationContext());
			dialog.setTitle(" ");
			dialog.setIcon(R.drawable.icon);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Authenticating Twitter");
			dialog.show();
			
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();
			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				twitterRequestToken = twitter.getOAuthRequestToken(Constants.TWITTER_CALLBACK_URL);
				if(twitterRequestToken != null)
					return true;
				else
					return false;
				
			} catch (TwitterException e) {
				return false;
			}			
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result)
			{
				// launch authentication in webview
				appInstance.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(twitterRequestToken.getAuthenticationURL())));
			}
			else
			{
				Toast.makeText(appInstance, "Failed to authenticate twitter", Toast.LENGTH_LONG).show();
			}
		}
	}

}
