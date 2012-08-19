package com.wolvencraft.MineReset.util;

import java.util.ArrayList;
import java.util.List;

public enum MineOre {
	CAKE_BLOCK ("cake_block", "cake_block", "cake"),
	COAL_ORE ("coal_ore", "coalore", "coal"),
	DIAMOND_BLOCK ("diamond_block", "diamondblock"),
	DIAMOND_ORE ("diamond_ore", "diamondore", "diamond"),
	EMERALD_BLOCK ("emerald_block", "emeraldblock"),
	EMERALD_ORE ("emerald_ore", "emeraldore", "emerald"),
	GOLD_BLOCK ("gold_block", "goldblock"),
	GOLD_ORE ("gold_ore", "goldore", "gold"),
	IRON_BLOCK ("iron_block", "ironblock"),
	IRON_ORE ("iron_ore", "ironore", "iron"),
	LAPIS_ORE ("lapis_ore", "lapisore", "lapislazuli", "lapis"),
	LAPIS_BLOCK ("lapis_block", "lapisblock"),
	MELON_BLOCK ("melon_block", "pumpkinblock", "melon"),
	NOTE_BLOCK ("note_block", "noteblock"),
	SNOW_BLOCK ("snow_block", "snowblock", "snow"),
	SUGARCANE_BLOCK ("sugarcane_block", "sugarcaneblock", "sugarcane"),
	PUMPKIN_BLOCK ("pumpkin_block", "pumpkinblock", "pumpkin"),
	REDSTONE_ORE ("redstone_ore", "redstoneore", "redstone");
	
	private List<String> alias;
	
	private MineOre(String... alias) {
		this.alias = new ArrayList<String>();
		for(String i : alias) { this.alias.add(i); }
	}
	
	public String getMaterial() { return alias.get(0); }
	public List<String> getAlias() { return alias; }
	
	public static MineOre match(String name) {
		MineOre ores[] = MineOre.class.getEnumConstants();
		for(MineOre ore : ores) {
			if(ore.getAlias().indexOf(name) != -1) return ore;
		}
		return null;
	}
}
