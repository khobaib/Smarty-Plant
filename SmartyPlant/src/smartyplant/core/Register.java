package smartyplant.core;

import smartyplant.api.ApiConnector;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class Register extends SherlockActivity {

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        
        // retrieve Connector Instance
        ApiConnector connector = ApiConnector.getInstance();
        
        
        ActionBar bar = getSupportActionBar();
        BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.actionbar);
        bar.setBackgroundDrawable(bg);
        bar.setIcon(R.drawable.logo);
        
        bar.setDisplayShowTitleEnabled(false);
    }
}
