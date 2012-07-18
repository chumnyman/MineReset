package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.AutoUpdater.Updater;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.ChatUtil;
import org.bukkit.ChatColor;

/**
 * @author jjkoletar
 */
public class MetaCommand  implements BaseCommand {
    public void run(String[] args) {
        String title = Language.getString("general.title");
        HelpCommand.formatHeader(20, title);
        ChatUtil.sendMessage(ChatColor.GREEN + "MineReset" + ChatColor.WHITE + " version " + ChatColor.BLUE + Updater.getCurVersion() + "." + Updater.getCurSubVersion());
        ChatUtil.sendMessage(ChatColor.YELLOW + "http://dev.bukkit.org/server-mods/minereset/");
        ChatUtil.sendMessage("Creator: " + ChatColor.AQUA + "bitWolfy");
        ChatUtil.sendMessage("Maintainers: " + ChatColor.AQUA + "bitWolfy " + ChatColor.WHITE + "and " + ChatColor.AQUA + "jjkoletar");
        ChatUtil.sendMessage("Testers: " + ChatColor.AQUA + "ProGamerzFTW");
    }
    
    public void getHelp() {}
}
