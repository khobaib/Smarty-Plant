package smartyplant.api;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Utils {
	
	private static Utils instance = new Utils();
	
	public static Utils getInstance(){
		return instance;
	}
	
	public String encodeImage(File image){
		Bitmap bm = BitmapFactory.decodeFile(image.getPath());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
		byte[] b = baos.toByteArray(); 
		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		return encodedImage;
		
	}
	
	public Bitmap decodeImage(String encodedImage){
		byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
		Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
		return decodedImage;
	}
	
	
	

}
