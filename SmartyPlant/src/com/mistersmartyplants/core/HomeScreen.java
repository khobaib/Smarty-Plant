package com.mistersmartyplants.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.anim;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.SubMenu;
import com.bugsense.trace.BugSenseHandler;
import com.facebook.Session;
import com.mistersmartyplants.adapter.GalleryAdapter;
import com.mistersmartyplants.adapter.ImageAdapter;
import com.mistersmartyplants.adapter.NothingSelectedSpinnerAdapter;
import com.mistersmartyplants.model.BriefedPlant;
import com.mistersmartyplants.model.ServerResponse;
import com.mistersmartyplants.parser.JsonParser;
import com.mistersmartyplants.utility.Constants;
import com.mistersmartyplants.utility.GlobalState;
import com.mistersmartyplants.utility.SmartyPlantApplication;

public class HomeScreen extends SherlockActivity implements ActionBar.TabListener {

    private Uri imageUri;
    private static final int CAMERA_REQ_CODE = 901;
    private static final int GALLERY_REQ_CODE = 902;

    final String[] uploadParams = {"Capture Image", "Select from gallery"};

    GridView gridView;
    Gallery gallery;
    Context mContext = this;
    int currentTab = -1;
    int mode = 1;
    //	File PhotoDir = new File(Environment.getExternalStorageDirectory(),
    //			"smarty_plant_uploads");
    //	String currentImagePath = "";
    int currentIndex;

    SmartyPlantApplication appInstance;
    JsonParser jsonParser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        BugSenseHandler.initAndStartSession(mContext, "f2391cbb");

        if(Constants.DEBUG){
            Log.d(Constants.LOG_TAG, "onCreate");
        }

        setTheme(R.style.Theme_Smarty_plants_style);
        super.onCreate(savedInstanceState);
        //		BugSenseHandler.initAndStartSession(HomeScreen.this, "8e103589");
        setContentView(R.layout.home_screen);
        appInstance = (SmartyPlantApplication) getApplication();
        jsonParser = new JsonParser();

        //		PhotoDir.mkdirs();
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
        currentIndex = 0;
        currentTab = tab.getPosition();
        if (tab.getPosition() <= 1) {
            setContentView(R.layout.my_plants_layout);
            Button loadMore = (Button) findViewById(R.id.load_more_button);
            loadMore.setVisibility(Button.VISIBLE);
            loadMore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    PlantsTask task = new PlantsTask();
                    task.execute();
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

        }  else {
            // GlobalState.getInstance().currentBitmaps.clear();
            setPhotoCaptureMode();

        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
    }

