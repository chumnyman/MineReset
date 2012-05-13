/**
 * LICENSING
 * 
 * This software is copyright by Adamki11s <adam@adamki11s.co.uk> and is
 * distributed under a dual license:
 * 
 * Non-Commercial Use:
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Commercial Use:
 *    Please contact adam@adamki11s.co.uk
 */

package couk.Adamki11s.AutoUpdater;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.cmd.Config;
import com.wolvencraft.MineReset.config.Configuration;


public class AUCore
{

	private double version;
	private int subVersion;
	private String reason, source, urgency;
	private Logger log;
	private URL url;
	
	public AUCore(String address, Logger log)
	{
		this.log = log;
		
		try
		{
			url = new URL(address);
		}
		catch(MalformedURLException ex)
		{
			ex.printStackTrace();
		}	
		
	}
	
	
	/**
	 * Check the current version against the latest one.
	 * @param currentVersion - Double
	 * @param currentSubVersion - Double
	 * @return
	 */
	public boolean checkVersion()
	{
		try
		{
		if(!Configuration.getBoolean("versions.check-for-new-versions"))
			return true;
		}
		catch(NullPointerException npe)
		{
			String[] args = {"config", "generate"};
			Config.run(args);
		}
		
		source = FetchSource.fetchSource();
		formatSource(source);
		
		String subVers = Integer.toString(MineReset.curSubVer);
		
		boolean verUpToDate;
		
		if(Configuration.getBoolean("versions.check-for-development-builds"))
		{
			if(version > MineReset.curVer)
				verUpToDate = false;
			else if(version == MineReset.curVer && subVersion > MineReset.curSubVer)
				verUpToDate = false;
			else
				verUpToDate = true;
		}
		else if(Configuration.getBoolean("versions.check-for-recommended-builds"))
		{
			if(version > MineReset.curVer)
				verUpToDate = false;
			else verUpToDate = true;
		}
		else
		{
			verUpToDate = true;
		}
		
		
		if(!verUpToDate)
		{
			String extraOne = "";
			String extraTwo = "";
			String extraAll = "";
			String extraDash = "";
			if(urgency.equalsIgnoreCase("LOW"))
				extraOne = "   ";
			else if(urgency.equalsIgnoreCase("MEDIUM"))
				extraOne = "";
			else if(urgency.equalsIgnoreCase("HIGH"))
				extraOne = "  ";
			
			if(reason.length() < 9)
			{
				for(int i = reason.length(); i < 9; i++)
					extraTwo = extraTwo + " ";
			}
			else if(reason.length() > 9)
			{
				for(int i = 9; i < reason.length(); i++)
				{
					extraAll = extraAll + " ";
					extraDash = extraDash + "-";
				}
			}
			log.info("+------------------------------" + extraDash + "+");
			log.info("| MineReset is not up to date! " + extraAll + "|");
			log.info("| Running version : " + MineReset.curVer + "." + subVers + "      " + extraAll + "|");
			log.info("| Latest version  : " + version + "." + subVersion + "      " + extraAll + "|");
			log.info("| Urgency         : " + urgency + extraOne + "     " + extraAll + "|");
			log.info("| Description     : " + reason + "  " + extraTwo + "|");
			log.info("+------------------------------" + extraDash + "+");
			return false;
		}
		else return true;
		
	}
	
	private void formatSource(String source)
	{
		String str[] = source.split("\\@");
		
		try
		{
			version = Double.parseDouble(str[1]);
			subVersion = Integer.parseInt(str[2]);
		}
		catch (NumberFormatException ex)
		{
			ex.printStackTrace();
			log.info("Error while parsing version number!");
		}
		
		urgency = str[3];
			
		reason = str[4];
	}
	
	public String getUrgency()
	{
		return urgency;
	}
	
	public String getReason()
	{
		return reason;
	}
	
	public double getVersion()
	{
		return version;
	}
	
	public int getSubVersion()
	{
		return subVersion;
	}
}
