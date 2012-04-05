package client;

/**@author Jonathan Demelo, Ramesh Raj
 */

import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.toolbar.ToolbarButtonField;
import net.rim.device.api.ui.toolbar.ToolbarManager;
import net.rim.device.api.util.StringProvider;
import data.GameData;
import data.User;

public class Common {

	private static ToolbarManager toolbar;
	
	/**
	 * creates the toolbar for common actions
	 */
	private static void createToolbar(String msg){
		toolbar = new ToolbarManager();
		try {
			// refresh button
			if(msg!=null){
				ToolbarButtonField btnLogout = new ToolbarButtonField(null,
						new StringProvider(msg));
			
				btnLogout.setCommandContext(new Object() {
					public String toString() {
						return "toolbutton1";
					}
				});
			
				// if pressed, go back to the splash screen
				btnLogout.setCommand(new Command(new CommandHandler() {
					public void execute(ReadOnlyCommandMetadata metadata,
							Object context) {
						System.out.println("go back to splash");
						if(logoff())
							UiApplication.getUiApplication().pushScreen(
									new SplashScreen());
					}
				}));
				toolbar.add(btnLogout);
			}
			// Exit button 
			ToolbarButtonField btnExit = new ToolbarButtonField(null,
					new StringProvider("Exit"));
			btnExit.setCommandContext(new Object() {
				public String toString() {
					return "toolbutton2";
				}
			});
			
			// if pressed, exit the system 
			btnExit.setCommand(new Command(new CommandHandler() {
				public void execute(ReadOnlyCommandMetadata metadata,
						Object context) {
					exitApp();
				}
			}));

			/* add buttons to the tool bar */
			
			toolbar.add(btnExit);
		} catch (Exception e) {}
	}
	
	/**
	 * The toolbar with refresh and exit
	 * @param msg Changes the first label
	 * @return
	 */
	public static ToolbarManager getToolbar(String msg){
		createToolbar(msg);
		return toolbar;
	}
	/**
	 * Save data to file with appropriate checks
	 * @return true if user saved or false if user hit cancel or could not save
	 */
	private static boolean save(){
		int response = Dialog.ask(Dialog.D_SAVE,"Do you want to save?");
		if(response == Dialog.SAVE){
			//TODO: is first week, week 0?
			if(GameData.getCurrentGame().getCurrentWeek()==1){
				User u = GameData.getCurrentGame().getCurrentUser();
				if(u.getUltimatePick()==null){
					displayErrorMsg("Cannot save unless you choose an ultimate pick.");
					UiApplication.getUiApplication().pushScreen(new PickScreen(PickScreen.T_ULTIMATE));
					return false;
				}
			}
			GameData.getCurrentGame().writeData();
			return true;
		}else if(response == Dialog.CANCEL)
			return false;
		return true;
	}
	
	public static boolean logoff(){
		boolean saveStatus= save();
		if(saveStatus)
			GameData.getCurrentGame().setCurrentUser(null);
		return saveStatus;
	}
	
	/**
	 * Close the app with the option to saveData
	 * @param saveData
	 */
	public static void exitApp(){
		if(GameData.getCurrentGame()==null||GameData.getCurrentGame().getCurrentUser()==null){
			System.exit(0);
			return;
		}
		if(save())
			System.exit(0);
	}

	/**
	 * Display error to user.
	 * @param msg
	 */
	public static void displayErrorMsg(String msg){
		Dialog.alert(msg);
	}
}
