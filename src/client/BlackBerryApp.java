package client;

/**@author Jonathan Demelo, Ramesh Raj
 * File Name: BlackBerryApp.java
 * Project: UWOSurvivorPool
 * Course: CS2212b
 * Description: The gateway class which create the first instance of the Blackberry application.
 * */

import net.rim.device.api.ui.UiApplication;

public class BlackBerryApp extends UiApplication {

	public BlackBerryApp () {
		try{
			pushScreen(new SplashScreen()); // loads first class screen
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println("CAUGHT IN BBAPP. ");
		}
	}

	public static void main(String[] args) {
		BlackBerryApp  BBApp = new BlackBerryApp (); // creates new instance
		BBApp.enterEventDispatcher(); // launches the event dispatcher
	}
}
