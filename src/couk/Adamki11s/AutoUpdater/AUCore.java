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


public class AUCore{

	private double versionNO;
	private int subVersionNO;
	private String reason, source, urgency;
	private Logger log;
	
	private URL uri;
	
	/**
	 * Constructor for variables needed for the AutoUpdaterCore.
	 * @param website
	 * @param l
	 * @param plugin
	 * @param serv
	 */
	public AUCore(String website, Logger l){
		
		log = l;
		
		try{
			uri = new URL(website);
		} catch(MalformedURLException ex){
			ex.printStackTrace();
			log.info("Malformed URL Exception. Make sure the URL is in the form 'http://www.website.domain'");
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
		source = FetchSource.fetchSource(uri, log);
		formatSource(source);
		
		String subVers;
		if(MineReset.curSubVer == 0)
		{
			subVers = "";
		}
		else
		{
			subVers = Integer.toString(MineReset.curSubVer);
		}
		
		if(versionNO != MineReset.curVer || subVersionNO != MineReset.curSubVer)
		{
			log.info("-------------------------------");
			log.info("| MineReset is not up to date!");
			log.info("| Running version : " + MineReset.curVer + "." + subVers + "");
			log.info("| Latest version  : " + versionNO + "." + subVersionNO + "");
			log.info("| Urgency         : " + urgency);
			log.info("| Reason          : " + reason);
			log.info("-------------------------------");
			return false;
		} else if(versionNO == MineReset.curVer && subVersionNO == MineReset.curSubVer){
			return true;
		}
		
		return false;
		
	}
	
	private void formatSource(String source)
	{
		String parts[] = source.split("\\@");
		
		try
		{
			versionNO = Double.parseDouble(parts[1]);
			subVersionNO = Integer.parseInt(parts[2]);
		} catch (NumberFormatException ex)
		{
			ex.printStackTrace();
			log.info("Error while parsing version number!");
		}
		
		urgency = parts[3];
			
		reason = parts[4];
	}
	
	public String getUrgency()
	{
		return urgency;
	}
	
	public String getReason()
	{
		return reason;
	}
}
