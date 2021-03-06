/*
 * Updater.java
 * 
 * MineReset
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.AutoUpdater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.util.ChatUtil;


public class Updater {
    private static final double curVer = 2.0;
    private static final int curSubVer = 0;
    
    private static Map<String, String> data;
    private static double newVer;
    private static int newSubVer, newBuild;
    private static String reason, devReason, urgency;
    
    public static boolean checkVersion() {
        if(Configuration.getString("updater.channel").equalsIgnoreCase("none"))
            return true;
        
        data = FetchSource.fetchSource();
        if(data == null) return true;
        formatSource();
        
        String channel = Configuration.getString("updater.channel");
        
        String verString;
        if(channel.equalsIgnoreCase("db")) {
            if(newVer <= curVer || (newVer == curVer && newSubVer <= curSubVer))
                return true;
            else {
                verString = "v. " + curVer + "." + curSubVer + "  ---> " + "Build #" + newBuild;
                reason = devReason + " Urgency: " + urgency;
            }
        }
        else if(channel.equalsIgnoreCase("rb")) {
            if(newVer <= curVer)
                return true;
            else {
                verString = "v. " + curVer + "." + curSubVer + "  --->  " + "v. " + newVer + "." + newSubVer;
                reason = reason + " Urgency: " + urgency;
            }
        }
        else
            return true;
        
        List<String> reasonString = new ArrayList<String>();
        String[] words = reason.split(" ");
        String temp = "";
        for(int i = 0; i < words.length; i++) {
            if((temp.length() + words[i].length() + 1) <= 40)
                temp = temp + words[i] + " ";
            else {
                reasonString.add(centerString(temp.substring(0, temp.length() - 1)));
                ChatUtil.debug("String added: " + temp);
                temp = "";
                i--;
            }
        }
        reasonString.add(centerString(temp.substring(0, temp.length() - 1)));
            
        verString = centerString(verString);

        ChatUtil.log(" +------------------------------------------+");
        ChatUtil.log(" |        MineReset is not up to date!      |");
        ChatUtil.log(" |          http://bit.ly/MineReset/        |");
        ChatUtil.log(" |                                          |");
        ChatUtil.log(" | " + verString + " |");
        ChatUtil.log(" |                                          |");
        for(String displayReason : reasonString)
            ChatUtil.log(" | " + displayReason + " |");
        ChatUtil.log(" +------------------------------------------+");
        
        return false;
        
    }
    
    private static void formatSource() {
        try {
            newVer = Double.parseDouble(data.get("version"));
            newSubVer = Integer.parseInt(data.get("subVersion"));
            newBuild = Integer.parseInt(data.get("build"));
        }
        catch (NumberFormatException ex) {
            ex.printStackTrace();
            ChatUtil.log("Error while parsing version number!");
        }
        
        urgency = data.get("urgency");
        reason = data.get("reason");
        devReason = data.get("devReason");
    }
    
    private static String centerString(String str) {
        while(str.length() < 40) {
            str = " " + str + " ";
        }
        if(str.length() == 41)
            str = str.substring(0, str.length() - 1);
        return str;
    }
    
    public static String getUrgency() {
        return urgency;
    }
    
    public static String getReason() {
        return reason;
    }
    
    public static double getVersion() {
        return newVer;
    }
    
    public static double getCurVersion() {
        return curVer;
    }
    
    public static int getSubVersion() {
        return newSubVer;
    }
    
    public static double getCurSubVersion() {
        return curSubVer;
    }
}
