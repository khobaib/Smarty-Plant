package smartyplant.Utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import smartyplant.core.R;
import smartyplant.modules.BriefedPlant;
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
	public ArrayList<Bitmap> currentBitmaps = new ArrayList<Bitmap>();
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
		currentBitmaps.remove(pos);
	}

	public void addBitmap(Bitmap bitmap) {
		currentBitmaps.add(bitmap);
	}

	public Bitmap decodeSampledBitmap(InputStream imageStream, int reqWidth,
			int reqHeight) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(imageStream, null, options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		String imageType = options.outMimeType;

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(imageStream, null, options);
	}

	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

}
