package smartyplant.adapters;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smartyplant.Utils.GlobalState;
import smartyplant.modules.BriefedPlant;
import android.content.Context;

public class PaginationController {

	public final int INITIAL_LOAD_COUNT = 6;
	public final int EXTRA_LOAD_COUNT = 4;
	public JSONArray orginalArray;
	private int totalCount;
	private int currentIndex;
	public ImageAdapter adapter;
	GlobalState globalState = GlobalState.getInstance();
	ArrayList<BriefedPlant> plants = new ArrayList<BriefedPlant>();
	private static PaginationController instance = new PaginationController();

	public static PaginationController getInstance() {
		return instance;
	}

	public void initialLoad(Context c,int  width,int height) {
		plants = new ArrayList<BriefedPlant>();
		if (globalState.all_plants.size() > INITIAL_LOAD_COUNT) {
			for (int i = 0; i < INITIAL_LOAD_COUNT; i++) {
				plants.add(globalState.all_plants.get(i));
			}
			currentIndex = INITIAL_LOAD_COUNT;
		} else {
			plants = globalState.all_plants;
			currentIndex = plants.size();
		}
		adapter = new ImageAdapter(c,width, height,plants);
	}

	public void extraLoad() throws Exception {
		
		if ((orginalArray.length() - currentIndex) > EXTRA_LOAD_COUNT) {
			for (int i = currentIndex; i < (currentIndex + EXTRA_LOAD_COUNT); i++) {
				JSONObject obj = orginalArray.getJSONObject(i);
				BriefedPlant p = new BriefedPlant();
				p.plant_id = obj.getInt("plant_id");
				p.plant_name = obj.getString("plant_name");
				p.image_url = "http://mistersmartyplants.com"
						+ obj.getString("image_url").replaceAll("~", "");

				p.identifier_name = obj.getString("identifier_name");
				p.identifier_twitter_url = obj.getString("identifier_twitter_url");
				p.identifier_picture_url = "http://mistersmartyplants.com"
						+ obj.getString("identifier_picture_url").substring(2);

				String num = obj.getString("plant_name_agree_percentage")
						.replaceAll("%", "");

				int prc = Integer.parseInt(num);
				p.plant_name_agree_prc = prc;
				globalState.all_plants.add(p);
				//plants.add(globalState.all_plants.get(i));
			}
			currentIndex = currentIndex + EXTRA_LOAD_COUNT;
		} else {
			plants = globalState.all_plants;
			currentIndex = plants.size();
		}
		adapter.updatePlants(plants);
	}

}
