package smartyplant.Utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import smartyplant.modules.Plant;
import android.graphics.Bitmap;
import android.util.Base64;

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

}
