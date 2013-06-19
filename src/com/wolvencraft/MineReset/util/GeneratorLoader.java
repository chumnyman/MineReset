/*
 * GeneratorLoader.java
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
                ChatUtil.log("Loaded generator: " + generator.getClass().getSimpleName());
            } catch(ClassNotFoundException cnfe) {
                ChatUtil.getLogger().log(Level.WARNING, "Error loading " + name + "! Generator disabled. [ClassNotFoundException]");
            } catch(InstantiationException ie) {
                ChatUtil.getLogger().log(Level.WARNING, "Error loading " + name + "! Generator disabled. [InstantiationException]");
            } catch(IllegalAccessException iae) {
                ChatUtil.getLogger().log(Level.WARNING, "Error loading " + name + "! Generator disabled. [IllegalAccessException]");
            } catch (Exception ex) {
                ChatUtil.getLogger().log(Level.WARNING, "Error loading " + name + "! Generator disabled. [Exception]");
            } catch (ExceptionInInitializerError eiie) {
                ChatUtil.getLogger().log(Level.WARNING, "Error loading " + name + "! Generator disabled. [ExceptionInInitializer]");
            } catch (Error ex) {
                ChatUtil.getLogger().log(Level.WARNING, "Error loading " + name + "! Generator disabled. [Error]");
            }
        }
        
        return generators;
    }
}