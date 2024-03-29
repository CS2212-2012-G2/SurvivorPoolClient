package data;

import java.util.Vector;

import net.rim.device.api.util.SimpleSortingVector;

import common.Utils;

import data.bonus.Bonus;
import data.bonus.BonusQuestion;
import data.bonus.UserAnswer;
import data.me.json.JSONArray;
import data.me.json.JSONException;
import data.me.json.JSONObject;

/**
 * The user class will be used to create an individual who will be participating
 * in the survivor pool.
 * 
 * @author Graem Littleton, Kevin Brightwell, Jonathan Demelo, Ramesh Raj,
 *         
 */

public class User implements Person {

	private String firstName, lastName, unID; // first and last names and unique
												// ID (UWO ID format)
	private int points; // point total, points user receives if that
									// pick wins
	protected int ultPoints;
	
	private Contestant ultPick, weeklyPick; // users pick of the winner and
										    // their weekly pick
	private int numBonusAnswered = 0;
	
	private SimpleSortingVector answers = new SimpleSortingVector();
	private final String answerPath = "file:///SDCard/res/data/UserAnswer";
	// JSON Keys:
	
	protected static final String KEY_ID = "id";
	protected static final String KEY_FIRST_NAME = "first";
	protected static final String KEY_LAST_NAME = "last";
	protected static final String KEY_POINTS = "curr_points";
	protected static final String KEY_WIN_PICK_POINTS = "win_points";
	protected static final String KEY_ULT_PICK_ID	= "ult_pick";
	protected static final String KEY_WEEKLY_PICK_ID = "week_pick";
	protected static final String KEY_NUM_BONUS_ANSWER = "num_bonus_answer";
	
	//used for bonus answers
	protected static final String KEY_ANSWERS = "answers";

	/**
	 * Constructor method for the type User sets names, initializes points
	 * 
	 * @param first
	 *            first name
	 * @param last
	 *            last name
	 * @param id
	 *            unique ID
	 * @throws InvalidFieldException Thrown if any of the parameters passed are
	 * 		invalid
	 */
	public User(String first, String last, String id) throws InvalidFieldException {
		setFirstName(first);
		setLastName(last);
		setID(id);
		setPoints(0); // begin with 0 points

	}

	/**
	 * Constructor for User with no information given.  Just sets points
	 * to 0, all other information is null.
	 */
	public User() {
		setPoints(0);
	}

	// -------------------- ACCESSOR METHODS ------------------ //

	/**
	 * getFirstName returns the first name of the user
	 * 
	 * @return this.firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * getLastName returns the users last name
	 * 
	 * @return this.lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * getID returns the users unique ID
	 * 
	 * @return this.unID
	 */
	public String getID() {
		return unID;
	}

	/**
	 * getWeeklyPick returns the users pick for which contestant will be
	 * eliminated @ return this.weeklyPick
	 */
	public Contestant getWeeklyPick() {
		return weeklyPick;
	}
	
	/**
	 * Returns the users current score
	 * @return
	 */
	public int getPoints() {
		return points;
	}
	
	/**
	 * Sets the users current score
	 * @param points
	 */
	public void setPoints(int points) {
		this.points = points;
	}


	/**
	 * getWinPick returns the users selection for which contestant will win the
	 * game
	 * 
	 * @return this.winPick
	 */
	public Contestant getUltimatePick()  {
		return ultPick;
	}
	
	

	// ---------------- MUTATOR METHODS ----------------- //

	/**
	 * addPoints add points to a users current total
	 * 
	 * @param newPoints
	 *            points to be added
	 */
	public void addPoints(int newPoints) {
		points += newPoints;
	}

	/**
	 * setFirstName sets the users first name
	 * 
	 * @param first
	 *            users first name
	 * @throws InvalidFieldException 
	 */
	public void setFirstName(String first) throws InvalidFieldException {
		if (!Utils.checkString(first, REGEX_FIRST_NAME))
			throw new InvalidFieldException("Invalid First Name (User)");
		firstName = first;
	}

	/**
	 * setFirstName sets the users last name
	 * 
	 * @param last
	 *            users last name
	 * @throws InvalidFieldException 
	 */
	public void setLastName(String last) throws InvalidFieldException {
		if (!Utils.checkString(last, REGEX_LAST_NAME))
			throw new InvalidFieldException("Invalid Last Name (User)");
		lastName = last;
	}

