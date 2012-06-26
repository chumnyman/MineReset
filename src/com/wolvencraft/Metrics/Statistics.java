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
		
		resets.addPlotter(new Metrics.Plotter("Overall") {
			
			@Override
			public int getValue() {
				return (viaSign + viaCommand + automatic);
			}
		});
		
		resets.addPlotter(new Metrics.Plotter("Via Command") {
			
			@Override
			public int getValue() {
				return viaCommand;
			}
		});
		
		resets.addPlotter(new Metrics.Plotter("Via Sign") {
			
			@Override
			public int getValue() {
				return viaSign;
			}
		});
		
		resets.addPlotter(new Metrics.Plotter("Automatic") {
			
			@Override
			public int getValue() {
				return automatic;
			}
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
