package data.bonus;

/**
 * UserAnswer stores the all the given answers of ONE user.
 * @author Ramesh Raj
 */
import net.rim.device.api.util.Comparator;
import data.me.json.JSONException;
import data.me.json.JSONObject;

public class UserAnswer {

	private int week;
	private int num;
	private String answer;
	
	private final String KEY_NUM = "num",
						 KEY_WEEK = "week",
						 KEY_ANSWER="answer";
					
	
	public static Comparator comp=new Comparator(){

		public int compare(Object o1, Object o2) {
			UserAnswer b1 = (UserAnswer) o1;
			UserAnswer b2 = (UserAnswer) o2;
			int weekDiff = b1.getWeek()-b2.getWeek();
			if(weekDiff==0){
				return b1.getNum()-b2.getNum();
			}else{
				return weekDiff;
			}	
		}
		
	};
	
	/**
	 * Create a new user answer
	 * @param week The week of the bonus question
	 * @param num the bonus question number
	 * @param answer the answer the user provided
	 */
	public UserAnswer(int week,int num,String answer){
		this.answer= answer;
		this.week = week;
		this.num=num;
	}
	
	/**
	 * Used mostly for comparing
	 * @param week
	 * @param num
	 */
	public UserAnswer(int week,int num){
		this.num= num;
		this.week = week;
	}
	
	/**
	 * Used mostly for json
	 */
	public UserAnswer() {}

	public int getWeek() {
		return week;
	}

	/**
	 * Set the week of the question this answers
	 * @param week
	 */
	public void setWeek(int week) {
		this.week = week;
	}

	/**
	 * Get the question number in the week this answers
	 * @return
	 */
	public int getNum() {
		return num;
	}

	/**
	 * Set the question number in the week this answers
	 * @param num
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * Get the answer
	 * @return
	 */
	public String getAnswer() {
		return answer;
	}
	
	/**
	 * Set the answer
	 * @param answer
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
	/**
	 * Converts the useranswer object to a valid json object
	 * @return a json object with all the necessary data
	 * @throws JSONException
	 */
	public JSONObject toJSONObject() throws JSONException {
		JSONObject o = new JSONObject();
		o.put(KEY_ANSWER, answer);
		o.put(KEY_WEEK, week);
		o.put(KEY_NUM, num);
		return o;
	}
	
	/**
	 * Converts a json object to an UserAnswer object
	 * @param o a valid json object
	 * @throws JSONException
	 */
	public void fromJSONObject(JSONObject o) throws JSONException {
		answer = o.getString(KEY_ANSWER);
		week = o.getInt(KEY_WEEK);
		num = o.getInt(KEY_NUM);
		
	}
}
