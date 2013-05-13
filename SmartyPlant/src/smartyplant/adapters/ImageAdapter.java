package smartyplant.adapters;

import smartyplant.Utils.GlobalState;
import smartyplant.core.R;
import smartyplant.core.R.layout;
import smartyplant.lazylist.ImageLoader;
import smartyplant.modules.Plant;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private ImageLoader imageLoader;

	public Plant[] plants = new Plant[GlobalState.getInstance().all_plants.size()];
	int width;
	int height;
	
	
	// Constructor
	public ImageAdapter(Context c, int width, int height){
		context = c;
		this.width = width;
		this.height = height;
		for (int i = 0 ; i < GlobalState.getInstance().all_plants.size() ; i ++){
			plants[i] = GlobalState.getInstance().all_plants.get(i);
		}
		
	    imageLoader=new ImageLoader(context);

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
		//imageFrame.setBackgroundDrawable(p.image_drawable);
		
		ImageView plant_image = (ImageView)imageFrame.getChildAt(0);
		imageLoader.DisplayImage(p.image_url, plant_image);
		
		RelativeLayout identifier_info = (RelativeLayout)imageFrame.getChildAt(1);
		ImageView identifier_image = (ImageView)identifier_info.getChildAt(0);
		imageLoader.DisplayImage(p.identifier_picture_url, identifier_image);
//		identifier_image.setImageDrawable(p.identifier_picture_drawable);
		
		TextView identifier_name = (TextView)identifier_info.getChildAt(1);
		identifier_name.setText(p.identifier_name);
		
		
		TextView identifier_name2 = (TextView)identifier_info.getChildAt(2);
		identifier_name2.setText("@"+p.identifier_name);
		
		RelativeLayout footer = (RelativeLayout)l.getChildAt(1);
		TextView plant_name = (TextView)footer.getChildAt(0);
		plant_name.setText(p.plant_name);
		
		
		
		ProgressBar bar = (ProgressBar)footer.getChildAt(2);
		bar.setProgress(p.plant_name_agree_prc);
		
		v.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, GridView.LayoutParams.FILL_PARENT));
		
		return v;
	}

}
