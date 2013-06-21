package smartyplant.modules;

import java.util.ArrayList;

public class DetailedPlant extends Plant {

	public ArrayList<String> imageUrls = new ArrayList<String>();
	public String country;
	public String region;
	public String city;
	public String state;
	public String description;
	public String group_id;
	public ArrayList<Vote> votes = new ArrayList<Vote>();

}
