package smartyplant.core;

import smartyplant.Network.DataConnector;
import smartyplant.Utils.GlobalState;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Login extends Activity {
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		user_name_field = (EditText) findViewById(R.id.user_name_field);
		password_field = (EditText) findViewById(R.id.password_field);
		remember_me = (CheckBox) findViewById(R.id.remember_me);
		prefs = getSharedPreferences(PREFS_NAME, 0);

		if (prefs.getBoolean("remember_me", false)) {
			remember_me.setChecked(true);
			user_name_field.setText(prefs.getString("user_name", ""));
			password_field.setText(prefs.getString("password", ""));
		}

		final Button settings = (Button) findViewById(R.id.settings);
		settings.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				registerForContextMenu(v);
				openContextMenu(v);
				unregisterForContextMenu(v);
			}
		});

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

				}
				else
				{
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

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("Options ... ");
		menu.add(0, v.getId(), 0, "Register");
		menu.add(0, v.getId(), 0, "Quit");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "Register") {
			Intent intent = new Intent(getApplicationContext(), Register.class);
			startActivity(intent);
		}

		else if (item.getTitle() == "Quit") {
			this.finish();
		}

		return true;
	}

	private class LoginTask extends AsyncTask<Void, Void, Void> {

		boolean result;
		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setTitle("Smarty Plants");
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
			} catch (Exception e) {

				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();

			if (this.result) {
				finish();
				startActivity(new Intent(mContext, Home.class));
			} else {
				Toast.makeText(mContext, "Username or Password Incorrect", 3000)
						.show();
			}
		}

	}

}
