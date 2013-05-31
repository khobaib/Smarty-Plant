package smartyplant.core;

import smartyplant.Network.DataConnector;
import smartyplant.Utils.GlobalState;
import smartyplant.lazylist.ImageLoader;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlantDetails extends Activity {
	Context mContext = this;
	GlobalState globalState = GlobalState.getInstance();
	String voted_name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plant_details);

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
		TextView label1 = (TextView) findViewById(R.id.label1);
		label1.setText(GlobalState.getInstance().currentPlant.identifier_name);
		TextView label3 = (TextView) findViewById(R.id.label3);
		label3.setText(GlobalState.getInstance().currentPlant.plant_name);

		TextView label4 = (TextView) findViewById(R.id.label4);
		label4.setText(globalState.currentPlant.plant_name_agree_prc
				+ "% agreed");

		ImageView image = (ImageView) findViewById(R.id.image_view);
		new ImageLoader(this).DisplayImage(globalState.currentPlant.image_url,
				image);

		ProgressBar bar = (ProgressBar) findViewById(R.id.agree_prc_bar);
		bar.setProgress(globalState.currentPlant.plant_name_agree_prc);

		initVotePanel();

	}

	private void initVotePanel() {
		Button agree = (Button) findViewById(R.id.agree_button);
		agree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				voted_name = globalState.currentPlant.plant_name;
				VoteTask task = new VoteTask();
				task.execute();

			}
		});

		Button disagree = (Button) findViewById(R.id.disagree_button);
		disagree.setOnClickListener(new OnClickListener() {

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
							dialog.dismiss();
							Toast.makeText(mContext, "Plant name can not be emtpy !", 5000).show();
						} else {
							VoteTask task = new VoteTask();
							task.execute();
							dialog.dismiss();
						}
					}
				});

				dialog.show();
			}
		});

		TextView plant_more_url = (TextView) findViewById(R.id.plant_more_url);
		plant_more_url.setText(globalState.currentPlant.plant_name);
		plant_more_url.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://www.google.com/search?q="
								+ globalState.currentPlant.plant_name
										.replaceAll(" ", "+") + "&tbm=isch"));
				startActivity(browserIntent);
			}
		});

		if (globalState.currentPlant.plant_name == null
				|| globalState.currentPlant.plant_name.equalsIgnoreCase("")) {
			TextView label1 = (TextView) findViewById(R.id.label1);
			label1.setVisibility(TextView.INVISIBLE);
			TextView label2 = (TextView) findViewById(R.id.label2);
			label2.setVisibility(TextView.INVISIBLE);
			TextView label3 = (TextView) findViewById(R.id.label3);
			label3.setVisibility(TextView.INVISIBLE);
			TextView more = (TextView) findViewById(R.id.more);
			more.setVisibility(TextView.INVISIBLE);
			plant_more_url.setVisibility(TextView.INVISIBLE);
		}

		if (globalState.currentPlant.plant_name_agree_prc == 0) {
			agree.setVisibility(Button.GONE);
			disagree.setText("Name It");
		}
	}

	// ============= Background Task ========
	class VoteTask extends AsyncTask<Void, Void, Void> {
		String result;
		ProgressDialog dialog = null;

		// alertDialog = null;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setTitle(" ");
			dialog.setIcon(R.drawable.logo);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Sending Request ...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				result = DataConnector.getInstance().voteForPlant(voted_name,
						globalState.currentPlant.plant_id);
			} catch (Exception e) {
				e.printStackTrace();
				result = "Error Sending Request";
			}
			return null;

		}

		protected void onPostExecute(Void result) {
			try {
				dialog.dismiss();
			} catch (Exception e) {

			}

			AlertDialog.Builder alertdialog = new AlertDialog.Builder(mContext);
			alertdialog.setIcon(R.drawable.logo);
			alertdialog.setTitle(" ");
			alertdialog.setMessage(this.result);
			alertdialog.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						}
					});
			alertdialog.setCancelable(true);
			alertdialog.create().show();

			if (this.result.equalsIgnoreCase("Success")) {
				finish();
				startActivity(new Intent(mContext, HomeScreen.class));
			}
		};
	}

}
