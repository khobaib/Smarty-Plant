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
import com.actionbarsherlock.app.ActionBar.LayoutParams;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;




public class Login extends SherlockActivity  {
	
	EditText user_name_field;
	EditText password_field;
    ApiConnector connector = ApiConnector.getInstance();

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
        SubMenu subMenu1 = menu.addSubMenu("");
        subMenu1.add("Register");
        
        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.abs__ic_menu_moreoverflow_holo_dark);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        MenuItem item = subMenu1.getItem(0);
        
        item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(getApplicationContext(), Register.class);
				startActivity(intent);
				return false;
			}
		});
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock);
        setContentView(R.layout.login);

        user_name_field = (EditText)findViewById(R.id.user_name_field);
        password_field = (EditText)findViewById(R.id.password_field);
        
        // retrieve Connector Instance
        
        Button login = (Button)findViewById(R.id.sign_in);
        login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String user_name = user_name_field.getEditableText().toString();
				String password = password_field.getEditableText().toString();
				connector.API_login(user_name, password);
				
			}
		});
        
        
        ActionBar bar = getSupportActionBar();
        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.actionbar);
        bar.setBackgroundDrawable(bg);
        bar.setIcon(R.drawable.logo);
        
        bar.setDisplayShowTitleEnabled(false);
    }

    
}
