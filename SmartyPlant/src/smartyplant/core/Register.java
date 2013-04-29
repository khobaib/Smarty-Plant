package smartyplant.core;

import smartyplant.api.ApiConnector;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class Register extends SherlockActivity {

	EditText user_name_field;
	EditText email_field;
	EditText password_field;
	EditText confirm_password_field;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock);

        setContentView(R.layout.register);
        ActionBar bar = getSupportActionBar();
        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.actionbar);
        bar.setBackgroundDrawable(bg);
        bar.setIcon(R.drawable.logo);
        bar.setDisplayShowTitleEnabled(false);
        
        user_name_field = (EditText)findViewById(R.id.user_name_field);
        email_field = (EditText)findViewById(R.id.email_field);
        password_field = (EditText)findViewById(R.id.password_field);
        confirm_password_field = (EditText)findViewById(R.id.confirm_password_field);
        
    }
	
	private void setClickListener(){
		Button reg = (Button)findViewById(R.id.create);
		reg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String user_name = user_name_field.getEditableText().toString();
				String email = email_field.getEditableText().toString();
				String password = password_field.getEditableText().toString();
				String confirm_password = confirm_password_field.getEditableText().toString();
				
				
			}
		});
	}
	
}
