package smartyplant.core;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import smartyplant.Utils.GlobalState;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class Home extends TabActivity {

	GlobalState globalState = GlobalState.getInstance();
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		
//		setTheme(R.style.Theme_Sherlock);
//		setContentView(R.layout.home);
//		android.app.ActionBar bar = getActionBar();	
//		bar.setDisplayHomeAsUpEnabled(false);
//		bar.setDisplayShowHomeEnabled(false);
//		bar.setDisplayShowTitleEnabled(false);
//		bar.setDisplayUseLogoEnabled(false);
//		
//		
//		
//		Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.actionbar_bg);
//		BitmapDrawable d = new BitmapDrawable(b);
//		bar.setBackgroundDrawable(d);
//		
//		
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View logoView = inflater.inflate(R.layout.bar, null, false);
//		ViewGroup decorViewGroup = (ViewGroup) ((SherlockActivity) context).getWindow().getDecorView();
//		decorViewGroup.addView(logoView);
//		
//		
//		TabHost tabHost = getTabHost();
//		tabHost.setLayoutParams(new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.FILL_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT));
//
//		TabSpec my_plants = tabHost.newTabSpec("My_Plants");
//		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
//		LinearLayout view = (LinearLayout)inflater.inflate(R.layout.tabs_bg, null);
//		TextView tv = (TextView) view.getChildAt(0);
//		tv.setText("My Plants");
//		my_plants.setIndicator(view);		
//		Intent songsIntent = new Intent(this, My_PlantsActivity.class);
//		my_plants.setContent(songsIntent);
//
//		TabSpec submit_plants = tabHost.newTabSpec("Submit_Plants");
//		LinearLayout view1 = (LinearLayout)inflater.inflate(R.layout.tabs_bg, null);
//		TextView tv1 = (TextView) view1.getChildAt(0);
//		tv1.setText("Submit Plant");
//		submit_plants.setIndicator(view1);
//		
//		Intent videosIntent = new Intent(this, Submit_PlantsActivity.class);
//		submit_plants.setContent(videosIntent);
//
//		tabHost.addTab(my_plants);
//		tabHost.addTab(submit_plants);
//		
//
//		
//
//	}

	

}
