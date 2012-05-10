package couk.Adamki11s.AutoUpdater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.wolvencraft.MineReset.util.Message;

public class FetchSource
{
	
	protected static String fetchSource()
	{
		URL url = null;
		try
		{
			url = new URL("http://wolvencraft.com/plugins/MineReset/index.html");
		}
		catch(MalformedURLException ex)
		{
			ex.printStackTrace();
		}	
		
		InputStream is = null;
	    String s, source = "";

		try
		{
			is = url.openStream();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			Message.log("Error opening URL input stream!");
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new BufferedInputStream(is))));
		
		try
		{
			while ((s = reader.readLine()) != null)
			{
				source += s;
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			Message.log("Error reading input stream!");
		}
		
		try
		{
			is.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			Message.log("Error closing URL input stream!");
		}
         
		return source;
	}
	
	
}
