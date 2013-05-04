package smartyplant.core;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class Home extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		TabHost tabHost = getTabHost();
		//tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		tabHost.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		TabSpec my_plants = tabHost.newTabSpec("My_Plants");
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		LinearLayout view = (LinearLayout)inflater.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.getChildAt(0);
		tv.setText("My Plants");
		my_plants.setIndicator(view);		
		Intent songsIntent = new Intent(this, My_PlantsActivity.class);
		my_plants.setContent(songsIntent);

		TabSpec submit_plants = tabHost.newTabSpec("Submit_Plants");
		LinearLayout view1 = (LinearLayout)inflater.inflate(R.layout.tabs_bg, null);
		TextView tv1 = (TextView) view1.getChildAt(0);
		tv1.setText("Submit Plant");
		submit_plants.setIndicator(view1);
		
		Intent videosIntent = new Intent(this, Submit_PlantsActivity.class);
		submit_plants.setContent(videosIntent);

		tabHost.addTab(my_plants);
		tabHost.addTab(submit_plants);
		
//		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
//			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
//			tv.setTextSize(10);
//		}

		final Button settings = (Button) findViewById(R.id.settings);
		settings.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				registerForContextMenu(v);
				openContextMenu(v);
				unregisterForContextMenu(v);
			}
		});

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderIcon(R.drawable.logo);
		menu.add(0, v.getId(), 0, "Submit Plant");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		Intent intent = new Intent(getApplicationContext(), Register.class);
		startActivity(intent);

		return true;
	}

}
