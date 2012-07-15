package com.wolvencraft.MineReset.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.generation.BaseGenerator;
import com.wolvencraft.MineReset.generation.RandomGenerator;

public class GeneratorUtil {
	
	/**
	 * Loads all the generators from file. Always loads the default <b>RandomGenerator</b> first, then loads everything else.<br />
	 * Invalid generators will be skipped.
	 * @return
	 */
	public static List<BaseGenerator> loadAll() {
		List<BaseGenerator> generators = new ArrayList<BaseGenerator>();
		Object object;
		try {
			BaseGenerator generator;
			object = RandomGenerator.class.newInstance();
			generator = (BaseGenerator) object;
			generators.add(generator);
			ChatUtil.log("Loaded generator: " + generator.getClass().getSimpleName());

			generators = GeneratorLoader.load(generators);
			
			return generators;
		} catch (InstantiationException e) {
			ChatUtil.getLogger().log(Level.SEVERE, "Error occurred while loading RandomGenerator!");
			return generators;
		} catch (IllegalAccessException e) {
			ChatUtil.getLogger().log(Level.SEVERE, "Error occurred while loading RandomGenerator!");
		}
		
		return null;
	}
	
	/**
	 * Returns the generator object that is represented by the name provided
	 * @param name Name of the generator, case insensitive
	 * @return Generator object, or null if none found
	 */
	public static BaseGenerator get(String name) {
		List<BaseGenerator> generators = MineReset.getGenerators();
		ChatUtil.debug(generators.size() + " generators found!");
		for(BaseGenerator generator: generators)
			if(generator.getName().equalsIgnoreCase(name)) return generator;
		if(name.equalsIgnoreCase("")) return generators.get(0);
		return null;
	}
	
	/**
	 * Returns a user-friendly string with a list of generator names
	 * @return List of generator names
	 */
	public static String list() {
		List<BaseGenerator> generators = MineReset.getGenerators();
		String list = "";
		for(BaseGenerator gen : generators) {
			list = list + ChatColor.GOLD + gen.getName() + ChatColor.WHITE + ", ";
		}
		list = list.substring(0, list.length() - 2);
		return list;
	}
}
