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

package com.wolvencraft.AutoUpdater;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.cmd.ConfigurationCommand;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.util.Message;


public class Updater
{

	private static double version;
	private static int subVersion;
	private static String reason, source, urgency;
	
	/**
	 * Check the current version against the latest one.
	 * @param currentVersion - Double
	 * @param currentSubVersion - Double
	 * @return
	 */
	public static boolean checkVersion()
	{
		try
		{
			if(!Configuration.getBoolean("versions.check-for-new-versions"))
				return true;
		}
		catch(NullPointerException npe)
		{
			String[] args = {"config", "generate"};
			ConfigurationCommand.run(args);
		}
		
		source = FetchSource.fetchSource();
		if(source == null) return true;
		formatSource(source);
		
		String subVers = Integer.toString(MineReset.curSubVer);
		
		boolean verUpToDate;
		String channel = Configuration.getString("updater.channel");
		
		if(channel.equalsIgnoreCase("db")) {
			if(version > MineReset.curVer || (version == MineReset.curVer && subVersion > MineReset.curSubVer))
				verUpToDate = false;
			else
				verUpToDate = true;
		}
		else if(channel.equalsIgnoreCase("rb")) {
			if(version > MineReset.curVer)
				verUpToDate = false;
			else verUpToDate = true;
		}
		else
			verUpToDate = true;
		
		
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
			Message.log("+------------------------------" + extraDash + "+");
			Message.log("| MineReset is not up to date! " + extraAll + "|");
			Message.log("|    http://bit.ly/MineReset   " + extraAll + "|");
			Message.log("| Running version : " + MineReset.curVer + "." + subVers + "      " + extraAll + "|");
			Message.log("| Latest version  : " + version + "." + subVersion + "      " + extraAll + "|");
			Message.log("| Urgency         : " + urgency + extraOne + "     " + extraAll + "|");
			Message.log("| Description     : " + reason + "  " + extraTwo + "|");
			Message.log("+------------------------------" + extraDash + "+");
			return false;
		}
		else return true;
		
	}
	
	private static void formatSource(String source)
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
			Message.log("Error while parsing version number!");
		}
		
		urgency = str[3];
			
		reason = str[4];
	}
	
	public static String getUrgency()
	{
		return urgency;
	}
	
	public static String getReason()
	{
		return reason;
	}
	
	public static double getVersion()
	{
		return version;
	}
	
	public static int getSubVersion()
	{
		return subVersion;
	}
}
