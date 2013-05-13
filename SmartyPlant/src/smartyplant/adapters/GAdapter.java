package smartyplant.adapters;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.actionbarsherlock.app.SherlockActivity;

import smartyplant.Utils.GlobalState;
import smartyplant.core.R;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

public class GAdapter extends BaseAdapter {
	private Context context;
	private Cursor cursor;
	private int columnIndex ;
	
	public GAdapter(Context localContext, Cursor cursor) {
		context = localContext;
		this.cursor = cursor;
	}

	@Override
	public int getCount() {
		return cursor.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout frame = (FrameLayout) inflater.inflate(
				R.layout.custom_gallery_item, null);
		TextView v = (TextView) frame.getChildAt(0);
		cursor.moveToPosition(position);
		int imageID = cursor.getInt(columnIndex);

		Drawable d = new BitmapDrawable(
				readBitmap(Uri.withAppendedPath(
						MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, ""
								+ imageID)));
		v.setBackgroundDrawable(d);
		v.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));

		CheckBox cb = (CheckBox) frame.getChildAt(1);
		cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				CheckBox cb = (CheckBox) arg0;
				boolean cbVal = !cb.isChecked();

				GridView gridView = (GridView) ((SherlockActivity)context).findViewById(R.id.grid_view);
				for (int i = 0; i < gridView.getChildCount(); i++) {
					FrameLayout fr = (FrameLayout) gridView.getChildAt(i);
					CheckBox c = (CheckBox) fr.getChildAt(1);
					c.setChecked(false);
				}
				if (cbVal) {
					GlobalState.getInstance().currentBitmap = null;
					cb.setChecked(false);
				} else {
					FrameLayout f = (FrameLayout) cb.getParent();
					TextView tv = (TextView) f.getChildAt(0);
					Bitmap b = ((BitmapDrawable) tv.getBackground())
							.getBitmap();
					GlobalState.getInstance().currentBitmap = b;
					cb.setChecked(true);
				}

			}
		});
		return frame;
	}
	
	public Bitmap readBitmap(Uri selectedImage) {
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2; // reduce quality
		AssetFileDescriptor fileDescriptor = null;
		try {
			fileDescriptor = context.getContentResolver().openAssetFileDescriptor(
					selectedImage, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bm = BitmapFactory.decodeFileDescriptor(
						fileDescriptor.getFileDescriptor(), null, options);
				fileDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	}


}