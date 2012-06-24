package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.ConfigurationUpdater;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.MineUtil;
import com.wolvencraft.MineReset.util.Util;

public class ConfigurationCommand
{
	public static void run(String[] args)
	{
		if(!Util.hasPermission("edit.config")) {
			ChatUtil.sendDenied(args);
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getConfig();
			return;
		}
		else if(args.length != 2) {
			ChatUtil.sendMessage(Language.getString("error.invalid-arguments"));
			return;
		}
		
		if(args[1].equalsIgnoreCase("save")) {
			MineUtil.saveAll(MineReset.getMines());
			ChatUtil.sendSuccess("Configuration saved to disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("load")) {
			CommandManager.getPlugin().reloadConfig();
			CommandManager.getPlugin().reloadLanguageData();
			MineReset.setMines(MineUtil.loadAll(MineReset.getMines()));
			ChatUtil.sendSuccess("Configuration loaded from disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("import")) {
			ChatUtil.sendSuccess("Data import started");
			ConfigurationUpdater.updateRegions();
			ConfigurationUpdater.updateSigns();
			ChatUtil.sendSuccess("Data import finished");
			return;
		}
		else {
			ChatUtil.sendInvalid(args);
			return;
		}
	}
}
