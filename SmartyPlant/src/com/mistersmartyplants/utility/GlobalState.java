package com.mistersmartyplants.utility;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
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

	public Bitmap bmp = null;
	public String bmpPath = "";
	public ArrayList<BriefedPlant> all_plants = new ArrayList<BriefedPlant>();

	public static GlobalState getInstance() {
		return instance;
	}

	public boolean bitmapToBase64(Bitmap currentBitmap) {
		bmp = currentBitmap;
		Thread t = new Thread() {
			@Override
			public void run() {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
				byte[] b = baos.toByteArray();
				base64 = Base64.encodeToString(b, Base64.DEFAULT);
				
				synchronized (this) {
					notify();
				}
			}	
		};
		t.start();
		synchronized (t) {
			try {
				t.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
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

		bmpPath = path;
		bmp = null;
		Thread t = new Thread() {
			@Override
			public void run() {
				super.run();

				boolean satisfied = false;
				int factor = 2;
				BitmapFactory.Options options = new BitmapFactory.Options();
				int orientation;

				try {
					ExifInterface ei = new ExifInterface(bmpPath);
					orientation = ei.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);
				} catch (Exception e) {
					orientation = 0;
				}

				while (!satisfied) {
					factor = factor * 2;
					options.inSampleSize = factor;
					bmp = BitmapFactory.decodeFile(bmpPath, options);
					if (bmp == null)
						satisfied = false;
					else
						satisfied = true;
				}

				Log.d("helal", "Orientation :" + orientation);

				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					Log.d("helal", "Rotate : 90");
					bmp = rotateImage(bmp, 90);
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					Log.d("helal", "Rotate : 180");
					bmp = rotateImage(bmp, 180);
					break;

				}
				synchronized (this) {
					notify();
				}
			}
		};

		t.start();
		try {
			synchronized (t) {
				t.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return bmp;
	}

	private Bitmap rotateImage(Bitmap bitmap, int angle) {
		Matrix matrix = new Matrix();
		// matrix.postRotate(angle);
		matrix.setRotate(angle);

		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

	}
	
	
	
	public String getDeviceModelName() {
	       String brand = Build.BRAND;
	       String model = Build.MODEL;
	       String version = Build.VERSION.RELEASE;
	       
	       if (model.startsWith(brand)) {
	           return model.toUpperCase() + " " + version;
	       } else {
	           return brand.toUpperCase() + " " + model + " " + version;
	       }
	   }

}
