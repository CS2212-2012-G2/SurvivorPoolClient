package client;

import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.toolbar.ToolbarButtonField;
import net.rim.device.api.ui.toolbar.ToolbarManager;
import net.rim.device.api.util.StringProvider;

public class Common {

	private static ToolbarManager toolbar;
	
	/**
	 * creates the toolbar for common actions
	 */
	private static void createToolbar(){
		toolbar = new ToolbarManager();
		try {
			// refresh button
			ToolbarButtonField btnRefresh = new ToolbarButtonField(null,
					new StringProvider("Refresh"));
			
			btnRefresh.setCommandContext(new Object() {
				public String toString() {
					return "toolbutton1";
				}
			});
			
			// if pressed, go back to the splash screen
			btnRefresh.setCommand(new Command(new CommandHandler() {
				public void execute(ReadOnlyCommandMetadata metadata,
						Object context) {
					UiApplication.getUiApplication().pushScreen(
							new SplashScreen());
				}
			}));
			
			
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
					System.exit(0);
				}
			}));

			/* add buttons to the tool bar */
			toolbar.add(btnRefresh);
			toolbar.add(btnExit);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * The toolbar with refresh and exit
	 * @return
	 */
	public static ToolbarManager getToolbar(){
		createToolbar();
		return toolbar;
	}
}
