package smartyplant.core;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GalleryAdapter extends BaseAdapter{
	public File[] images;
	private Context context;
	private int height;
	private int width;
	public GalleryAdapter(Context c, int w, int h) {
		context = c;
		width = w;
		height = h;
		String path = Environment.getExternalStorageDirectory()+"//DCIM//Camera";
		File dir = new File(path);
		dir.mkdirs();
		images = dir.listFiles();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return images[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout frame = (FrameLayout)inflater.inflate(R.layout.custom_gallery_item	, null);
		TextView v = (TextView)frame.getChildAt(0);
		
		Bitmap bitmap = BitmapFactory.decodeFile(images[arg0].getPath());
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleWidth = ((float) 1) / 8;
		float scaleHeight = ((float) 1) / 8;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
		Drawable d = new BitmapDrawable(resizedBitmap);
		v.setBackgroundDrawable(d);
		v.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, height));
		
		return frame;
	}

}
