package com.wolvencraft.MineReset.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.logging.Level;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.generation.BaseGenerator;

public class GeneratorLoader {
    public static List<BaseGenerator> load(List<BaseGenerator> generators) {
		File dir = new File(CommandManager.getPlugin().getDataFolder() + "/generators");
	
		if (!dir.exists()) {
		    dir.mkdir();
		    return generators;
		}
		
		ClassLoader loader;
		try {
			loader = new URLClassLoader(new URL[] { dir.toURI().toURL() }, BaseGenerator.class.getClassLoader());
		} catch (MalformedURLException ex) {
			ChatUtil.getLogger().log(Level.SEVERE, "Error while configuring generator class loader", ex);
			return generators;
		}
			
		for (File file : dir.listFiles()) {
		    if (!file.getName().endsWith(".class")) continue;
		    
		    String name = file.getName().substring(0, file.getName().lastIndexOf("."));
	
		    try {
				Class<?> clazz = loader.loadClass(name);
				Object object = clazz.newInstance();
				if (!(object instanceof BaseGenerator)) {
				    ChatUtil.getLogger().info(clazz.getSimpleName() + " is not a generator class");
				    continue;
				}
				BaseGenerator generator = (BaseGenerator) object;
				generators.add(generator);
				ChatUtil.getLogger().info("Loaded generator: " + generator.getClass().getSimpleName());
			} catch (Exception ex) {
			ChatUtil.getLogger().log(Level.WARNING, "Error loading " + name + "! Generator disabled.");
		    } catch (Error ex) {
		    	ChatUtil.getLogger().log(Level.WARNING, "Error loading " + name + "! Generator disabled.");
		    }
		}
		
		return generators;
    }
}