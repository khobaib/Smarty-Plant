package com.mistersmartyplants.core;

import java.io.ByteArrayOutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.mistersmartyplants.adapter.GalleryAdapter;
import com.mistersmartyplants.model.ServerResponse;
import com.mistersmartyplants.parser.JsonParser;
import com.mistersmartyplants.utility.Constants;
import com.mistersmartyplants.utility.GlobalState;
import com.mistersmartyplants.utility.SmartyPlantApplication;

public class Submit extends Activity {
	Context mContext = this;
	String country;
	EditText countryField;

	String state;
	EditText stateField;

	String city;
	EditText cityField;

	String region;
	EditText regionField;

	String desc;
	EditText descField;
	ProgressDialog dialog = null;
	boolean requestResult = false;
	JsonParser jsonParser;
	SmartyPlantApplication appInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BugSenseHandler.initAndStartSession(mContext, "f2391cbb");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_form);
		jsonParser = new JsonParser();
		appInstance = (SmartyPlantApplication)getApplication();
		countryField = (EditText) findViewById(R.id.country_field);
		stateField = (EditText) findViewById(R.id.state_field);
		cityField = (EditText) findViewById(R.id.city_field);
		regionField = (EditText) findViewById(R.id.region_field);
		descField = (EditText) findViewById(R.id.desc_field);

		Gallery gallery = (Gallery) findViewById(R.id.gallery_view);
		GalleryAdapter gAdapter = new GalleryAdapter(mContext, 2);
		gallery.setAdapter(gAdapter);

		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				country = countryField.getEditableText().toString();
				if (country.equalsIgnoreCase("") || country == null) {
					Toast.makeText(mContext,
							"Country field can not be left empty", 3000).show();
				} else {
					state = stateField.getEditableText().toString();
					city = cityField.getEditableText().toString();
					region = regionField.getEditableText().toString();
					desc = descField.getEditableText().toString();
					UploadTask task = new UploadTask();
					task.execute();
				}
			}
		});
	}

	// ============= Background Task ========
	class UploadTask extends AsyncTask<Void, Void, JSONObject> {
		ProgressDialog dialog = null;

		// alertDialog = null;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setTitle(" ");
			dialog.setIcon(R.drawable.logo);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Uploading Mystery Plant");
			dialog.show();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			try {
				String url = Constants.METHOD_PLANT;
				
				
				JSONArray arr = new JSONArray();
				for (int i = 0; i < GlobalState.getInstance().currentBitmaps.size(); i++) {
					JSONObject json = new JSONObject();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					String path = GlobalState.getInstance().currentBitmaps.get(i);
					Bitmap bm = GlobalState.getInstance().bitmapFromPath(path);
					bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
					// bm.recycle();
					byte[] b = baos.toByteArray();
					baos.close();
					baos = null;

					String base64 = Base64.encodeToString(b, Base64.DEFAULT);
					json.put("base64String", base64.replaceAll("\n", ""));
					json.put("description", desc);
					json.put("country", country);
					json.put("state", state);
					json.put("city", city);
					json.put("region", region);
					arr.put(json);
				}
				String loginData = arr.toString();
				ServerResponse response = jsonParser
						.retrieveServerData(1, Constants.REQUEST_TYPE_POST,
								url, null, loginData, appInstance.getAccessToken());
				
				Log.d("vote", ""+ response.getStatus());
				Log.d("vote", response.getjObj().toString());
				

				if (response.getStatus() == 200 || response.getStatus() == 201) {
					return response.getjObj();
				} else {
					return null;
				}
				
//				result = DataConnector.getInstance().uploadImage(country,
//						state, city, region, desc);
		//		Log.d("<<<<>>>>", "result = " + result);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		protected void onPostExecute(JSONObject result) {
			try {
				dialog.dismiss();
			} catch (Exception e) {

			}
            Log.d("OnPostExec", "res: "+result);

			AlertDialog.Builder alertdialog = new AlertDialog.Builder(mContext);
			alertdialog.setIcon(R.drawable.logo);
			alertdialog.setTitle(" ");
			String msg = result.optString("response");
			
			if (msg.equalsIgnoreCase("success")) {
				alertdialog.setMessage("Plant Submitted Successfully");
				requestResult = true;
			}

			else {
				alertdialog.setMessage("Failed to Submit Plant");
				requestResult = false;
			}
			alertdialog.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							try{
							dialog.dismiss();
							}
							catch(Exception e ){
								
							}							finish();
							if (requestResult) {
								GlobalState.getInstance().currentBitmaps.clear();	
								startActivity(new Intent(mContext,
										HomeScreen.class));
							}
						}
					});
			alertdialog.setCancelable(true);
			alertdialog.create().show();

			if (msg.equalsIgnoreCase("success")) {
				GlobalState.getInstance().currentBitmaps.clear();
				finish();
				Intent i = new Intent(Submit.this, HomeScreen.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		};
	}

}
