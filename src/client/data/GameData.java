package client.data;

/**@author Ramesh Raj, Kevin Brightwell
 */
import java.util.Vector;

import net.rim.device.api.io.FileNotFoundException;
import client.Common;
import data.bonus.Bonus;
import data.me.json.JSONException;
import data.me.json.JSONObject;

public class GameData extends data.GameData {

	private Vector allList;
	public static String filePath = "file:///SDCard/res/data/GameData.dat";
	
	public GameData(int numContestants) {
		super(numContestants);
		
		allList = allContestants;
	}
	
	/**
	 * initGameData reads in a data file and builds a GameData object out
	 * of it, returning it to the user.
	 * 
	 * @param inputFile   file to be read in
	 * @return GameData object made out of file or null if season not created
	 * 
	 */
	public static boolean initGameData() {
		JSONObject json;
		
		try {
			json = JSONUtils.readFile(filePath,true);
			if(json==null){
				return false;
			}
		} catch (FileNotFoundException e) {	
			return false;
		}
		
		try {
			if (!json.getBoolean(KEY_SEASON_STARTED)){
				Common.displayErrorMsg("Season has not started.");
				return false;
			}
			currentGame = new GameData(json.getInt(KEY_NUM_CONTEST));
		} catch (JSONException e) {
			return false;
		}
		
		try {
			GameData.getCurrentGame().fromJSONObject(json);
		} catch (JSONException e) {
			Common.displayErrorMsg("Corrupted file.");
			return false;
		}
		
		Bonus.initBonus();//initalize bonus
		return true;
	}

	public void fromJSONObject(JSONObject obj) throws JSONException {
		super.fromJSONObject(obj);
		allList = allContestants;
		
	}
	
	public void writeData() {
		try {
			JSONUtils.writeData();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

}
