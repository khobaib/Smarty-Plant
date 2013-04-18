package smartyplant.core;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

public class HomeScreen extends SherlockActivity implements ActionBar.TabListener {
   
	private TextView mSelected;

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
