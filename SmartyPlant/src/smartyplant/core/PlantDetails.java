package smartyplant.core;

import smartyplant.Utils.GlobalState;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PlantDetails extends Activity{
	Context mContext = this;
	GlobalState globalState = GlobalState.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plant_details);
		
		Button prev = (Button)findViewById(R.id.prev);
		prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(globalState.currentIndex > 0)
				{
					globalState.currentIndex--;
					globalState.currentPlant = globalState.all_plants.get(globalState.currentIndex);
					finish();
					startActivity(new Intent(mContext, PlantDetails.class));
				}
				else
				{
					globalState.currentIndex = globalState.all_plants.size()-1;
					globalState.currentPlant = globalState.all_plants.get(globalState.currentIndex);
					finish();
					startActivity(new Intent(mContext, PlantDetails.class));
				}
			}
		});
		
		Button next = (Button)findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(globalState.currentIndex < globalState.all_plants.size()-1)
				{
					globalState.currentIndex++;
					globalState.currentPlant = globalState.all_plants.get(globalState.currentIndex);
					finish();
					startActivity(new Intent(mContext, PlantDetails.class));
				}
				else
				{
					globalState.currentIndex = 0;
					globalState.currentPlant = globalState.all_plants.get(globalState.currentIndex);
					finish();
					startActivity(new Intent(mContext, PlantDetails.class));
				}
			}
		});
		TextView label1 = (TextView)findViewById(R.id.label1);
		label1.setText(GlobalState.getInstance().currentPlant.identifier_name);
		TextView label3 = (TextView)findViewById(R.id.label3);
		label3.setText(GlobalState.getInstance().currentPlant.plant_name);
		
		TextView image = (TextView)findViewById(R.id.image_view);
		image.setBackgroundDrawable(globalState.currentPlant.image_drawable);
		
	}

}
