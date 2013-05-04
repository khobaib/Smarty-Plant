package smartyplant.core;

import smartyplant.Utils.GlobalState;
import smartyplant.modules.Plant;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context context;

	public Plant[] plants = new Plant[GlobalState.getInstance().all_plants.size()];
	
	
	
	// Constructor
	public ImageAdapter(Context c){
		context = c;
		for (int i = 0 ; i < GlobalState.getInstance().all_plants.size() ; i ++){
			plants[i] = GlobalState.getInstance().all_plants.get(i);
		}
	}

	@Override
	public int getCount() {
		return plants.length;
	}

	@Override
	public Object getItem(int position) {
		return plants[position];
	}

	@Override
	public long getItemId(int position) {
		return plants[position].plant_id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		Plant p = plants[position];
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View v = inflater.inflate(R.layout.custom_item	, null);
		RelativeLayout l = (RelativeLayout)v;
		FrameLayout imageFrame = (FrameLayout)l.getChildAt(0);
		imageFrame.setBackgroundDrawable(p.image_drawable);
		// setBackGroudImage
		
		RelativeLayout footer = (RelativeLayout)l.getChildAt(1);
		TextView plant_name = (TextView)footer.getChildAt(0);
		plant_name.setText(p.plant_name);
		
		TextView plant_prc = (TextView)footer.getChildAt(1);
		plant_prc.setText(p.plant_name_agree_prc + "% agree");
		
		ProgressBar bar = (ProgressBar)footer.getChildAt(2);
		bar.setProgress(p.plant_name_agree_prc);
		
		return v;
	}

}
