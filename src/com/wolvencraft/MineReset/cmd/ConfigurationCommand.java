package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.MineUtils;
import com.wolvencraft.MineReset.util.Util;

public class ConfigurationCommand
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("config")) {
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
			MineUtils.save(MineReset.getMines());
			Message.sendSuccess("Configuration saved to disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("load")) {
			MineReset.setMines(MineUtils.load(MineReset.getMines()));
			Message.sendSuccess("Configuration loaded from disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("generate")) {
			
			
			Message.sendSuccess("Configuration and language files generated successfully");
			return;
		}
		else {
			Message.sendInvalid(args);
			return;
		}
	}
}
