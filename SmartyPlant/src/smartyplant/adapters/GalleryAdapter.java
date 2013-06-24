package smartyplant.adapters;

import smartyplant.Utils.GlobalState;
import smartyplant.core.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GalleryAdapter extends BaseAdapter {
	private Context mContext;
	private int mode;
	GlobalState globalState = GlobalState.getInstance();

	public GalleryAdapter(Context c, int m) {
		mContext = c;
		mode = m;
	}

	@Override
	public int getCount() {
		return globalState.currentBitmaps.size();
	}

	@Override
	public Object getItem(int index) {
		return globalState.currentBitmaps.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		String path = globalState.currentBitmaps.get(pos);
		Bitmap bitmap = globalState.bitmapFromPath(path);
		
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.custom_gallery_item, null);
		FrameLayout frame = (FrameLayout)v;
		TextView tv = (TextView)frame.getChildAt(0);
		Drawable d = new BitmapDrawable(bitmap);
		tv.setBackgroundDrawable(d);
		Button remove = (Button)frame.getChildAt(1);		
		if (mode == 1){
		final int bitmapPos = pos;
		remove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				globalState.removeBitmap(bitmapPos);
				Button upload = (Button)((Activity)mContext).findViewById(R.id.upload_image);
				Button done = (Button)((Activity)mContext).findViewById(R.id.done);
				
				if (globalState.currentBitmaps.size() == 0){
					upload.setText("Upload Mystery Plant");
					done.setVisibility(Button.GONE);
				}
				else
				{
					upload.setText("Upload Another Mystery");
					done.setVisibility(Button.VISIBLE);
					
				}
				
				notifyDataSetChanged();
			}
		});
		}
		else
		{
			remove.setVisibility(Button.INVISIBLE);
		}
		
		return v;
	}

}
