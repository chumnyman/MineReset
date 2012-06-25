package com.wolvencraft.AutoUpdater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.wolvencraft.MineReset.util.ChatUtil;

public class FetchSource
{
	
	protected static Map<String, String> fetchSource()
	{
		URL url = null;
		try {
			url = new URL("http://update.wolvencraft.com/MineReset/");
		}
		catch(MalformedURLException ex) {
			ex.printStackTrace();
			return null;
		}	
		
		InputStream is = null;
	    String source;

		try {
			is = url.openStream();
		}
		catch (IOException ex) {
			ChatUtil.log("Unable to connect to the update server!");
			return null;
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new BufferedInputStream(is))));
		Map<String, String> data = new HashMap<String, String>();
		
		try {
			while ((source = reader.readLine()) != null) {
				if(source.indexOf("@") == 0) {
					String[] parts = source.substring(1).split("=");
					parts[1] = parts[1].substring(0, (parts[1].length() - 6));
				    data.put(parts[0], parts[1]);
				}
			 }
		}
		catch (IOException ex) {
			ChatUtil.log("Error reading input stream!");
			return null;
		}
		
		try {
			is.close();
		}
		catch (IOException ioe) {
			ChatUtil.log("Error closing URL input stream!");
			return null;
		}
         
		return data;
	}
	
	
}
