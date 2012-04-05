package client;

/**@author Jonathan Demelo, Ramesh Raj
 * File Name: StandingsScreen.java
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
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.table.RichList;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.util.Comparator;
import net.rim.device.api.util.SimpleSortingVector;
import client.data.GameData;
import data.User;

public class StandingScreen extends MainScreen implements FieldChangeListener {
	/* Variables */
	private LabelField labelTemp; // various labels.
	private String temp;
	private FontFamily ff1; // fonts.
	private Font font1, font2; // fonts.
	private User tempUser;
	
	public StandingScreen() {
		super();

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
		
		RichList list = new RichList(vertFieldManager, false, 1, 0);
		try { // set up the header list font
			ff1 = FontFamily.forName("Verdana");
			font1 = ff1.getFont(Font.BOLD, 20);
		} catch (final ClassNotFoundException cnfe) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.alert("FontFamily.forName() threw "
							+ cnfe.toString());
				}
			});
		}

		try { // set up the smaller list font
			ff1 = FontFamily.forName("Verdana");
			font2 = ff1.getFont(Font.BOLD, 20);
		} catch (final ClassNotFoundException cnfe) {}

		/* build the list */
		temp = "Rank Score Name"; // headers
		labelTemp = new LabelField(temp, LabelField.ELLIPSIS) {
			public void paint(Graphics g) {
				g.setColor(Color.WHITE); // white text
				super.paint(g);
			}
		};
		labelTemp.setFont(font1);

		
		Vector userL = GameData.getCurrentGame().getAllUsers();
		//to display from most points to least point
		SimpleSortingVector userList = new SimpleSortingVector();
		for(int i =0;i<userL.size();i++)
			userList.addElement(userL.elementAt(i));
		userList.setSortComparator(new Comparator(){//sorts highest to least points
			public int compare(Object o1, Object o2) {
				User u1 = (User)o1;
				User u2 = (User)o2;
				return u2.getPoints()-u1.getPoints();
			}
			
		});
		userList.reSort();//sort the array
		
		list.add(new Object[] { labelTemp }); // add headers

		/* fill list with players */
		for (int i = 0; i < userList.size(); i++) {
			tempUser = (User) userList.elementAt(i);
			int tempPlacement = i + 1;
			
			// Formatting decisions
			if (tempPlacement > 99)
				temp = " " + tempPlacement + "\t   ";
			else if (tempPlacement > 9)
				temp = "   " + tempPlacement + "\t   ";
			else
				temp = "\t" + tempPlacement + "\t   ";

			if (tempUser.getPoints() > 99)
				temp = temp + tempUser.getPoints() + "   " + tempUser.getFirstName() + " " + tempUser.getLastName();
			else if (tempUser.getPoints() > 9)
				temp = temp + " " + tempUser.getPoints() + "    " + tempUser.getFirstName() + " " + tempUser.getLastName();
			else
				temp = temp + " " + tempUser.getPoints() + "\t\t" + tempUser.getFirstName() + " " + tempUser.getLastName();
			/* list contains labels so that the text colour can change */
			labelTemp = new LabelField(temp, LabelField.ELLIPSIS) {
				public void paint(Graphics g) {
					g.setColor(Color.WHITE);
					super.paint(g);
				}
			};
			labelTemp.setFont(font2);
			list.add(new Object[] { labelTemp });

		}

		/* Build the components to MainScreen */
		this.setStatus(Common.getToolbar("Log Out"));
		this.add(vertFieldManager);

	}

	public void fieldChanged(Field arg0, int arg1) {
		// merely required, but not used.
	}

}