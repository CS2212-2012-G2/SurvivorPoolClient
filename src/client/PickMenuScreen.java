package client;

/* Author: CS2212 Group 2
 * File Name: PickMenuScreen.java
 * Date: 25/01/2012
 * Project: UWOSurvivorPool
 * Course: CS2212b
 * Description:
 * */

import data.GameData;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class PickMenuScreen extends MainScreen implements FieldChangeListener{
	/* Variables */
	private ButtonField btnWeekly, btnUltimate; 
	private Bitmap backgroundBitmap;
	
	
	public PickMenuScreen() {
		super(NO_VERTICAL_SCROLL);
		backgroundBitmap = Bitmap.getBitmapResource("MainMenu.png");
		VerticalFieldManager vertFieldManager = new VerticalFieldManager(
				VerticalFieldManager.USE_ALL_WIDTH
						| VerticalFieldManager.USE_ALL_HEIGHT) {
			// Override the paint method to draw the background image.
			public void paint(Graphics graphics) {
				graphics.drawBitmap(0, 0, 640, 430, backgroundBitmap, 0, 0);
				super.paint(graphics);
			}
		};
		
		/* Buttons */
		btnWeekly = new ButtonField("Vote For This Week", LabelField.FIELD_HCENTER); 
		btnWeekly.setChangeListener(this);
		btnWeekly.setMargin(230, 0, 0, 0); // formatting
		btnUltimate = new ButtonField("Vote For Ultimate", LabelField.FIELD_HCENTER); 
		btnUltimate.setChangeListener(this);

		if(GameData.getCurrentGame().weeksLeft()==1){
			btnWeekly.setLabel("Vote For Finals");
		}

		this.setStatus(Common.getToolbar("Log Out"));
		vertFieldManager.add(btnWeekly);
		vertFieldManager.add(btnUltimate);
		this.add(vertFieldManager);
	}
	
	public boolean onSavePrompt(){
		return true;
	}

	public void fieldChanged(Field arg0, int arg1) {
		if (arg0 == btnWeekly) { 
			UiApplication.getUiApplication().pushScreen(
					new PickScreen(PickScreen.T_WEEKLY));
		
		}else if (arg0 == btnUltimate){
			UiApplication.getUiApplication().pushScreen(
					new PickScreen(PickScreen.T_ULTIMATE));
		
		}

	}
}