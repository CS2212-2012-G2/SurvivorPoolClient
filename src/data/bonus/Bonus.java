package data.bonus;

import java.util.Vector;

import net.rim.device.api.io.FileNotFoundException;
import net.rim.device.api.util.Arrays;
import net.rim.device.api.util.Comparator;
import client.data.JSONUtils;
import data.me.json.JSONArray;
import data.me.json.JSONException;
import data.me.json.JSONObject;

/**
 * This class holds all the bonus questions
 * @author Ramesh
 *
 */
public class Bonus {

	static Vector questions = new Vector();
	
	private static final String KEY_QUESTIONS = "questions";
	public static final String  filePath 	  = "file:///SDCard/res/data/Bonus.dat";
	static Comparator comp= new Comparator(){

		public int compare(Object o1, Object o2) {
			BonusQuestion b1 = (BonusQuestion) o1;
			BonusQuestion b2 = (BonusQuestion) o2;
			int weekDiff = b1.getWeek()-b2.getWeek();
			if(weekDiff==0){
				return b1.getNumber()-b2.getNumber();
			}else{
				return b1.getWeek()-b2.getWeek();
			}	
		}
		
	};
	/**
	 * DO NOT CALL THIS FUNCTION! Only used fromJSONObject or when a bonusquestion is created
	 * @param b
	 */
	public static void addNewQuestion(BonusQuestion b){
		questions.addElement(b);
		sortQuestions();
	}
	
	private static void sortQuestions(){
		Object[] unsorted = new Object[questions.size()];
		questions.copyInto(unsorted);
		Arrays.sort(unsorted,comp);
	}
	
	/**
	 * Get all the questions. Probably not as useful as get by week
	 * @return
	 */
	public static Vector getAllQuestions(){
		return questions;
	}
	
	public static JSONObject toJSONObject() throws JSONException{
		JSONObject obj = new JSONObject();
		
		JSONArray qA = new JSONArray();
		
		for(int i=0;i<questions.size();i++){
			BonusQuestion b = (BonusQuestion) questions.elementAt(i);
			qA.put(b.toJSONObject());
		}
		obj.put(KEY_QUESTIONS, qA);
		return obj;
	}
	
	public static void fromJSONObject(JSONObject o) throws JSONException{
		if(o==null)
			return;
		JSONArray qA = o.getJSONArray(KEY_QUESTIONS);
		for(int i =0;i<qA.length();i++){
			BonusQuestion b = new BonusQuestion();
			b.fromJSONObject(qA.getJSONObject(i));
			addNewQuestion(b);
		}
	}
	
	/**
	 * Initalize bonus
	 */
	public static void initBonus(){
		System.out.println("Bonus init start");
		try {
			fromJSONObject(JSONUtils.readFile(filePath,false));
		} catch (FileNotFoundException e) {
			System.out.println("could not read "+filePath);
		} catch (JSONException e) {
			System.out.println("could not convert to json object "+filePath);
			e.printStackTrace();
		}
	}
}
