package client;

/* Author: CS2212 Group 2
 * File Name: StandingsScreen.java
 * Date: 25/01/2012
 * Project: UWOSurvivorPool
 * Course: CS2212b
 * Description:
 * */

import java.util.Vector;

import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.table.RichList;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.toolbar.ToolbarButtonField;
import net.rim.device.api.ui.toolbar.ToolbarManager;
import net.rim.device.api.util.StringProvider;
import client.data.GameData;
import data.Contestant;
import data.User;

public class PickScreen extends MainScreen implements FieldChangeListener {
	/* Variables */
	private LabelField labelTempName, labelTempTribe, labelTempStatus; // various
																		// labels.
	private String name, voteType;
	private FontFamily ff1; // fonts.
	private Font font2; // fonts.
	private ButtonField btnVoted;
	private Contestant tempCont;
	private ObjectChoiceField tempField;
	private String[] possibleChoices;
	
	public PickScreen(String voteType) {
		super();
		System.out.println("PickScreen constructor");
		this.voteType = voteType;

		VerticalFieldManager vertFieldManager = new VerticalFieldManager(
				VerticalFieldManager.USE_ALL_WIDTH
						| VerticalFieldManager.VERTICAL_SCROLLBAR) {
			// Override the paint method to draw the background image.
			public void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
				super.paint(graphics);
			}
		};
		;

		/* build the tool bar */
		ToolbarManager manager = new ToolbarManager();
		setToolbar(manager);
		try {
			/* Logout button */
			ToolbarButtonField toolbutton1 = new ToolbarButtonField(null,
					new StringProvider("Log Out"));
			toolbutton1.setCommandContext(new Object() {
				public String toString() {
					return "toolbutton1";
				}
			});
			/* if pressed, go back to Splash */
			toolbutton1.setCommand(new Command(new CommandHandler() {
				public void execute(ReadOnlyCommandMetadata metadata,
						Object context) {
					UiApplication.getUiApplication().pushScreen(
							new SplashScreen());
				}
			}));
			/* Exit button */
			ToolbarButtonField toolbutton2 = new ToolbarButtonField(null,
					new StringProvider("Exit"));
			toolbutton2.setCommandContext(new Object() {
				public String toString() {
					return "toolbutton2";
				}
			});
			/* if pressed, exit the system */
			toolbutton2.setCommand(new Command(new CommandHandler() {
				public void execute(ReadOnlyCommandMetadata metadata,
						Object context) {
					System.exit(0);
				}
			}));

			/* add buttons to the tool bar */
			manager.add(toolbutton1);
			manager.add(toolbutton2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		/* build contestant list */
		RichList list = new RichList(vertFieldManager, true, 3, 0);

		try { // set up the smaller list font
			ff1 = FontFamily.forName("Verdana");
			font2 = ff1.getFont(Font.BOLD, 20);
		} catch (final ClassNotFoundException cnfe) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.alert("FontFamily.forName() threw "
							+ cnfe.toString());
				}
			});
		}

		/* build the list */

		/*
		 * FILL THE LIST WITH ALL THE PLAYERS --------------------------
		 * 
		 * SHOW NAME, TRIBE, GAME STATUS AND PICTURE
		 * 
		 * -----------------------------------------------------------
		 */

		System.out.println("PickScreen: Getting all contestants from GD");
		Vector contList = GameData.getCurrentGame().getAllContestants();
		System.out.println("PickScreen: conlist size"+contList.size());
		
		/* build choices drop down*/
		String[] choices = new String[contList.size()];
		int iSetTo = 0;
		
		System.out.println("PickScreen: adding contestants to table");
		
		for (int i = 0; i < contList.size(); i++) {
			tempCont = (Contestant) contList.elementAt(i);
			System.out.println("PickScreen: "+tempCont.getFirstName());
			if(!tempCont.isCastOff()){
				choices[iSetTo]= tempCont.getFirstName() + " " + tempCont.getLastName()+" "+tempCont.getID();
				iSetTo++;
			}
			/* list contains labels so that the text colour can change */
			labelTempName = new LabelField(tempCont.getFirstName() + " " + tempCont.getLastName(), LabelField.ELLIPSIS) {
				public void paint(Graphics g) {
					g.setColor(Color.WHITE);
					super.paint(g);
				}
			};
			labelTempName.setFont(font2);

			labelTempTribe = new LabelField(tempCont.getTribe(), LabelField.ELLIPSIS) {
				public void paint(Graphics g) {
					g.setColor(Color.WHITE);
					super.paint(g);
				}
			};
			labelTempName.setFont(font2);

			String tempString = "";
			
			if (tempCont.isCastOff())
				tempString = "Castoff";
			else
				tempString = "Active";
			
			labelTempStatus = new LabelField(tempString, LabelField.ELLIPSIS) {
				public void paint(Graphics g) {
					g.setColor(Color.WHITE);
					super.paint(g);
				}
			};
			labelTempName.setFont(font2);

		//	list.add(new Object[] { Bitmap.getBitmapResource(tempCont.getPicture()), labelTempName, labelTempTribe,
			//		labelTempStatus });
		}
		System.out.println("PickScreen: contestants added to list");
		
		HorizontalFieldManager horFieldManager = new HorizontalFieldManager(
				HorizontalFieldManager.USE_ALL_WIDTH
						| HorizontalFieldManager.FIELD_HCENTER) {
			// Override the paint method to draw the background image.
			public void paint(Graphics graphics) {
				// graphics.setColor(Color.GREEN);
				// graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
				super.paint(graphics);
			}
		};
		;

		/* Build the banner */



		btnVoted = new ButtonField("Okay");
		btnVoted.setChangeListener(this);

		/*
		 * ADD AFTER PERSISTANCE ------------------------------
		 * 
		 * CHECK IF THE PERSON HAS ALREADY VOTED IF THEY HAVE, TURN OFF BUTTON
		 * ACCESS
		 * 
		 * ---------------------------------------------------
		 */
		possibleChoices = new String[iSetTo];
		for(int i =0;i<iSetTo;i++){
			possibleChoices[i]=choices[i];
		}
		
		tempField = new ObjectChoiceField(" Cast your "
				+ voteType + " vote: ", possibleChoices, 0,
				ObjectChoiceField.FORCE_SINGLE_LINE
						| ObjectChoiceField.FIELD_HCENTER);
		horFieldManager.add(btnVoted);
		horFieldManager.add(tempField);
		horFieldManager.setFont(font2);
		
		/* Build the components to MainScreen */
		this.setTitle(horFieldManager);
		this.add(vertFieldManager);
		this.setStatus(manager);
		System.out.println("PickScreen: Constructor end");
	}

	public void fieldChanged(Field arg0, int arg1) {
		if (arg0 == btnVoted) { // if the okay button is clicked
			if (voteType.equals("weekly")) {
				
				//TODO: move this to a helper function
				int i =tempField.getSelectedIndex();
				if(i==-1)
					return;
				
				String conName = possibleChoices[i];
				
				int idLoc = conName.lastIndexOf((int)' ');
				String conId = conName.substring(idLoc+1);
				Contestant c = GameData.getCurrentGame().getContestant(conId);
				System.out.println("****You chose "+c.getFirstName());
				GameData.getCurrentGame().getCurrentUser().setWeeklyPick(c);
			} else if (voteType.equals("ultimate")) { // ultimate
				// output
			} else { // final
						// output
			}
		}
	}

}