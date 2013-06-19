/*
 * FetchSource.java
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

public class FetchSource {
    
    protected static Map<String, String> fetchSource() {
        URL url = null;
        try {
            url = new URL("http://update.wolvencraft.com/MineReset/");
        }
        catch(MalformedURLException ex) {
            ChatUtil.log("Error occurred while connecting to the update server!");
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
