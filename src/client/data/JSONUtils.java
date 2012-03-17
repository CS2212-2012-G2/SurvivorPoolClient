package client.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

import net.rim.device.api.io.FileNotFoundException;
import net.rim.device.api.io.IOUtilities;
import data.me.json.JSONException;
import data.me.json.JSONObject;


/**
 * Use this class to get the keys, to write to a file, and getting values.
 * This differs from justing using the JSON classes directly as we keep
 * references to the object centralized. We can take care of any additional
 * code that is needed to read/write to files rather.
 */
public class JSONUtils{
	
	public static String seasonFile = "file:///SDCard/res/data/Settings.dat";
	
	//some code from http://supportforums.blackberry.com/t5/Java-Development/Unable-to-read-SDCard-data/td-p/492822
	public static JSONObject readFile(String path) throws FileNotFoundException{
		InputStream is = null;
		if(!isSDCardInserted()){
			ErrorText.displayErrorMsg("SD Card not inserted.");
			return null;
		}
		
		try {
			FileConnection fconn = (FileConnection)Connector.open(seasonFile,Connector.READ_WRITE);
			if (!fconn.exists()) {//file doesn't exist
				ErrorText.displayErrorMsg(seasonFile+" not found.");
				return null;
			}
			
			is = fconn.openInputStream();
			String str = new String(IOUtilities.streamToBytes(is), "UTF-8");
			JSONObject obj = new JSONObject(str);
			return obj; //return json obejct
			
		}catch (IOException e) {
			ErrorText.displayErrorMsg("Error reading "+seasonFile);
			e.printStackTrace();
			return null;
		
		}catch (JSONException e) {
			ErrorText.displayErrorMsg("File is malformed. Contact admin.");
			e.printStackTrace();
			return null;
		
		}finally{
			if(is != null){
				try { is.close(); } catch (IOException ignored) {}
			}
		}
	}
	
	
	/**
	 * Writes to file using a json object
	 * @param filePath The file path to write to
	 * @param json A json object that has the keys and teh values
	 */
	public static void writeJSON(JSONObject json){
		OutputStream os = null;
		if(!isSDCardInserted()){
			ErrorText.displayErrorMsg("SD Card not inserted.");
			return;
		}
		
		try {
			FileConnection fconn = (FileConnection)Connector.open(seasonFile+".as",Connector.READ_WRITE);
			if (!fconn.exists()) {//file doesn't exist
				fconn.create();
			}
			
			os = fconn.openOutputStream(0);
			String jsonString = json.toString();
			System.out.println(jsonString);
			os.write(jsonString.getBytes());
			
		}catch (IOException e) {
			ErrorText.displayErrorMsg("Error reading "+seasonFile);
			e.printStackTrace();
			return;
		
		}finally{
			if(os != null){
				try { os.close(); } catch (IOException ignored) {}
			}
		}
		
	}

	
	private static boolean isSDCardInserted(){
		Enumeration r = FileSystemRegistry.listRoots();
		
		//makes sure the sd card is inserted
		while (r.hasMoreElements()) {
			String root = (String) r.nextElement();
			if( root.equalsIgnoreCase("sdcard/") ) {
				return true;
			}
		}
		
		return false;
	}
}
