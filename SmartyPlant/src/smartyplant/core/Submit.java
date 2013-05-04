package smartyplant.core;

import smartyplant.Network.DataConnector;
import smartyplant.Utils.GlobalState;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Submit extends Activity{
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_form);
		countryField = (EditText)findViewById(R.id.country_field);
		stateField = (EditText)findViewById(R.id.state_field);
		cityField = (EditText)findViewById(R.id.city_field);
		regionField = (EditText)findViewById(R.id.region_field);
		descField = (EditText)findViewById(R.id.desc_field);
		
		
		TextView image = (TextView)findViewById(R.id.image);
		Drawable d = new BitmapDrawable(GlobalState.getInstance().currentBitmap);
		image.setBackgroundDrawable(d);
		
		Button submit = (Button)findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				country = countryField.getEditableText().toString();
				state = stateField.getEditableText().toString();
				city = cityField.getEditableText().toString();
				region = regionField.getEditableText().toString();
				desc = descField.getEditableText().toString();
				
				UploadTask task = new UploadTask();
				task.execute();
				
			}
		});
	}
	
	class UploadTask extends AsyncTask<Void, Void, Void>{
		String result ;
    	ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);			
			dialog.setTitle("Smarty Plants");
			dialog.setIcon(R.drawable.logo);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Uploading Image ...");
			dialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				result = DataConnector.getInstance().uploadImage(GlobalState.getInstance().base64, country, state, city, region, desc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = "Error Uploading Image";
			}
			return null;

		}
		
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			Toast.makeText(mContext, this.result, 3000).show();
			
		};
	}

}
