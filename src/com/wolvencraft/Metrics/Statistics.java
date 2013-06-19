/*
 * Statistics.java
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

package com.wolvencraft.Metrics;

import java.io.IOException;

import com.wolvencraft.Metrics.Metrics.Graph;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.util.ChatUtil;

public class Statistics {
    
    private Metrics metrics;
    private int viaCommand;
    private int viaSign;
    private int automatic;
    
    public Statistics(MineReset plugin) {
        try {
            this.metrics = new Metrics(plugin);
        } catch (IOException e) {
            ChatUtil.getLogger().severe("Unable to start PluginMetrics");
        }
        
        viaCommand = 0;
        viaSign = 0;
        automatic = 0;
    }
    
    public void gatherData() {
        if(metrics.isOptOut()) return;
        
        Graph resets = metrics.createGraph("Mine Resets");
        
        resets.addPlotter(new Metrics.Plotter("Via Command") {
            @Override
            public int getValue() {
                int temp = viaCommand;
                viaCommand = 0;
                return temp;
            }
        });
        
        resets.addPlotter(new Metrics.Plotter("Via Sign") {
            @Override
            public int getValue() {
                int temp = viaSign;
                viaSign = 0;
                return temp;
            }
        });
        
        resets.addPlotter(new Metrics.Plotter("Automatic") {
            @Override
            public int getValue() {
                int temp = automatic;
                automatic = 0;
                return temp;
            }
        });
        
        Graph mines = metrics.createGraph("Mine & Signs");
        
        mines.addPlotter(new Metrics.Plotter("Mines") {
            @Override
            public int getValue() { return MineReset.getMines().size(); }
        });
        
        mines.addPlotter(new Metrics.Plotter("Signs") {
            @Override
            public int getValue() { return MineReset.getSigns().size(); }
        });
        
        mines.addPlotter(new Metrics.Plotter("Generators") {
            
            @Override
            public int getValue() { return MineReset.getGenerators().size(); }
        });
    }
    
    public Metrics getMetrics() {
        return metrics;
    }
    
    public void updateViaCommand() {
        viaCommand++;
    }
    
    public void updateViaSign() {
        viaSign++;
    }
    
    public void updateAutomatic() {
        automatic++;
    }
}
