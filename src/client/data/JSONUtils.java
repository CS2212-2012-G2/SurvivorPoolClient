package client.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

import net.rim.device.api.io.FileNotFoundException;
import net.rim.device.api.io.IOUtilities;
import client.Common;
import data.User;
import data.bonus.Bonus;
import data.me.json.JSONException;
import data.me.json.JSONObject;

/**
 * @author Ramesh Raj
 * Use this class to get the keys, to write to a file, and getting values. This
 * differs from justing using the JSON classes directly as we keep references to
 * the object centralized. We can take care of any additional code that is
 * needed to read/write to files rather.
 */
public class JSONUtils {

	/**
	 * Get a json object from file path
	 * 
	 * @param path
	 *            The file path where the json string is stored
	 * @param displayError
	 *            true if you want to display an error on file not found.
	 * Source: {@link http://supportforums.blackberry.com/t5/Java-Development/Unable-to-read-SDCard-data/td-p/492822}
	 */
	public static JSONObject readFile(String path, boolean displayError)
			throws FileNotFoundException {
		String s = readTextFile(path,displayError);
		if(s==null)
			return null;
		
		System.out.println("    "+s);
		try {
			return new JSONObject(s);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Write all the necessary files needed for bb to be operational
	 * source {@link http://stackoverflow.com/questions/1519328/j2me-blackberry-how-to-read-write-text-file}
	 * @throws JSONException
	 */
	public static void writeData() throws JSONException {
		// 

		if (!isSDCardInserted()) {
			Common.displayErrorMsg("SD Card not inserted.");
			return;
		}

		try {
			JSONObject s = GameData.getCurrentGame().toJSONObject();
			writeData(s, GameData.filePath);
			System.out.println("\n\n\nWhat we got:" + s);
			if (Bonus.questionsExist()) {
				User u = GameData.getCurrentGame().getCurrentUser();
				writeData(u.answerToJSONObject(), u.getAnswerPath());
			}
		} catch (IOException e) {
			Common.displayErrorMsg("Error writing file");
			e.printStackTrace();
			return;

		}
	}

	/**
	 * Write json object to a specified object
	 * 
	 * @param json the json object
	 * @param filePath the file path
	 * @throws IOException
	 */
	private static void writeData(JSONObject json, String filePath)
			throws IOException {
		if (json == null)
			return;
		DataOutputStream os = null;
		FileConnection fconn = (FileConnection) Connector.open(filePath,
				Connector.READ_WRITE);
		if(fconn.exists())
			fconn.delete();
		fconn.create();

		os = fconn.openDataOutputStream();
		String jsonString = json.toString();
		os.write(jsonString.getBytes());
		System.out.println("\n\n\n\n actually writing" + jsonString.getBytes());
		
		os.close();
		fconn.close();
	}

	/**
	 * Return string of file contents
	 * @param path the path of the file
	 * @param displayError to display error for any errors
	 * @return string of file contents
	 * Source: {@link http://stackoverflow.com/questions/1519328/j2me-blackberry-how-to-read-write-text-file}
	 */
	private static String readTextFile(String path,boolean displayError) {
		if (!isSDCardInserted()) {
			Common.displayErrorMsg("SD Card not inserted.");
			return null;
		}
		
		String result = null;
		FileConnection fconn = null;
		DataInputStream is = null;
		try {
			fconn = (FileConnection)Connector.open(path,Connector.READ_WRITE);
			if (!fconn.exists()) {// file doesn't exist
				if (displayError)
					Common.displayErrorMsg(path + " not found.");
				return null;
			}
			fconn = (FileConnection) Connector.open(path, Connector.READ_WRITE);
			is = fconn.openDataInputStream();
			byte[] data = IOUtilities.streamToBytes(is);
			result = new String(data);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (null != is)

					is.close();
				if (null != fconn)
					fconn.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * Quick check to see if sd card has been inserter
	 * @return true if sd card found
	 */
	private static boolean isSDCardInserted() {
		Enumeration r = FileSystemRegistry.listRoots();

		// makes sure the sd card is inserted
		while (r.hasMoreElements()) {
			String root = (String) r.nextElement();
			if (root.equalsIgnoreCase("sdcard/")) {
				return true;
			}
		}

		return false;
	}
}
