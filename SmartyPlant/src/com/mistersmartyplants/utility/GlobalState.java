package com.mistersmartyplants.utility;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.mistersmartyplants.core.R;
import com.mistersmartyplants.model.BriefedPlant;
import com.mistersmartyplants.model.Plant;

public class GlobalState {

	private static GlobalState instance = new GlobalState();

	public String API_TOKEN = "";
	public String base64 = "";
	public ArrayList<String> currentBitmaps = new ArrayList<String>();
	public Plant currentPlant;
	public int currentIndex;

	public static String PLANTS_ALL_MINE = "allmine";
	public static String PLANTS_ALL = "all";
	public static String PLANTS_UNSOLVED = "unsolved";

	public ArrayList<BriefedPlant> all_plants = new ArrayList<BriefedPlant>();

	public static GlobalState getInstance() {
		return instance;
	}

	public boolean bitmapToBase64(Bitmap currentBitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is
																		// the
																		// bitmap
																		// object
		byte[] b = baos.toByteArray();
		base64 = Base64.encodeToString(b, Base64.DEFAULT);
		return true;
	}

	public void initActionBar(Context context, int layoutRes) {
		context.setTheme(R.style.Theme_Sherlock_Light);
		((SherlockActivity) context).setContentView(layoutRes);
		ActionBar bar = ((SherlockActivity) context).getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayUseLogoEnabled(false);

		Bitmap b = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.actionbar_bg);
		BitmapDrawable d = new BitmapDrawable(b);
		bar.setBackgroundDrawable(d);

		LayoutInflater inflater = LayoutInflater.from(context);
		View logoView = inflater.inflate(R.layout.bar, null, false);
		ViewGroup decorViewGroup = (ViewGroup) ((SherlockActivity) context)
				.getWindow().getDecorView();
		decorViewGroup.addView(logoView);
	}

	public void removeBitmap(int pos) {
		if (currentBitmaps.size() > pos)
			currentBitmaps.remove(pos);
	}

	public void addBitmap(String bitmap) {
		currentBitmaps.add(bitmap);
	}

	public Bitmap bitmapFromPath(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		
		// try again with more downsampling 
		if(bitmap == null)
		{
			options.inSampleSize = 8;
			bitmap = BitmapFactory.decodeFile(path, options);
		}
		
		// try again with more downsampling 
		if(bitmap == null)
		{
			options.inSampleSize = 16;
			bitmap = BitmapFactory.decodeFile(path, options);
		}
		return bitmap;
	}

}
