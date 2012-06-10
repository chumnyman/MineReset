package com.wolvencraft.MineReset.config;

import com.wolvencraft.MineReset.CommandManager;

public class ConfigurationUpdater {
	public static void run() {
		if(Configuration.getString("configuration.version").equalsIgnoreCase("2.0.0")) return;
		updateConfiguration();
		updateLanguage();
		
	}
	
	public static void updateConfiguration() {
		Configuration.remove("defaults");
		
		if(!Configuration.getString("configuration.version").equalsIgnoreCase("1.2.1") && !Configuration.getString("configuration.version").equalsIgnoreCase("1.2.2")) {
			Configuration.remove("configuration.teleport-out-of-the-mine-on-reset");
			if(!CommandManager.getPlugin().getConfig().isSet("lag")) {
				CommandManager.getPlugin().getConfig().set("lag.automatic-resets-enabled", true);
				CommandManager.getPlugin().getConfig().set("lag.check-time-every", "20");
				CommandManager.getPlugin().getConfig().set("lag.protection-checks-enabled", "");
				CommandManager.getPlugin().getConfig().set("lag.teleport-out-of-the-mine-on-reset", "");
			}
			if(!CommandManager.getPlugin().getConfig().isSet("versions")) {
				CommandManager.getPlugin().getConfig().set("versions.check-for-new-versions", true);
				CommandManager.getPlugin().getConfig().set("versions.check-for-recommended-builds", true);
				CommandManager.getPlugin().getConfig().set("versions.check-for-development-builds", false);
				CommandManager.getPlugin().getConfig().set("versions.remind-on-login", true);
				CommandManager.getPlugin().getConfig().set("versions.permission-node", "minereset.edit");
			}			
		}
		else {
			CommandManager.getPlugin().getConfig().set("lag.check-time-every", "20");
		}
	}
	
	public static void updateLanguage() {
		CommandManager.getPlugin().getConfig().set("error.command", Language.getString("general.invalid-command"));
		CommandManager.getPlugin().getConfig().set("error.mine-name", Language.getString("general.mine-name-invalid"));
		CommandManager.getPlugin().getConfig().set("error.mine-not-selected", Language.getString("general.mine-not-selected"));
		CommandManager.getPlugin().getConfig().set("error.command", Language.getString("general.invalid-command"));
		CommandManager.getPlugin().getConfig().set("error.arguments", "Invalid parameters. Check your argument count!");
		CommandManager.getPlugin().getConfig().set("error.block-does-not-exist", "Block &c%BLOCK%&f does not exist");
		CommandManager.getPlugin().getConfig().set("error.removing-air", "This value is calculated automatically");

		CommandManager.getPlugin().getConfig().set("editing.mine-selected-successfully", Language.getString("general.mine-selected-successfully"));
		CommandManager.getPlugin().getConfig().set("editing.mine-deselected-successfully", Language.getString("general.mine-deselected-successfully"));
	}
}
