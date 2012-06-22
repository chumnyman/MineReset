package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.ConfigurationUpdater;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.MineUtils;
import com.wolvencraft.MineReset.util.Util;

public class ConfigurationCommand
{
	public static void run(String[] args)
	{
		if(!Util.hasPermission("edit.config")) {
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getConfig();
			return;
		}
		else if(args.length != 2) {
			Message.sendMessage(Language.getString("error.invalid-arguments"));
			return;
		}
		
		if(args[1].equalsIgnoreCase("save")) {
			MineUtils.saveAll(MineReset.getMines());
			Message.sendSuccess("Configuration saved to disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("load")) {
			CommandManager.getPlugin().reloadConfig();
			CommandManager.getPlugin().reloadLanguageData();
			MineReset.setMines(MineUtils.loadAll(MineReset.getMines()));
			Message.sendSuccess("Configuration loaded from disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("import")) {
			Message.sendSuccess("Data import started");
			ConfigurationUpdater.updateRegions();
			ConfigurationUpdater.updateSigns();
			Message.sendSuccess("Data import finished");
			return;
		}
		else {
			Message.sendInvalid(args);
			return;
		}
	}
}
