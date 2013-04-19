package smartyplant.core;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class HomeScreen extends SherlockActivity implements ActionBar.TabListener {
   
	private TextView mSelected;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
        SubMenu subMenu1 = menu.addSubMenu("");
        subMenu1.add("Sample");
        subMenu1.add("Menu");
        subMenu1.add("Items");

        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.abs__ic_menu_moreoverflow_holo_dark);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);

    
    }
    
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setTheme(R.style.Theme_Sherlock_Light_DarkActionBar); 

	        setContentView(R.layout.home);
	        mSelected = (TextView)findViewById(R.id.text);
	        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        for (int i = 1; i <= 3; i++) {
	            ActionBar.Tab tab = getSupportActionBar().newTab();
	            tab.setText("Tab " + i);
	            tab.setTabListener(this);
	            getSupportActionBar().addTab(tab);
	        }
	        
	        ActionBar bar = getSupportActionBar();
            BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.actionbar);
            bar.setBackgroundDrawable(bg);
            
            bar.setIcon(R.drawable.logo);
            bar.setDisplayShowTitleEnabled(false);
            
	        
	    }

	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
        mSelected.setText("Selected: " + tab.getText());		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


}
