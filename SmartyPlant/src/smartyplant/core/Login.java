package smartyplant.core;

import smartyplant.api.ApiConnector;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.LayoutParams;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;




public class Login extends SherlockActivity  {
	
	
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
        setContentView(R.layout.login);

        
        // retrieve Connector Instance
        ApiConnector connector = ApiConnector.getInstance();
        
        Button login = (Button)findViewById(R.id.sign_in);
        login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
				startActivity(intent);
				finish();
			}
		});
        
        
        ActionBar bar = getSupportActionBar();
        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.actionbar);
        bar.setBackgroundDrawable(bg);
        bar.setIcon(R.drawable.logo);
        
        bar.setDisplayShowTitleEnabled(false);
    }

    
}
