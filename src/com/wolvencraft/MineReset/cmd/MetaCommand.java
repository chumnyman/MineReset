package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.AutoUpdater.Updater;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.Message;
import org.bukkit.ChatColor;

/**
 * @author jjkoletar
 */
public class MetaCommand {
    public static void run(String[] args) {
        String title = Language.getString("general.title");
        HelpCommand.formatHeader(20, title);
        Message.sendMessage(ChatColor.GREEN + "MineReset" + ChatColor.WHITE + " version " + ChatColor.BLUE + Updater.getCurVersion() + "." + Updater.getCurSubVersion());
        Message.sendMessage(ChatColor.YELLOW + "http://dev.bukkit.org/server-mods/minereset/");
        Message.sendMessage("Creator: " + ChatColor.AQUA + "bitWolfy");
        Message.sendMessage("Maintainers: " + ChatColor.AQUA + "bitWolfy " + ChatColor.WHITE + "and " + ChatColor.AQUA + "jjkoletar");
        Message.sendMessage("Testers: " + ChatColor.AQUA + "ProGamerzFTW");


    }
}
