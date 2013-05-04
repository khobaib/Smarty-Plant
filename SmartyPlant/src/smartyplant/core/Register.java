package smartyplant.core;

import smartyplant.Network.DataConnector;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Register extends Activity {

	String username;
	String password;
	String email;
	EditText username_field;
	EditText password_field;
	EditText confirm_field;
	EditText email_field;
	Context mContext = this;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        username_field = (EditText)findViewById(R.id.user_name_field);
        email_field = (EditText)findViewById(R.id.email_field);
        password_field = (EditText)findViewById(R.id.password_field);
        confirm_field = (EditText)findViewById(R.id.confirm_password_field);
        
        ImageView create = (ImageView)findViewById(R.id.create);
        create.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				username = username_field.getEditableText().toString();
				email = email_field.getEditableText().toString();
				password = password_field.getEditableText().toString();
				String confirm = confirm_field.getEditableText().toString();
				
				if(!password.equals(confirm)){
					Toast.makeText(mContext,"Password & Confirmation do no match, please retype password", 3000).show();
				}
				else{
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
	
	
	 private class RegTask extends AsyncTask<Void, Void, Void>
	 {

		int result ;
    	ProgressDialog dialog = null;

		
		
		@Override
    	protected void onPreExecute() {
    		dialog = new ProgressDialog(mContext);			
			dialog.setTitle("Smarty Plants");
			dialog.setIcon(R.drawable.logo);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Registering ...");
			dialog.show();
    	}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			try 
			{
				result = DataConnector.getInstance().register(username, email, password);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			return null;
			
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			
			if(this.result == 1){
				Toast.makeText(mContext, "Registered Successfully", 3000).show();

			finish();
			startActivity(new Intent(mContext, Login.class));
			}
			
			else if(this.result == 2){
				Toast.makeText(mContext, "User already exists", 3000).show();
			}
			
			else
				Toast.makeText(mContext, "Problem With Registration", 3000).show();

		}
		 
	 }
	
}