    private class PlantsTask extends AsyncTask<Void, Void, Void> implements
    DialogInterface.OnCancelListener {

        GlobalState globalState = GlobalState.getInstance();
        ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {
        	try{
            dialog = new ProgressDialog(mContext);
            dialog.setTitle(" ");
            dialog.setIcon(R.drawable.icon);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Loading Data");
            dialog.show();
        	}
        	catch(Exception e){
        		Log.d("smarty-plants", e.getMessage());
        	}
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ServerResponse response = null;
                String url = Constants.METHOD_PLANT;
                List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
                currentIndex = currentIndex + 6;

                if (currentTab == 0) {

                    urlParam.add(new BasicNameValuePair("flag", "unsolved"));
                    urlParam.add(new BasicNameValuePair("number_of_record", ""
                            + currentIndex));

                    response = jsonParser.retrieveServerData(1,
                            Constants.REQUEST_TYPE_GET, url, urlParam, null,
                            appInstance.getAccessToken());

                    // globalState.all_plants = dataConnector
                    // .getPlantsPartial(GlobalState.PLANTS_UNSOLVED);
                } else {
                    urlParam.add(new BasicNameValuePair("flag", "allmine"));
                    urlParam.add(new BasicNameValuePair("number_of_record", ""
                            + currentIndex));

                    response = jsonParser.retrieveServerData(1,
                            Constants.REQUEST_TYPE_GET, url, urlParam, null,
                            appInstance.getAccessToken());
                    // globalState.all_plants = dataConnector
                    // .getPlants(GlobalState.PLANTS_ALL_MINE);
                }

                if (response.getStatus() == 200) {

                    JSONObject responseObj = response.getjObj();
                    String responseStatus = responseObj.optString("response");

                    if (responseStatus.equalsIgnoreCase("success")) {
                        JSONArray arr = responseObj
                                .optJSONArray("plant_details");
                        globalState.all_plants.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            BriefedPlant p = new BriefedPlant();
                            p.plant_id = obj.getInt("plant_id");
                            p.plant_name = obj.getString("plant_name");
                            p.image_url = "http://mistersmartyplants.com"
                                    + obj.getString("image_url").replaceAll(
                                            "~", "");
                            p.identifier_name = obj
                                    .getString("identifier_name");
                            p.identifier_twitter_url = obj
                                    .getString("identifier_twitter_url");
                            String identifier_picture_url = obj
                                    .optString("identifier_picture_url");
                            if (identifier_picture_url.equalsIgnoreCase("")) {
                                p.identifier_picture_url = "http://mistersmartyplants.com/images/default_person.jpg";
                            } else {
                                p.identifier_picture_url = "http://mistersmartyplants.com"
                                        + obj.getString(
                                                "identifier_picture_url")
                                                .substring(2);
                            }
                            String num = obj.getString(
                                    "plant_name_agree_percentage").replaceAll(
                                            "%", "");
                            int prc = Integer.parseInt(num);
                            p.plant_name_agree_prc = prc;
                            globalState.all_plants.add(p);
                        }
                    }
                    else{
                        globalState.all_plants.clear();

                    }

                }

            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(gridView.getAdapter() == null){

                gridView.setAdapter(new com.mistersmartyplants.adapter.ImageAdapter(
                        mContext, getColumnWidth(), getColumnHeight(),
                        globalState.all_plants));
            }
            else
            {
                ImageAdapter adapter = (ImageAdapter) gridView.getAdapter();
                adapter.updatePlants(globalState.all_plants);
            }

            if (currentIndex > globalState.all_plants.size())
            {
                Button load_more = (Button)findViewById(R.id.load_more_button);
                load_more.setVisibility(Button.GONE);
            }

            try {
                dialog.dismiss();
                dialog = null;
                if (globalState.all_plants.size() == 0)
                    Toast.makeText(mContext, "No Plants to show", 3000).show();
            } catch (Exception e) {

            }
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

        final Spinner sUpload = (Spinner) findViewById(R.id.s_upload);
        ArrayAdapter<String> sAdapter= new ArrayAdapter<String>(HomeScreen.this, android.R.layout.simple_spinner_dropdown_item, uploadParams);
        sUpload.setAdapter(new NothingSelectedSpinnerAdapter(sAdapter, R.layout.spinner_row_nothing_selected,                         
                HomeScreen.this));

        sUpload.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                switch(pos){
                    case 1:
                        startCameraActivity();
                        break;
                    case 2:
                        setGallerySelectMode();
                        break;
                    default:
                        break;                        
                }
                sUpload.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        Button menu_button = (Button) findViewById(R.id.upload_image);
        menu_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sUpload.performClick();

            }
        });


        Button menu_button2 = (Button) findViewById(R.id.upload_image2);
        menu_button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sUpload.performClick();
                //                registerForContextMenu(v);
                //                openContextMenu(v);
                //                unregisterForContextMenu(v);
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
        LinearLayout noteText = (LinearLayout)findViewById(R.id.note_text);
        Button upload = (Button) findViewById(R.id.upload_image);
        Button upload2 = (Button) findViewById(R.id.upload_image2);
        if (GlobalState.getInstance().currentBitmaps.size() > 0) {
            upload.setVisibility(Button.INVISIBLE);
            upload2.setVisibility(Button.VISIBLE);
            done.setVisibility(Button.VISIBLE);
            noteText.setVisibility(LinearLayout.GONE);
        } else {
            upload.setVisibility(Button.VISIBLE);
            upload2.setVisibility(Button.GONE);
            done.setVisibility(Button.GONE);
            noteText.setVisibility(LinearLayout.VISIBLE);
        }
    }

    protected void startCameraActivity() {
        mode = 1;
        // File file = new File(PhotoDir, "image_"+new Date().getTime()+".jpg");

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File directory = Constants.APP_DIRECTORY;
        String mainDir = directory.toString();

        long currentTime = System.currentTimeMillis();
        String photoFileName = currentTime + ".png";

        File photoFile = new File(mainDir, photoFileName);
        imageUri = Uri.fromFile(photoFile);
        if(Constants.DEBUG){
            Log.d(">>>>>>>>>>>>>", "path of photofolder = " + photoFile.getPath());
            Log.d(">>>>>>>>>>>>>", "path of photofolder = " + imageUri.getPath());
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);    
        startActivityForResult(intent, CAMERA_REQ_CODE);  

        //		String timetamp = new Date().getTime() + "";
        //		File file = new File(PhotoDir, "//img" + timetamp + ".jpeg");
        //		// file.mkdirs();
        //		currentImagePath = file.getPath();
        //		Uri outputFileUri = Uri.fromFile(file);
        //		Intent intent = new Intent(
        //				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        //		startActivityForResult(intent, 1);
    }

    private void setGallerySelectMode() {
        mode = 2;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,GALLERY_REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // setPhotoCaptureMode();
        if(Constants.DEBUG){
            Log.d(Constants.LOG_TAG, "on onActivityResult");
        }
        Button done = (Button) findViewById(R.id.done);
        Button upload = (Button) findViewById(R.id.upload_image);
        Button upload2 = (Button) findViewById(R.id.upload_image2);
        LinearLayout noteText = (LinearLayout)findViewById(R.id.note_text);
        gallery = (Gallery) findViewById(R.id.gallery_view);
        if(Constants.DEBUG){
            Log.d(Constants.LOG_TAG, "Gallery" + gallery);
            Log.d(Constants.LOG_TAG, "result code = " + resultCode);
        }

        switch (resultCode) {
            case 0:
                if (GlobalState.getInstance().currentBitmaps.size() > 0) {
                    upload.setVisibility(Button.INVISIBLE);
                    upload2.setVisibility(Button.VISIBLE);
                    done.setVisibility(Button.VISIBLE);
                    noteText.setVisibility(LinearLayout.GONE);
                } else {
                    upload.setVisibility(Button.VISIBLE);
                    upload2.setVisibility(Button.GONE);
                    done.setVisibility(Button.GONE);
                    noteText.setVisibility(LinearLayout.VISIBLE);

                }
                break;

            case RESULT_OK:

                try {
                    if (requestCode == CAMERA_REQ_CODE) {
                        //			        GlobalState.getInstance().addBitmap(currentImagePath);
                        if(Constants.DEBUG){
                            Log.d(Constants.LOG_TAG, "imageUri.getPath() = " + imageUri.getPath());
                        }
                        GlobalState.getInstance().addBitmap(imageUri.getPath());
                        GalleryAdapter adapter = (GalleryAdapter) HomeScreen.this.gallery.getAdapter();
                        adapter.notifyDataSetChanged();
                        //					onPhotoTaken();
                        if (GlobalState.getInstance().currentBitmaps.size() > 0) {
                            upload.setVisibility(Button.INVISIBLE);
                            upload2.setVisibility(Button.VISIBLE);
                            done.setVisibility(Button.VISIBLE);

                            noteText.setVisibility(LinearLayout.GONE);
                        } else {
                            upload.setVisibility(Button.VISIBLE);
                            upload2.setVisibility(Button.GONE);
                            done.setVisibility(Button.GONE);

                            noteText.setVisibility(LinearLayout.VISIBLE);
                        }
                    } else {
                        if (resultCode == RESULT_OK) {
                            Uri selectedImage = data.getData();
                            GlobalState.getInstance().addBitmap(pathFromUri(selectedImage));
                            GalleryAdapter adapter = (GalleryAdapter) HomeScreen.this.gallery.getAdapter();
                            adapter.notifyDataSetChanged();
                            if (GlobalState.getInstance().currentBitmaps.size() > 0) {
                                upload.setVisibility(Button.INVISIBLE);
                                upload2.setVisibility(Button.VISIBLE);
                                done.setVisibility(Button.VISIBLE);

                                noteText.setVisibility(LinearLayout.GONE);
                            } else {
                                upload.setVisibility(Button.VISIBLE);
                                upload2.setVisibility(Button.GONE);
                                done.setVisibility(Button.GONE);

                                noteText.setVisibility(LinearLayout.VISIBLE);
                            }

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }


    //    @Override
    //    public void onCreateContextMenu(ContextMenu menu, View v,
    //            ContextMenuInfo menuInfo) {
    //        super.onCreateContextMenu(menu, v, menuInfo);
    //        MenuInflater inflater = getMenuInflater();
    //        inflater.inflate(R.menu.upload_image_menu, menu);
    //    }

    //    @Override
    //    public boolean onContextItemSelected(MenuItem item) {
    //        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    //        switch (item.getItemId()) {
    //            case R.id.capture_image:
    //                startCameraActivity();
    //                return true;
    //            case R.id.gallery_image:
    //                setGallerySelectMode();
    //                return true;
    //            default:
    //                return super.onContextItemSelected(item);
    //        }
    //    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String pathFromUri(Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SubMenu subMenu1 = menu.addSubMenu("Log out");

        subMenu1.setIcon(R.drawable.actionbar);
        com.actionbarsherlock.view.MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.actionbar);
        subMenu1Item
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        // getSupportMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(
            com.actionbarsherlock.view.MenuItem item) {
        appInstance.setCredentials("", "");
        appInstance.setRememberMe(false);

        Session session = Session.getActiveSession();
        if (session!=null && !session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
        finish();
        startActivity(new Intent(mContext, Login.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        BugSenseHandler.closeSession(HomeScreen.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //        Log.d("<<<<<< SMARTY PLANT >>>>>>>", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //        Log.d("<<<<<< SMARTY PLANT >>>>>>>", "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        //        Log.d("<<<<<< SMARTY PLANT >>>>>>>", "onStop");
        BugSenseHandler.closeSession(HomeScreen.this);
    }
}
