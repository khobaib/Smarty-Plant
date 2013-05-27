package smartyplant.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import smartyplant.Network.DataConnector;
import smartyplant.Utils.GlobalState;
import smartyplant.adapters.GalleryAdapter;
import smartyplant.adapters.PaginationController;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

public class HomeScreen extends SherlockActivity implements
		ActionBar.TabListener {
	GridView gridView;
	Gallery gallery;
	Context mContext = this;
	int currentTab = -1;
	int mode = 1;
	String PhotoPath = Environment.getExternalStorageDirectory()
			+ "//smarty_plant.jpg";

	PaginationController pController = PaginationController.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		ActionBar bar = getSupportActionBar();
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayUseLogoEnabled(false);
		Bitmap b = BitmapFactory.decodeResource(getResources(),
				R.drawable.actionbar_bg);
		BitmapDrawable d = new BitmapDrawable(b);
		bar.setBackgroundDrawable(d);
		bar.setLogo(null);
		LayoutInflater inflater = getLayoutInflater();
		View logoView = inflater.inflate(R.layout.bar, null, false);
		ViewGroup decorViewGroup = (ViewGroup) ((SherlockActivity) this)
				.getWindow().getDecorView();
		decorViewGroup.addView(logoView);

		ActionBar.Tab tab0 = getSupportActionBar().newTab();
		tab0.setText("ID Plants");
		tab0.setTabListener(this);
		getSupportActionBar().addTab(tab0);

		ActionBar.Tab tab1 = getSupportActionBar().newTab();
		tab1.setText("My Plants");
		tab1.setTabListener(this);
		getSupportActionBar().addTab(tab1);

		ActionBar.Tab tab2 = getSupportActionBar().newTab();
		tab2.setText("Submit Plant");
		tab2.setTabListener(this);
		getSupportActionBar().addTab(tab2);

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction transaction) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {

		currentTab = tab.getPosition();
		if (tab.getPosition() == 0) {
			setContentView(R.layout.my_plants_layout);
			Button loadMore = (Button) findViewById(R.id.load_more_button);
			loadMore.setVisibility(Button.VISIBLE);
			loadMore.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View arg0) {
					try {
						pController.extraLoad();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					gridView.setAdapter(pController.adapter);
				}
			});
			
			
			gridView = (GridView) findViewById(R.id.grid_view);
			gridView.setColumnWidth(getColumnWidth());
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					GlobalState.getInstance().currentPlant = GlobalState
							.getInstance().all_plants.get(arg2);
					GlobalState.getInstance().currentIndex = arg2;
					startActivity(new Intent(mContext, PlantDetails.class));
				}
			});
			PlantsTask task = new PlantsTask();
			task.execute();

		} else if (tab.getPosition() == 1) {
			setContentView(R.layout.my_plants_layout);
			gridView = (GridView) findViewById(R.id.grid_view);
			gridView.setColumnWidth(getColumnWidth());
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					GlobalState.getInstance().currentPlant = GlobalState
							.getInstance().all_plants.get(arg2);
					GlobalState.getInstance().currentIndex = arg2;
					startActivity(new Intent(mContext, PlantDetails.class));
				}
			});
			PlantsTask task = new PlantsTask();
			task.execute();

		} else {
			setPhotoCaptureMode();
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
	}

	private class PlantsTask extends AsyncTask<Void, Void, Void> implements
			DialogInterface.OnCancelListener {

		GlobalState globalState = GlobalState.getInstance();
		DataConnector dataConnector = DataConnector.getInstance();
		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setTitle(" ");
			dialog.setIcon(R.drawable.logo);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Loading Data");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (currentTab == 0)
					globalState.all_plants = dataConnector
							.getPlantsPartial(GlobalState.PLANTS_UNSOLVED);
				else
					globalState.all_plants = dataConnector
							.getPlants(GlobalState.PLANTS_ALL_MINE);

			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (currentTab == 0) {
				pController.initialLoad(mContext, getColumnWidth(), getColumnHeight());
				gridView.setAdapter(pController.adapter);
				
			} else {
				gridView.setAdapter(new smartyplant.adapters.ImageAdapter(
						mContext, getColumnWidth(), getColumnHeight(),
						globalState.all_plants));
			}
			dialog.dismiss();

		}

		@Override
		public void onCancel(DialogInterface arg0) {

		}

	}

	private int getColumnWidth() {
		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int colWidth = (screenWidth) / 3;
		return colWidth;
	}

	private int getColumnHeight() {
		Display display = getWindowManager().getDefaultDisplay();
		int screenH = display.getHeight();
		int colH = (screenH) / 3;
		return colH;
	}

	// ========================== Submit Plants Functions =============
	private void setPhotoCaptureMode() {
		setContentView(R.layout.submit_plants_layout);
		setClickListners();
		setGallery();
	}

	private void setGallery() {
		GalleryAdapter galleryAdapter = new GalleryAdapter(mContext, 1);
		gallery = (Gallery) findViewById(R.id.gallery_view);
		gallery.setAdapter(galleryAdapter);
	}

	private void setClickListners() {
		Button takePic = (Button) findViewById(R.id.take_picture);
		takePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// GlobalState.getInstance().currentBitmap = null;
				startCameraActivity();
			}
		});

		Button gallery = (Button) findViewById(R.id.select_gallery);
		gallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// GlobalState.getInstance().currentBitmap = null;
				setGallerySelectMode();
			}
		});

		Button done = (Button) findViewById(R.id.done);
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (GlobalState.getInstance().currentBitmaps.size() > 0) {
					startActivity(new Intent(mContext, Submit.class));
				}

				else {
					Toast.makeText(mContext,
							"Please Capture or Select Image first",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	protected void startCameraActivity() {
		mode = 1;
		File file = new File(PhotoPath);
		Uri outputFileUri = Uri.fromFile(file);
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, 0);
	}

	private void setGallerySelectMode() {
		mode = 2;
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		setPhotoCaptureMode();
		Button done = (Button) findViewById(R.id.done);

		switch (resultCode) {
		case 0:
			if (GlobalState.getInstance().currentBitmaps.size() > 0)
				done.setVisibility(Button.VISIBLE);
			else
				done.setVisibility(Button.INVISIBLE);
			break;

		case -1:

			try {
				if (mode == 1)
					onPhotoTaken();
				else {
					if (resultCode == RESULT_OK) {
						Uri selectedImage = data.getData();
						InputStream imageStream = getContentResolver()
								.openInputStream(selectedImage);
						Bitmap selectedBitmap = BitmapFactory
								.decodeStream(imageStream);
						GlobalState.getInstance().addBitmap(selectedBitmap);
						GalleryAdapter adapter = (GalleryAdapter) this.gallery
								.getAdapter();
						adapter.notifyDataSetChanged();

						if (GlobalState.getInstance().currentBitmaps.size() > 0)
							done.setVisibility(Button.VISIBLE);
						else
							done.setVisibility(Button.INVISIBLE);
						break;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void onPhotoTaken() throws Exception {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bitmap = BitmapFactory.decodeFile(PhotoPath, options);

		GlobalState.getInstance().addBitmap(bitmap);
		GalleryAdapter adapter = (GalleryAdapter) this.gallery.getAdapter();
		adapter.notifyDataSetChanged();
	}

}
