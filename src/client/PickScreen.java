package client;

/**@author Jonathan Demelo, Ramesh Raj
 * File Name: StandingsScreen.java
 * Date: 25/01/2012
 * Project: UWOSurvivorPool
 * Course: CS2212b
 * Description:
 * */

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.component.table.RichList;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import client.data.GameData;
import data.Contestant;
import data.User;

public class PickScreen extends MainScreen implements FieldChangeListener {
	/* Variables */
	private LabelField lblContName, labelContTribe, labelTempStatus; // various
																		// labels.
	private int voteType;
	private FontFamily ff1; // fonts.
	private Font font2; // fonts.
	private ButtonField btnVoted;
	private ObjectChoiceField ocfActiveContestant;
	private RichList list;

	private final static String DEFAULT_IMAGE = "res/img/defaultpic.png";
	
	public final static int T_WEEKLY= 0;
	public final static int T_ULTIMATE= 1;
	public PickScreen(int voteType) {
		super();
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

		try { // set up the smaller list font
			ff1 = FontFamily.forName("Verdana");
			font2 = ff1.getFont(Font.BOLD, 20);
		} catch (final ClassNotFoundException cnfe) {}
		
		Vector contList = GameData.getCurrentGame().getActiveContestants();
		
		Contestant[] contArray = new Contestant[contList.size()];
		contList.copyInto(contArray);
		ocfActiveContestant = new ObjectChoiceField(" Cast your "+ voteType + " vote: ",contArray);
		User user = GameData.getCurrentGame().getCurrentUser();
		if(voteType==T_WEEKLY&&user.getWeeklyPick()!=null){
			ocfActiveContestant.setSelectedIndex(user.getWeeklyPick());
		}else if(voteType==T_ULTIMATE&&user.getUltimatePick()!=null){
			ocfActiveContestant.setSelectedIndex(user.getUltimatePick());
		}
			
		list = new RichList(vertFieldManager, true, 3, 0);

		//get all contestants for list
		contList = GameData.getCurrentGame().getAllContestants();
		for (int i = 0; i < contList.size(); i++) {
			Contestant cont = (Contestant) contList.elementAt(i);
			/* list contains labels so that the text colour can change */
			lblContName = new LabelField(cont.getFirstName() + " " + cont.getLastName(), LabelField.ELLIPSIS) {
				public void paint(Graphics g) {
					g.setColor(Color.WHITE);
					super.paint(g);
				}
			};
			lblContName.setFont(font2);

			labelContTribe = new LabelField(cont.getTribe(), LabelField.ELLIPSIS) {
				public void paint(Graphics g) {
					g.setColor(Color.WHITE);
					super.paint(g);
				}
			};
			lblContName.setFont(font2);

			String tempString = "";
			
			if (cont.isCastOff())
				tempString = "Castoff";
			else
				tempString = "Active";
			
			labelTempStatus = new LabelField(tempString, LabelField.ELLIPSIS) {
				public void paint(Graphics g) {
					g.setColor(Color.WHITE);
					super.paint(g);
				}
			};
			lblContName.setFont(font2);
			Bitmap imgContestant = getImage(cont.getPicture());
			list.add(new Object[] {imgContestant,lblContName, labelContTribe,labelTempStatus });
			
		}
			
		HorizontalFieldManager horFieldManager = new HorizontalFieldManager(
				HorizontalFieldManager.USE_ALL_WIDTH
						| HorizontalFieldManager.FIELD_HCENTER) {
			// Override the paint method to draw the background image.
			public void paint(Graphics graphics) {
				graphics.setColor(Color.GREEN);
				graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
				super.paint(graphics);
			}
		};
		;

		String voted= "Vote";
		user = GameData.getCurrentGame().getCurrentUser();
		System.out.println(user.getWeeklyPick()+" "+this.voteType);
		if((this.voteType==T_WEEKLY&&user.getWeeklyPick()!=null)||(this.voteType==T_ULTIMATE&&user.getUltimatePick()!=null))
			voted = "Revote";
		btnVoted = new ButtonField(voted);
		btnVoted.setChangeListener(this);

		horFieldManager.add(btnVoted);
		horFieldManager.add(ocfActiveContestant);
		horFieldManager.setFont(font2);
		
		
		this.setTitle(horFieldManager);
		this.add(vertFieldManager);
		this.setStatus(Common.getToolbar("Log Out"));
		vertFieldManager.setFocus(); //THIS NEEDS TO BE HERE. APP CRASHES WITHOUT IT
	}

	public boolean onSavePrompt() {
		return true;
	}

	public void fieldChanged(Field arg0, int arg1) {
		if (arg0 == btnVoted) { // if the okay button is clicked
			Contestant c = getChosenContestant();
			if (voteType==T_WEEKLY) {
				GameData.getCurrentGame().getCurrentUser()
						.setWeeklyPick(c);
			} else if (voteType==T_ULTIMATE) {
				GameData.getCurrentGame().getCurrentUser()
						.setUltimatePick(c);
			}
			Status.show("You chose: "+c.getFirstName()+" "+c.getLastName());
			UiApplication.getUiApplication().popScreen(this);
		}
	}

	/**
	 * Gets the contestant from the drop down box or null if none chosen
	 * @return the contestant object
	 */
	private Contestant getChosenContestant() {
		int i = ocfActiveContestant.getSelectedIndex();
		if (i == -1)
			return null;

		Contestant contestant = (Contestant) ocfActiveContestant.getChoice(i);
		return contestant;
	}

	/**
	 * Gets the image given a path
	 * Source: {@link http://supportforums.blackberry.com/t5/Java-Development/How-to-read-display-image-from-SD-card-on-Device/m-p/621671#M129277}
	 */
	public Bitmap getImage(String path) {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("file:///SDCard/").append(path);
		String filename = sb.toString();
		sb = null;
		Bitmap image = null;

		FileConnection fc = null;
		InputStream input = null;
		try {
			fc = (FileConnection) Connector
					.open(filename, Connector.READ_WRITE);
			if (fc.exists()) {
				input = fc.openInputStream();
				byte[] data = new byte[(int) fc.fileSize()];
				input.read(data);
				image = Bitmap.createBitmapFromPNG(data, 0, data.length);
			}
		} catch (Exception e) {
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
			if (fc != null) {
				try {
					fc.close();
				} catch (Exception e) {
				}
			}
		}

		if (image == null) {
			return getImage(DEFAULT_IMAGE);//TODO: possible recursive loop
		}
		return image;
	}

}