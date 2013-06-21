package smartyplant.core;

import smartyplant.Network.DataConnector;
import smartyplant.Utils.GlobalState;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.SubMenu;
import com.bugsense.trace.BugSenseHandler;

public class Login extends SherlockActivity {
	String user_name = "";
	String password = "";
	EditText user_name_field;
	EditText password_field;
	CheckBox remember_me;
	Context mContext = this;

	GlobalState globalState = GlobalState.getInstance();
	DataConnector dataConnector = DataConnector.getInstance();
	SharedPreferences prefs;
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BugSenseHandler.initAndStartSession(mContext, "f2391cbb");
		super.onCreate(savedInstanceState);
		globalState.initActionBar(this, R.layout.login);

		user_name_field = (EditText) findViewById(R.id.user_name_field);
		password_field = (EditText) findViewById(R.id.password_field);
		remember_me = (CheckBox) findViewById(R.id.remember_me);
		prefs = getSharedPreferences(PREFS_NAME, 0);

		if (prefs.getBoolean("remember_me", false)) {
			remember_me.setChecked(true);
			user_name = prefs.getString("user_name", "");
			password = prefs.getString("password", "");

			LoginTask task = new LoginTask();
			task.execute();
		}

		final ImageView sign_in = (ImageView) findViewById(R.id.sign_in);
		sign_in.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				user_name = user_name_field.getEditableText().toString();
				password = password_field.getEditableText().toString();

				if (remember_me.isChecked()) {
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("user_name", user_name);
					editor.putString("password", password);
					editor.putBoolean("remember_me", true);
					editor.commit();

				} else {
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("user_name", "");
					editor.putString("password", "");
					editor.putBoolean("remember_me", false);
					editor.commit();
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

	private class LoginTask extends AsyncTask<Void, Void, Void> {

		boolean result;
		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setTitle(" ");

			dialog.setIcon(R.drawable.logo);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Logging In");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				result = dataConnector.loginIn(user_name, password);
				// result =true;
			} catch (Exception e) {

				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			try {
				dialog.dismiss();
				dialog = null;
			} catch (Exception e) {

			}
			if (this.result) {
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
