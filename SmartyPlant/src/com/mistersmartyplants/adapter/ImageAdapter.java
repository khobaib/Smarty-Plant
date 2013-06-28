package com.mistersmartyplants.adapter;

import java.util.ArrayList;

import com.mistersmartyplants.core.R;
import com.mistersmartyplants.core.R.layout;
import com.mistersmartyplants.lazylist.ImageLoader;
import com.mistersmartyplants.model.BriefedPlant;
import com.mistersmartyplants.utility.GlobalState;

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

	public BriefedPlant[] plants;
	int width;
	int height;

	// Constructor
	public ImageAdapter(Context c, int width, int height,
			ArrayList<BriefedPlant> plants) {
		context = c;
		this.width = width;
		this.height = height;
		this.plants = new BriefedPlant[plants.size()];
		for (int i = 0; i < plants.size(); i++) {
			this.plants[i] = plants.get(i);
		}
		imageLoader = new ImageLoader(context);
	}
	
	public ImageAdapter(Context c, int width, int height) {
		context = c;
		this.width = width;
		this.height = height;
		imageLoader = new ImageLoader(context);
	}
	

	public void updatePlants(ArrayList<BriefedPlant> plants) {
		this.plants = new BriefedPlant[plants.size()];
		for (int i = 0; i < plants.size(); i++) {
			this.plants[i] = plants.get(i);
		}
		notifyDataSetChanged();
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

		BriefedPlant p = plants[position];
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.custom_item, null);
		RelativeLayout l = (RelativeLayout) v;
		FrameLayout imageFrame = (FrameLayout) l.getChildAt(0);
		// imageFrame.setBackgroundDrawable(p.image_drawable);

		ImageView plant_image = (ImageView) imageFrame.getChildAt(0);
		imageLoader.DisplayImage(p.image_url, plant_image);

		RelativeLayout identifier_info = (RelativeLayout) imageFrame
				.getChildAt(1);

		ImageView identifier_image = (ImageView) identifier_info.getChildAt(0);
		imageLoader.DisplayImage(p.identifier_picture_url, identifier_image);

		TextView identifier_name = (TextView) identifier_info.getChildAt(1);
		identifier_name.setText("");

		TextView identifier_name2 = (TextView) identifier_info.getChildAt(2);
		identifier_name2.setText("");

		RelativeLayout footer_parent = (RelativeLayout) l.getChildAt(1);
		RelativeLayout footer = (RelativeLayout) footer_parent.getChildAt(0);
		TextView plant_name = (TextView) footer.getChildAt(0);
		plant_name.setText(p.plant_name);

		TextView plant_agree = (TextView) footer.getChildAt(1);
		plant_agree.setText(p.plant_name_agree_prc+"% agree");

		ProgressBar bar = (ProgressBar) footer.getChildAt(2);
		bar.setProgress(p.plant_name_agree_prc);

		v.setLayoutParams(new GridView.LayoutParams(
				GridView.LayoutParams.FILL_PARENT,
				GridView.LayoutParams.FILL_PARENT));

		return v;
	}

}
