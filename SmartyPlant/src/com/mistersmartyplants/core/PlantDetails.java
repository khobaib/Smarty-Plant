package com.mistersmartyplants.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.mistersmartyplants.lazylist.ImageLoader;
import com.mistersmartyplants.model.DetailedPlant;
import com.mistersmartyplants.model.ServerResponse;
import com.mistersmartyplants.model.User;
import com.mistersmartyplants.model.Vote;
import com.mistersmartyplants.parser.JsonParser;
import com.mistersmartyplants.utility.Constants;
import com.mistersmartyplants.utility.GlobalState;
import com.mistersmartyplants.utility.SmartyPlantApplication;

public class PlantDetails extends Activity {
	Context mContext = this;
	GlobalState globalState = GlobalState.getInstance();
	String voted_name = "";
	DetailedPlant detailedPlant;
	ImageLoader lazyLoader;
	JsonParser jsonParser;
	SmartyPlantApplication appInstance;

	Bitmap bMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(PlantDetails.this, "8e103589");
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.plant_details);
		appInstance = (SmartyPlantApplication) getApplication();
		jsonParser = new JsonParser();
		lazyLoader = new ImageLoader(mContext);
		LoadDataTask loadData = new LoadDataTask();
		loadData.execute();

		Button prev = (Button) findViewById(R.id.prev);
		prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (globalState.currentIndex > 0) {
					globalState.currentIndex--;
					globalState.currentPlant = globalState.all_plants
							.get(globalState.currentIndex);
					finish();
					startActivity(new Intent(mContext, PlantDetails.class));
					overridePendingTransition(android.R.anim.slide_in_left,
							android.R.anim.slide_out_right);
				} else {
					globalState.currentIndex = globalState.all_plants.size() - 1;
					globalState.currentPlant = globalState.all_plants
							.get(globalState.currentIndex);
					finish();
					startActivity(new Intent(mContext, PlantDetails.class));
				}
			}
		});

		Button next = (Button) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (globalState.currentIndex < globalState.all_plants.size() - 1) {
					globalState.currentIndex++;
					globalState.currentPlant = globalState.all_plants
							.get(globalState.currentIndex);
					finish();
					startActivity(new Intent(mContext, PlantDetails.class));
				} else {
					globalState.currentIndex = 0;
					globalState.currentPlant = globalState.all_plants
							.get(globalState.currentIndex);
					finish();
					startActivity(new Intent(mContext, PlantDetails.class));
				}
			}
		});

	}

	private void initUI() {

		ImageView image = (ImageView) findViewById(R.id.image_view);
		// if(bMap != null)
		// bMap.recycle();
		bMap = lazyLoader.getBitmap(detailedPlant.imageUrls.get(0), 1000);
		image.setImageBitmap(bMap);
		// lazyLoader.DisplayImage(detailedPlant.imageUrls.get(0), image);

		initVotePanel();
		initMiniGallery();
	}

	private void initMiniGallery() {
		LinearLayout miniGallery = (LinearLayout) findViewById(R.id.mini_gallery);
		for (int i = 0; i < detailedPlant.imageUrls.size(); i++) {

			ImageView imgV = new ImageView(mContext);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 5, 5, 5);
			imgV.setLayoutParams(params);

			if (i == 0)
				imgV.setBackgroundResource(R.drawable.rounded_text);
			else
				imgV.setBackgroundResource(R.drawable.rounded_gray);

			// if(bMap != null)
			// bMap.recycle();
			bMap = lazyLoader.getBitmap(detailedPlant.imageUrls.get(i), 1000);
			imgV.setImageBitmap(bMap);
			// lazyLoader.DisplayImage(detailedPlant.imageUrls.get(i), imgV);
			imgV.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ImageView miniItem = (ImageView) v;
					ImageView mainImageView = (ImageView) findViewById(R.id.image_view);
					mainImageView.setImageDrawable(miniItem.getDrawable());

					LinearLayout miniGallery = (LinearLayout) findViewById(R.id.mini_gallery);

					for (int j = 0; j < miniGallery.getChildCount(); j++) {
						ImageView view = (ImageView) miniGallery.getChildAt(j);
						view.setBackgroundResource(R.drawable.rounded_gray);
					}
					miniItem.setBackgroundResource(R.drawable.rounded_text);

				}
			});
			miniGallery.addView(imgV);
		}
		if (detailedPlant.imageUrls.size() <= 1) {
			miniGallery.setVisibility(LinearLayout.INVISIBLE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) { // Back key pressed
			Intent i = new Intent(PlantDetails.this, HomeScreen.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private RelativeLayout setUpSingleVotePanel(String userName,
			String plantName, int prc, String googleURL, int visibility,
			Boolean agreeBtnVisable) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		RelativeLayout votePanel = (RelativeLayout) inflater.inflate(
				R.layout.cusotm_vote_panel, null);

		TextView userNameLabel = (TextView) votePanel.getChildAt(0);
		userNameLabel.setText(userName);
		userNameLabel.setVisibility(visibility);

		TextView calledThis = (TextView) votePanel.getChildAt(1);
		calledThis.setVisibility(visibility);

		TextView plantNameLabel = (TextView) votePanel.getChildAt(2);
		plantNameLabel.setText(plantName);
		plantNameLabel.setVisibility(visibility);

		TextView percentLabel = (TextView) votePanel.getChildAt(3);
		percentLabel.setText(prc + "% agreed");

		ProgressBar progressBar = (ProgressBar) votePanel.getChildAt(4);
		progressBar.setProgress(prc);

		RelativeLayout buttonGroup = (RelativeLayout) votePanel.getChildAt(5);

		Button agreeButton = (Button) buttonGroup.getChildAt(0);
		agreeButton.setVisibility(visibility);

		Button disAgreeButton = (Button) buttonGroup.getChildAt(1);
		if (visibility == Button.GONE)
			disAgreeButton.setText("Name It");
		final String votedName = plantName;

		if (!agreeBtnVisable) {
			agreeButton.setVisibility(Button.INVISIBLE);
			disAgreeButton.setVisibility(Button.INVISIBLE);
		}
		agreeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				voted_name = votedName;
				VoteTask task = new VoteTask();
				task.execute();
			}
		});

		// more details -- Google Search on Plant
		TextView moreDetails = (TextView) buttonGroup.getChildAt(3);

		moreDetails.setText(plantName);
		final String url = googleURL;

		moreDetails.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url));
				startActivity(browserIntent);
			}
		});

		disAgreeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(mContext);
				dialog.setTitle("Mister Smarty Plants");
				LayoutInflater inflater = getLayoutInflater();
				final View view = inflater.inflate(R.layout.custom_vote_dialog,
						null);
				dialog.setContentView(view);
				View ok = view.findViewById(R.id.ok);
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						TextView tv = (TextView) view
								.findViewById(R.id.voted_named);
						voted_name = tv.getEditableText().toString();
						if (voted_name.equals("") || voted_name == null) {
							try {
								dialog.dismiss();
							} catch (Exception e) {

							}
							Toast.makeText(mContext,
									"Plant name can not be emtpy !",
									Toast.LENGTH_LONG).show();
						} else {
							VoteTask task = new VoteTask();
							task.execute();
							try {
								dialog.dismiss();
							} catch (Exception e) {

							}
						}
					}
				});
				dialog.show();
			}
		});
		return votePanel;
	}

	private void initVotePanel() {
		LinearLayout votingList = (LinearLayout) findViewById(R.id.voting_list);
		votingList.removeAllViews();

		if (detailedPlant.votes.size() == 0) {

			RelativeLayout votePanel = setUpSingleVotePanel(
					detailedPlant.identifier_name, detailedPlant.plant_name,
					detailedPlant.plant_name_agree_prc, "", Button.GONE, true);
			votingList.addView(votePanel);
		} else
			for (int i = 0; i < detailedPlant.votes.size(); i++) {
				Vote vote = detailedPlant.votes.get(i);
				RelativeLayout votePanel = setUpSingleVotePanel(vote.userNames,
						vote.plantName, vote.agreePercentage,
						vote.plantGoogleUrl, Button.VISIBLE,
						vote.agreeButtonVisible);
				votingList.addView(votePanel);
			}
	}

	// ============= Background Task ========
	class VoteTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog = null;

		// String message = "";

		// alertDialog = null;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setTitle(" ");
			dialog.setIcon(R.drawable.icon);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Sending Request ...");
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {

				String url = Constants.METHOD_VOTE;

				JSONObject requestObj = new JSONObject();
				requestObj.put("plant_id", globalState.currentPlant.plant_id);
				requestObj.put("plant_name", voted_name);
				requestObj.put("ip_address", "127.0.0.1");
				String loginData = requestObj.toString();
				ServerResponse response = jsonParser.retrieveServerData(1,
						Constants.REQUEST_TYPE_POST, url, null, loginData,
						appInstance.getAccessToken());
				if (response.getStatus() == 200) {
					JSONObject obj = response.getjObj();
					String message = obj.optString("response");
					if (message.equalsIgnoreCase("success")) {
						JSONArray votes = obj.optJSONArray("vote_details");
						detailedPlant.votes.clear();
						for (int i = 0; i < votes.length(); i++) {
							Vote v = new Vote();
							JSONObject voteObj = votes.getJSONObject(i);
							v.agreeButtonVisible = voteObj
									.optBoolean("agreeButtonVisible");
							v.agreePercentage = voteObj.optInt("agreePercent");
							v.isSolved = voteObj.optBoolean("isSolved");
							v.plantGoogleUrl = voteObj
									.optString("plantGoogleURL");
							v.plantId = voteObj.optInt("plantId");
							v.plantName = voteObj.optString("plantName");
							v.userNames = voteObj.optString("userNames");
							JSONArray users = voteObj.optJSONArray("users");
							for (int j = 0; j < users.length(); j++) {
								User user = new User();
								JSONObject userObj = users.getJSONObject(j);
								user.profileImageUrl = userObj
										.optString("profileImageURL");
								user.userName = userObj.getString("name");
								v.users.add(user);
							}
							detailedPlant.votes.add(v);
						}
						return true;
					}
				} else {
					return false;
				}

				// result = DataConnector.getInstance().voteForPlant(voted_name,
				// globalState.currentPlant.plant_id);
			} catch (Exception e) {
				e.printStackTrace();
				// result = "Error Sending Request";
			}
			return false;

		}

		protected void onPostExecute(Boolean result) {
			String message = "Failed to send vote";
			try {
				dialog.dismiss();
			} catch (Exception e) {

			}

			try {

				if (result) {
					initVotePanel();
					message = "Vote sent successfully";
				} else {

					Toast.makeText(mContext, "Failed to send vote",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Toast.makeText(mContext, "Failed to send vote",
						Toast.LENGTH_SHORT).show();
			}

			AlertDialog.Builder alertdialog = new AlertDialog.Builder(mContext);
			alertdialog.setIcon(R.drawable.logo);
			alertdialog.setTitle(" ");
			alertdialog.setMessage(message);
			alertdialog.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							try {
								dialog.dismiss();
							} catch (Exception e) {

							} // finish();
						}
					});
			alertdialog.setCancelable(true);
			alertdialog.create().show();

			// if (this.result.equalsIgnoreCase("Success")) {
			// finish();
			// startActivity(new Intent(mContext, HomeScreen.class));
			// }
		};
	}

	// ====== LoadDataTask ===============
	private class LoadDataTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setTitle(" ");
			dialog.setIcon(R.drawable.icon);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Loading Plant Info ...");
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {

				String url = Constants.METHOD_SINGLE_PLANT;
				List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
				urlParam.add(new BasicNameValuePair("id", ""
						+ globalState.currentPlant.plant_id));
				ServerResponse response = jsonParser.retrieveServerData(1,
						Constants.REQUEST_TYPE_GET, url, urlParam, null,
						appInstance.getAccessToken());

				Log.d("singlePlant", "Status:" + response.getStatus());
				Log.d("singlePlant", "Res :" + response.getjObj().toString());

				if (response.getStatus() == 200) {
					JSONObject obj = response.getjObj();
					if (obj.optString("response").equalsIgnoreCase("success")) {
						detailedPlant = new DetailedPlant();
						detailedPlant.plant_id = obj.optInt("plant_id");
						detailedPlant.identifier_name = obj
								.optString("uploaded_by");
						detailedPlant.country = obj.optString("country");
						detailedPlant.state = obj.optString("state");
						detailedPlant.region = obj.optString("region");
						detailedPlant.city = obj.optString("city");
						detailedPlant.description = obj
								.getString("description");
						detailedPlant.group_id = obj.optString("group_id");

						JSONArray images = obj.optJSONArray("image_url");
						for (int i = 0; i < images.length(); i++) {
							detailedPlant.imageUrls
									.add("http://mistersmartyplants.com"
											+ images.getString(i).replaceAll(
													"~", ""));
						}

						detailedPlant.plant_name = GlobalState.getInstance().currentPlant.plant_name;
						detailedPlant.plant_name_agree_prc = GlobalState
								.getInstance().currentPlant.plant_name_agree_prc;

						JSONArray votes = obj.optJSONArray("vote_detail");
						for (int i = 0; i < votes.length(); i++) {
							Vote v = new Vote();
							JSONObject voteObj = votes.getJSONObject(i);
							v.agreeButtonVisible = voteObj
									.optBoolean("agreeButtonVisible");
							v.agreePercentage = voteObj.optInt("agreePercent");
							v.isSolved = voteObj.optBoolean("isSolved");
							v.plantGoogleUrl = voteObj
									.optString("plantGoogleURL");
							v.plantId = voteObj.optInt("plantId");
							v.plantName = voteObj.optString("plantName");
							v.userNames = voteObj.optString("userNames");
							JSONArray users = voteObj.optJSONArray("users");
							for (int j = 0; j < users.length(); j++) {
								User user = new User();
								JSONObject userObj = users.getJSONObject(j);
								user.profileImageUrl = userObj
										.optString("profileImageURL");
								user.userName = userObj.getString("name");
								v.users.add(user);
							}
							detailedPlant.votes.add(v);
						}
						globalState.currentPlant = detailedPlant;
						return true;
					} else
						return false;
				} else
					return false;

			} catch (Exception e) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			try {
				dialog.dismiss();
				if (result) {
					initUI();
				} else
					Toast.makeText(mContext, "Failed to load plant data",
							Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(mContext, "Failed to load plant data",
						Toast.LENGTH_SHORT).show();
			}

		}
	}
}