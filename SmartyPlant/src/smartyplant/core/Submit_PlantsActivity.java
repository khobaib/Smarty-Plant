package smartyplant.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import smartyplant.Utils.GlobalState;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Submit_PlantsActivity extends Activity {

	Context mContext = this;
	private Cursor cursor;
	private int columnIndex;

	int mode = 1;
	String PhotoPath = Environment.getExternalStorageDirectory()
			+ "//smarty_plant.jpg";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMode1();
		setClickListners();

	}

	protected void startCameraActivity() {
		File file = new File(PhotoPath);
		Uri outputFileUri = Uri.fromFile(file);
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		setMode1();
		switch (resultCode) {
		case 0:
			break;

		case -1:
			try {
				Drawable d = onPhotoTaken();
				TextView t = (TextView) findViewById(R.id.image_view);
				t.setBackgroundDrawable(d);
				Display display = getWindowManager().getDefaultDisplay();
				int width = display.getWidth() / 2;
				int height = display.getHeight() / 3;
				t.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
				android.widget.RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) t
						.getLayoutParams();
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				t.setLayoutParams(params);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected Drawable onPhotoTaken() throws Exception {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bitmap = BitmapFactory.decodeFile(PhotoPath, options);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleWidth = ((float) 1) / 2;
		float scaleHeight = ((float) 1) / 2;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		String ff = Environment.getExternalStorageDirectory()
				+ "/compressed.png";
		try {
			FileOutputStream out = new FileOutputStream(ff);
			resizedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			File queryImg = new File(ff);
			int imageLen = (int) queryImg.length();
			byte[] imgData = new byte[imageLen];
			FileInputStream fis = new FileInputStream(queryImg);
			fis.read(imgData);
			GlobalState.getInstance().base64 = Base64.encodeToString(imgData,
					Base64.DEFAULT);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Drawable d = new BitmapDrawable(resizedBitmap);
		GlobalState.getInstance().currentBitmap = resizedBitmap;
		
		
		Button btn = (Button)findViewById(R.id.take_picture);
		btn.setTextColor(Color.RED);
		
		return d;
	}

	private void setMode1() {
		setContentView(R.layout.submit_plants_layout);
		mode = 1;
		setClickListners();

	}

	private void setClickListners() {
		Button takePic = (Button) findViewById(R.id.take_picture);
		takePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GlobalState.getInstance().currentBitmap = null;
				startCameraActivity();
			}
		});

		Button gallery = (Button) findViewById(R.id.select_gallery);
		gallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GlobalState.getInstance().currentBitmap = null;
				openGallery();
			}
		});

		Button done = (Button) findViewById(R.id.done);
		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (GlobalState.getInstance().currentBitmap != null) {
					startActivity(new Intent(mContext, Submit.class));
				}

				else {
					Toast.makeText(mContext, "Please Capture or Select Image first", Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	private void openGallery() {
		mode = 2;
		setContentView(R.layout.gallery);
		setClickListners();

		String[] projection = { MediaStore.Images.Thumbnails._ID };
		cursor = managedQuery(
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection,
				null, null, MediaStore.Images.Thumbnails.IMAGE_ID);
		columnIndex = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);

		GridView gridView = (GridView) findViewById(R.id.grid_view);
		gridView.setColumnWidth(getColumnWidth());
		gridView.setAdapter(new GAdapter(mContext));
	}

	private int getColumnWidth() {
		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int colWidth = (screenWidth - 50) / 2;
		return colWidth;
	}

	private int getColumnHeight() {
		Display display = getWindowManager().getDefaultDisplay();
		int screenHeight = display.getHeight();
		int colHeight = (screenHeight - 30) / 3;
		return colHeight;
	}

	private class GAdapter extends BaseAdapter {
		private Context context;

		public GAdapter(Context localContext) {
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

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			FrameLayout frame = (FrameLayout) inflater.inflate(
					R.layout.custom_gallery_item, null);
			TextView v = (TextView) frame.getChildAt(0);
			cursor.moveToPosition(position);
			int imageID = cursor.getInt(columnIndex);
			// v.setImageURI(Uri.withAppendedPath(
			// MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" +
			// imageID));

			Drawable d = new BitmapDrawable(readBitmap(Uri.withAppendedPath(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, ""
							+ imageID)));
			v.setBackgroundDrawable(d);
			v.setLayoutParams(new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.FILL_PARENT, getColumnHeight()));

			CheckBox cb = (CheckBox) frame.getChildAt(1);
			cb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					CheckBox cb = (CheckBox) arg0;
					boolean cbVal = !cb.isChecked();
					
					GridView gridView = (GridView) findViewById(R.id.grid_view);
					for (int i = 0; i < gridView.getChildCount(); i++) {
						FrameLayout fr = (FrameLayout) gridView.getChildAt(i);
						CheckBox c = (CheckBox) fr.getChildAt(1);
						c.setChecked(false);
					}
					if(cbVal){
						GlobalState.getInstance().currentBitmap = null;
						cb.setChecked(false);
					}
					else
					{
						FrameLayout f = (FrameLayout)cb.getParent();
						TextView tv = (TextView)f.getChildAt(0);
						Bitmap b = ((BitmapDrawable)tv.getBackground()).getBitmap();
						GlobalState.getInstance().currentBitmap = b;
						cb.setChecked(true);
					}
				
					
				}
			});
			return frame;
		}

	}

	public Bitmap readBitmap(Uri selectedImage) {
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2; // reduce quality
		AssetFileDescriptor fileDescriptor = null;
		try {
			fileDescriptor = this.getContentResolver().openAssetFileDescriptor(
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
