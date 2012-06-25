package com.wolvencraft.MineReset.util;

import java.util.List;
import java.util.logging.Level;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.generation.BaseGenerator;
import com.wolvencraft.MineReset.generation.RandomGenerator;
import com.wolvencraft.MineReset.generation.SnapshotGenerator;

public class GeneratorUtil {
	public static List<BaseGenerator> loadDefault(List<BaseGenerator> generators) {
		Object object;
		try {
			BaseGenerator generator;
			object = RandomGenerator.class.newInstance();
			generator = (BaseGenerator) object;
			generators.add(generator);
			ChatUtil.log("Loaded generator: " + generator.getClass().getSimpleName());
			
			object = SnapshotGenerator.class.newInstance();
			generator = (BaseGenerator) object;
			generators.add(generator);
			ChatUtil.log("Loaded generator: " + generator.getClass().getSimpleName());

			ChatUtil.debug(generators.size() + " default generators loaded");
			return generators;
		} catch (InstantiationException e) {
			ChatUtil.getLogger().log(Level.SEVERE, "Error occurred while loading RandomGenerator!");
			return generators;
		} catch (IllegalAccessException e) {
			ChatUtil.getLogger().log(Level.SEVERE, "Error occurred while loading RandomGenerator!");
		}
		
		return null;
	}
	
	public static BaseGenerator get(String name) {
		List<BaseGenerator> generators = MineReset.getGenerators();
		ChatUtil.debug(generators.size() + " generators found!");
		for(BaseGenerator generator: generators) {
			ChatUtil.log("Checking generator: " + generator.getClass().getSimpleName() + " (" + generator.getName() + ")");
			if(generator.getName().equalsIgnoreCase(name)) return generator;
		}
		
		return null;
	}
}
