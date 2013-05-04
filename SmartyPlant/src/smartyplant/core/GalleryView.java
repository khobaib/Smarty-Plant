package smartyplant.core;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;

public class GalleryView extends Activity {
	Gallery gallery;
	Context mContext = this;
	private Cursor cursor;
	private int columnIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);

		String[] projection = { MediaStore.Images.Thumbnails._ID };
		cursor = managedQuery(
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, // Which
																				// columns
																				// to
																				// return
				null, // Return all rows
				null, MediaStore.Images.Thumbnails.IMAGE_ID);
		columnIndex = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);

		gallery = (Gallery) findViewById(R.id.grid_view);
		gallery.setAdapter(new GAdapter(mContext));

	}

	private int getColumnWidth() {
		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int colWidth = (screenWidth - 50) / 3;
		return colWidth;
	}

	private int getColumnHeight() {
		Display display = getWindowManager().getDefaultDisplay();
		int screenHeight = display.getHeight();
		int colHeight = (screenHeight - 30) / 3;
		return colHeight;
	}

	
	private class GAdapter extends BaseAdapter{
        private Context context;
        
        public GAdapter(Context localContext){
            context = localContext;
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
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                // Move cursor to current position
                cursor.moveToPosition(position);
                // Get the current value for the requested column
                int imageID = cursor.getInt(columnIndex);
                // Set the content of the image based on the provided URI
                picturesView.setImageURI(Uri.withAppendedPath(
                        MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID));
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView.setPadding(8, 8, 8, 8);
                picturesView.setLayoutParams(new GridView.LayoutParams(100, 100));
            }
            else {
                picturesView = (ImageView)convertView;
            }
            return picturesView;
        }
		
	}
}
