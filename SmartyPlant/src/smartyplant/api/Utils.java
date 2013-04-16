package smartyplant.api;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	
	
	public String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}

}
