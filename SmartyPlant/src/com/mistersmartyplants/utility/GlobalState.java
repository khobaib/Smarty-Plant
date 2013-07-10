package com.mistersmartyplants.utility;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
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

	public String base64 = "";
	public ArrayList<String> currentBitmaps = new ArrayList<String>();
	public Plant currentPlant;
	public int currentIndex;
	public RequestToken TwitterRequestToken;
	public Twitter twitter;

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

	public Bitmap bitmapFromPath(String path)
	{
		boolean satisfied = false;
		int factor = 2;
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bitmap = null;
		
		while (!satisfied)
		{
			factor = factor*2;
			options.inSampleSize = factor;
			bitmap = BitmapFactory.decodeFile(path, options);
			
			if(bitmap == null)
				satisfied = false;
		}
		return bitmap;
	}
	
	
	
	
}
