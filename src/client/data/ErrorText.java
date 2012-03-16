package client.data;

import net.rim.device.api.ui.component.Dialog;

/**
 * Only reason this class exists at the moment is for future
 * features.
 * @author CS2212-G2
 *
 */
public class ErrorText {
	
	/**
	 * Display error to user.
	 * @param msg
	 */
	public static void displayErrorMsg(String msg){
		Dialog.alert(msg);
	}
	

}