	/**
	 * setWeeklyPick sets the users pick for which contestant will be eliminated
	 * 
	 * @param pick
	 *            contestant choice
	 */
	public void setWeeklyPick(Contestant pick)  {
		weeklyPick = pick;
	}

	/**
	 * setWinPick sets the users choice for which contestant will win the
	 * competition. Also determines how many points the user will receive if
	 * that player wins
	 * 
	 * @param winner
	 *            contestant choice
	 * @throws InvalidFieldException If null, throws exception.
	 */
	public void setUltimatePick(Contestant winner){
		setUltimatePickNoSetPts(winner);
		if(winner!=null)
			ultPoints = 2 * GameData.getCurrentGame().weeksLeft();
	}
	/**
	 * just sets the same as prior without setting pts.
	 * @param winner
	 * @throws InvalidFieldException
	 */
	public void setUltimatePickNoSetPts(Contestant winner) {
		ultPick = winner;
	}
	
	/**
	 * Get the ultimate points based on ultimate pick
	 * @return the ultimate points
	 */
	public int getUltimatePoints() {
		return ultPoints;
	}
	
	public void setUltimatePoints(int pts) {
		ultPoints = pts;
	}

	/**
	 * Sets the user ID.
	 */
	public void setID(String id) throws InvalidFieldException {
		id = id.toLowerCase();
		if (Utils.checkString(id,REGEX_PLAYER_ID))
			unID = id;
		else 
			throw new InvalidFieldException("Invalid Player ID");
	}
	
	/**
	 * toString returns a string of the contestant's information in JSON format.
	 */
	public String toString() {
		return new String("User<FN: " + "\"" + firstName + "\"" + ", LN: " + "\"" + lastName + "\"" + 
				", Points: " + "\"" + points + "\"" + ", ID: " + "\"" + unID + "\">");
	}

	/**
	 * Get the number of bonus questions answered this week
	 * @return num questions answered
	 */
	public int getNumBonusAnswer() {
		return numBonusAnswered;
	}

	/**
	 * Set the number of bonus questions answered this week.
	 * Set this to 0 after week has advanced!
	 * @param numBonusAnswer The number of bonus questions answered
	 */
	public void setNumBonusAnswer(int numBonusAnswer) {
		this.numBonusAnswered = numBonusAnswer;
	}
	
	/**
	 * Checks to see if an answer is correct and updates
	 * values accordingly
	 * @param b The bonus question that is being answered
	 * @param curAnswer The answer the user provided
	 */
	private void checkAnswer(BonusQuestion b, String curAnswer){
		String prevAnswer = getUserAnswer(b);
		String correctAnswer = b.getAnswer();
		
		if(correctAnswer.equalsIgnoreCase(prevAnswer)){
			if(!correctAnswer.equals(curAnswer))//user previously had it correct
				numBonusAnswered--;
		}else{
			if(correctAnswer.equals(curAnswer))//user got it correct this time
				numBonusAnswered++;
		}
	}
	
	/**
	 * Gets the answer the user provided
	 * @param numQuestion The question number
	 * @return
	 */
	public String getUserAnswer(BonusQuestion b){
		if(answers.size()==0)
			return null;
		UserAnswer uA = new UserAnswer(b.getWeek(),b.getNumber());
		int i =answers.find(uA);
		if(i<0)
			return null;
		uA = (UserAnswer) answers.elementAt(i);
		return uA.getAnswer();
	}
	
	public void setUserAnswer(BonusQuestion b,String answer){
		checkAnswer(b,answer);
		UserAnswer uA = new UserAnswer(b.getWeek(),b.getNumber(),answer);
		int i = answers.find(uA);
		if(i>=0)
			answers.setElementAt(uA, i);
		else
			answers.addElement(uA);
	}
	// ----------------- JSON ----------------- //

