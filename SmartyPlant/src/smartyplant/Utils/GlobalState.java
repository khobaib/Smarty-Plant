package smartyplant.Utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import smartyplant.core.R;
import smartyplant.modules.Plant;
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

public class GlobalState {
	
	
	private static GlobalState instance = new GlobalState();
	
	
	public String API_TOKEN = "";
	public String base64 = "";
	public Bitmap currentBitmap;
	public Plant currentPlant;
	public int currentIndex;
	
	
	public ArrayList<Plant> all_plants = new ArrayList<Plant>();
	
	public static GlobalState getInstance(){
		return instance;
	}
	
	public boolean bitmapToBase64(){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object   
		byte[] b = baos.toByteArray();
		base64 = Base64.encodeToString(b, Base64.DEFAULT);
		return true;
	}
	
	public void initActionBar(Context context, int layoutRes){
		context.setTheme(R.style.Theme_Sherlock_Light);
		((SherlockActivity) context).setContentView(layoutRes);
		ActionBar bar = ((SherlockActivity) context).getSupportActionBar();	
		bar.setDisplayHomeAsUpEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayUseLogoEnabled(false);
		
		
		
		Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.actionbar_bg);
		BitmapDrawable d = new BitmapDrawable(b);
		bar.setBackgroundDrawable(d);
		
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View logoView = inflater.inflate(R.layout.bar, null, false);
		ViewGroup decorViewGroup = (ViewGroup) ((SherlockActivity) context).getWindow().getDecorView();
		decorViewGroup.addView(logoView);
	}
	
	

}
