package client;

/* Author: CS2212 Group 2
 * File Name: BonusScreen.java
 * Date: 25/01/2012
 * Project: UWOSurvivorPool
 * Course: CS2212b
 * Description:
 * */

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import data.GameData;
import data.User;
import data.bonus.Bonus;
import data.bonus.BonusQuestion;

public class BonusScreen extends MainScreen implements FieldChangeListener {
	/* Variables */
	private LabelField labelTitle; // various labels.
	private FontFamily ff1; // fonts.
	private Font font1, font2; // fonts.
	private ButtonField buttonSend, buttonPrevious, buttonNext;
	private EditField answerField, questionField; // editable text field
	private ObjectChoiceField multiChoiceField;
	
	private VerticalFieldManager vertFieldManager;
	
	private Vector questions = Bonus.getAllQuestions();
	//int questionNum = questions.size() - 1;
	BonusQuestion currentQuestion;
	
	public BonusScreen() {
		super(NO_VERTICAL_SCROLL);
		currentQuestion = (BonusQuestion) questions.lastElement();
		drawQuestionScreen();
	}

	private void drawQuestionScreen() {

		vertFieldManager = new VerticalFieldManager(
				VerticalFieldManager.USE_ALL_WIDTH
						| VerticalFieldManager.USE_ALL_HEIGHT) {
			// Override the paint method to draw the background image.
			public void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, 640, 430);
				super.paint(graphics);
			}
		};

		setToolbar(Common.getToolbar());

		/* Font setup */
		try { // set up the smaller list font
			ff1 = FontFamily.forName("Verdana");
			font2 = ff1.getFont(Font.BOLD, 20);
		} catch (final ClassNotFoundException cnfe) {
			System.out.println("font family failed");
		}

		/* Add the send button */
		buttonSend = new ButtonField("Send", ButtonField.FIELD_HCENTER|ButtonField.FIELD_BOTTOM);
		buttonSend.setChangeListener(this);

		buttonSend.setMargin(0, 20, 30, 20);
		vertFieldManager.add(buttonSend);

		HorizontalFieldManager horFieldManager = new HorizontalFieldManager(
				HorizontalFieldManager.USE_ALL_WIDTH
						| HorizontalFieldManager.FIELD_HCENTER);

		/* Top manager bar setup */
		buttonPrevious = new ButtonField("Prev"); // previous button
		buttonPrevious.setChangeListener(this);
		
		horFieldManager.add(buttonPrevious);

		labelTitle = new LabelField("  Bonus Questions ");
		labelTitle.setFont(font1); // centre label

		horFieldManager.add(labelTitle);

		buttonNext = new ButtonField("Next"); // next button
		buttonNext.setChangeListener(this);
		horFieldManager.add(buttonNext);
		
		updateQuestionScreen();
		this.setTitle(horFieldManager);
		this.add(vertFieldManager);
		this.setStatus(Common.getToolbar());
	}
	
	private void updateQuestionScreen(){
		//TODO: a better way to update screen than this.
		vertFieldManager.deleteAll();
		questionField = new EditField(currentQuestion.getPrompt(), "", 1,
				EditField.NO_NEWLINE) {
			public void paint(Graphics graphics) { // keep on same line
				graphics.setColor(Color.WHITE); // white text
				super.paint(graphics);
			}
		};

		questionField.setEditable(false);
		questionField.setFont(font2);
		questionField.setMargin(30, 20, 30, 20);
		vertFieldManager.add(questionField);

		multiChoiceField = new ObjectChoiceField("", currentQuestion.getChoices(), 0,
				ObjectChoiceField.FORCE_SINGLE_LINE
						| ObjectChoiceField.FIELD_HCENTER);
		
		answerField = new EditField("Answer:  ", "", 200, EditField.NO_NEWLINE) {
			public void paint(Graphics graphics) { // keep on same line
				graphics.setColor(Color.WHITE); // white text
				super.paint(graphics);
			}
		};
		
		User curUser = GameData.getCurrentGame().getCurrentUser();
		String uAnswer = curUser.getUserAnswer(currentQuestion);
		
		if(currentQuestion.getBonusType()==0){//short answer
			vertFieldManager.add(answerField);
			answerField.setMargin(0, 20, 10, 20);
			String ans = "";
			if (showAnswer()){//has the week passed
				ans += "Correct Answer: "+currentQuestion.getAnswer()+".";
				if(uAnswer!=null)
					ans+="Your Answer: ";
			}
			
			if(uAnswer!=null) //has the user answered this question before
				ans+=uAnswer;
			answerField.setText(ans);
		}else{//MC question
			if (showAnswer()){
				String[] c = new String[2];
				c[0] = "Cor. Answer:"+currentQuestion.getAnswer();
				if(uAnswer!=null)
					c[1]= "Your answer: "+uAnswer;
				else
					c[1] = "Did not answer";
				multiChoiceField.setChoices(c);
			}else{
				String[] choices = currentQuestion.getChoices();
				multiChoiceField.setChoices(choices);
				if(uAnswer!=null){
					for(int i =0;i<choices.length;i++){
						if(uAnswer.equalsIgnoreCase(choices[i]))
							multiChoiceField.setSelectedIndex(i);
					}
				}
				
			}
			
			vertFieldManager.add(multiChoiceField);
		}
		
		buttonPrevious.setEnabled(true);
		buttonNext.setEnabled(true);
		
		if(currentQuestion.equals(questions.firstElement()))
			buttonPrevious.setEnabled(false);
		if(currentQuestion.equals(questions.lastElement()))
			buttonNext.setEnabled(false);
		

		/* Add the send button */
		if(!showAnswer()){
			buttonSend = new ButtonField("Send", ButtonField.FIELD_HCENTER|ButtonField.FIELD_BOTTOM);
			buttonSend.setChangeListener(this);
			buttonSend.setMargin(0, 20, 30, 20);
			vertFieldManager.add(buttonSend);
		}
	}

	private boolean showAnswer(){
		GameData g = GameData.getCurrentGame();
		if(currentQuestion.getWeek()<g.getCurrentWeek())
			return true;
		return false;
	}
	
	public boolean onSavePrompt() {
		return true;
	}

	public void fieldChanged(Field arg0, int arg1) {
		if (arg0 == buttonSend) { 
			User u = GameData.getCurrentGame().getCurrentUser();
			String answer="";
			if(currentQuestion.getBonusType()==0){
				answer = answerField.getText();
			}else{
				int i =multiChoiceField.getSelectedIndex();
				answer = currentQuestion.getChoices()[i];
			}
			u.setUserAnswer(currentQuestion, answer);
			Status.show("Submitted.");
		} else if (arg0 == buttonNext) {
			int loc = questions.indexOf(currentQuestion);
			currentQuestion = (BonusQuestion) questions.elementAt(loc+1);
			updateQuestionScreen();
		} else if (arg0 == buttonPrevious) {
			int loc = questions.indexOf(currentQuestion);
			currentQuestion = (BonusQuestion) questions.elementAt(loc-1);
			updateQuestionScreen();

		}
	}

}