	/**
	 * Turns a User into a JSON object.
	 * 
	 * @return obj   JSON object
	 */
	public JSONObject toJSONObject() throws JSONException {
		JSONObject obj = new JSONObject();
		
		obj.put(KEY_FIRST_NAME, getFirstName());
		obj.put(KEY_LAST_NAME, getLastName());
		obj.put(KEY_ID, getID());
		obj.put(KEY_POINTS, getPoints());
		
		Contestant c = getWeeklyPick();
		if (c != null)
			obj.put(KEY_WEEKLY_PICK_ID, c.getID());
		else 
			obj.put(KEY_WEEKLY_PICK_ID, "??");
		
		c = getUltimatePick();
		if (c != null) 
			obj.put(KEY_ULT_PICK_ID, c.getID());
		else
			obj.put(KEY_ULT_PICK_ID, "??");
		
		obj.put(KEY_WIN_PICK_POINTS, new Integer(getUltimatePoints()));
		obj.put(KEY_NUM_BONUS_ANSWER,getNumBonusAnswer());
		return obj;
	}
	

	/**
	 * Turns a JSON object into a User.
	 * 
	 * @param o      JSON object
	 */
	public void fromJSONObject(JSONObject o) {
		answers.setSort(true);
		answers.setSortComparator(UserAnswer.comp);
		try {
			setFirstName((String)o.remove(KEY_FIRST_NAME));
			setLastName((String)o.remove(KEY_LAST_NAME));
			setID((String)o.remove(KEY_ID));
			setPoints(((Integer)o.remove(KEY_POINTS)).intValue());
			
			String id = (String)o.remove(KEY_WEEKLY_PICK_ID);
			Contestant c = null;
			if(!id.equals("??"))
				c = GameData.getCurrentGame().getContestant(id);
			setWeeklyPick(c);
			
			id = (String)o.remove(KEY_ULT_PICK_ID);
			c=null;
			if(!id.equals("??"))
				c = GameData.getCurrentGame().getContestant(id);
			setUltimatePick(c);
			
			setUltimatePoints(((Integer)o.remove(KEY_WIN_PICK_POINTS)).intValue());
			setNumBonusAnswer(((Integer)o.remove(KEY_NUM_BONUS_ANSWER)).intValue());
		} catch (InvalidFieldException e) {
			e.printStackTrace();
		}

	}
	

	/**
	 * Creates a json object from the user's answer
	 * @return JSONObject json object with the answer
	 */
	public JSONObject answerToJSONObject() throws JSONException{
		if(answers.size()==0)
			return null;
		JSONObject o = new JSONObject();
		JSONArray a = new JSONArray();
		for(int i =0;i<answers.size();i++){
			UserAnswer uA = (UserAnswer) answers.elementAt(i);
			a.put(uA.toJSONObject());
		}
		o.put(KEY_ANSWERS, a);
		return o;
	}
	
	/**
	 * Creates the user's answer from json object
	 * @param o json object
	 * @throws JSONException
	 */
	public void answersFromJSONObject(JSONObject o) throws JSONException{
		if(o==null)
			return;
		JSONArray a = (JSONArray) o.remove(KEY_ANSWERS);
		
		for(int i =0;i<a.length();i++){
			JSONObject ansJson = a.getJSONObject(i);
			UserAnswer uA = new UserAnswer();
			uA.fromJSONObject(ansJson);
			answers.addElement(uA);
		}
	}

	/**
	 * Get the path of the user's answer
	 * @param o      JSON object
	 */
	public String getAnswerPath(){
		return answerPath+this.getID()+".dat";
	}
	/**
	 * Updates the stored user with any not null information in the passed user
	 * @param u The user to update from.
	 * @throws InvalidFieldException Thrown if anything is of the wrong format.
	 * 
	 */
	public void update(User u) throws InvalidFieldException {
		if (u.getFirstName() != null) {
			setFirstName(u.getFirstName());
		}
		
		if (u.getLastName() != null) {
			setLastName(u.getLastName());
		}
		
		if (u.getID() != null) {
			setID(u.getID());
		}
		
		if (u.getPoints() != getPoints()) {
			setPoints(u.getPoints());
		}
		
		if (u.getWeeklyPick() != null) {
			setWeeklyPick(u.getWeeklyPick());
		}
		
		if (u.getUltimatePick() != null) {
			setUltimatePickNoSetPts(u.getUltimatePick());
		}
		
		if (u.getUltimatePoints() != getUltimatePoints()) {
			setUltimatePoints(u.getUltimatePoints());
		}
	}
}
