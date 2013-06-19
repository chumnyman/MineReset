package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.ConfigurationUpdater;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineError;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.SignUtil;
import com.wolvencraft.MineReset.util.Util;

public class DataCommand implements BaseCommand {
    public boolean run(String[] args) {
        if(!Util.hasPermission("edit.admin")) {
            ChatUtil.sendInvalid(MineError.ACCESS, args);
            return false;
        }
        
        if(args.length == 1) {
            getHelp();
            return true;
        }
        
        if(args.length != 2) {
            ChatUtil.sendInvalid(MineError.ARGUMENTS, args);
            return false;
        }
        
        if(args[1].equalsIgnoreCase("save")) {
            MineUtil.saveAll();
            SignUtil.saveAll();
            ChatUtil.sendSuccess("Mine and sign data saved to disc");
            return true;
        }
        else if(args[1].equalsIgnoreCase("load")) {
            CommandManager.getPlugin().reloadConfig();
            CommandManager.getPlugin().reloadLanguageData();
            MineReset.setMines(MineUtil.loadAll());
            MineReset.setSigns(SignUtil.loadAll());
            ChatUtil.sendSuccess("Mine and sign data loaded from disc");
            return true;
        }
        else if(args[1].equalsIgnoreCase("import")) {
            ChatUtil.sendSuccess("Data import started");
            ConfigurationUpdater.updateRegions();
            ConfigurationUpdater.updateSigns();
            ChatUtil.sendSuccess("Data import finished");
            return true;
        }
        else {
            ChatUtil.sendInvalid(MineError.INVALID, args);
            return false;
        }
    }
    
    public void getHelp() {
        ChatUtil.formatHeader(20, "Data");
        ChatUtil.formatHelp("data", "save", "Saves the mine data to file");
        ChatUtil.formatHelp("data", "load", "Loads the mine data from file");
        ChatUtil.formatHelp("data", "import", "Imports the old-formatted data to the new format");
    }
}
