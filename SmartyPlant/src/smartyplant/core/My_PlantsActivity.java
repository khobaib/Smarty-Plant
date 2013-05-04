package smartyplant.core;

import smartyplant.Network.DataConnector;
import smartyplant.Utils.GlobalState;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

public class My_PlantsActivity extends Activity {

	GridView gridView;
	Context mContext = this;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_plants_layout);
		gridView = (GridView) findViewById(R.id.grid_view);
		gridView.setColumnWidth(getColumnWidth());
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				GlobalState.getInstance().currentPlant = GlobalState.getInstance().all_plants.get(arg2);
				GlobalState.getInstance().currentIndex = arg2;
				
				
				
				startActivity(new Intent(mContext, PlantDetails.class));
				
			}
		});
		
		
		PlantsTask task = new PlantsTask();
		task.execute();

	}

	private int getColumnWidth(){
		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int colWidth = (screenWidth-50) / 2;
		
		return colWidth;
	}


	private class PlantsTask extends AsyncTask<Void, Void, Void> implements
			DialogInterface.OnCancelListener {

		GlobalState globalState = GlobalState.getInstance();
		DataConnector dataConnector = DataConnector.getInstance();

		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setTitle("Smarty Plants");
			dialog.setIcon(R.drawable.logo);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
			dialog.setMessage("Loading Data");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				globalState.all_plants = dataConnector.getAllMyPlants();
			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			gridView.setAdapter(new smartyplant.core.ImageAdapter(mContext));
			dialog.dismiss();
		}

		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub

		}

	}
}