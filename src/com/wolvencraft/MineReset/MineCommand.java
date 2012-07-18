package com.wolvencraft.MineReset;

import java.util.ArrayList;
import java.util.List;

import com.wolvencraft.MineReset.cmd.*;
import com.wolvencraft.MineReset.util.ChatUtil;

public enum MineCommand {
	BLACKLIST (BlacklistCommand.class, "blacklis", "bl"),
	DATA (DataCommand.class, "data"),
	EDIT (EditCommand.class, "edit", "none", "add", "+", "remove", "-", "delete", "del", "name", "silent", "generator", "link", "setparent", "cooldown"),
	HELP (HelpCommand.class, "help", "?"),
	INFO (InfoCommand.class, "info", "time", "list"),
	META (MetaCommand.class, "meta", "about"),
	PROTECTION (ProtectionCommand.class, "protection", "prot"),
	RESET (ResetCommand.class, "reset"),
	SAVE (SaveCommand.class, "save"),
	SELECT (SelectCommand.class, "select", "pos1", "pos2", "hpos1", "hpos2"),
	SIGN (SignCommand.class, "sign"),
	TIMER (TimerCommand.class, "timer", "auto"),
	WAND (WandCommand.class, "wand"),
	WARP (WarpCommand.class, "warp", "tp");
	
	@SuppressWarnings("rawtypes")
	MineCommand(Class clazz, String... args) {
		try {
			this.clazz = (BaseCommand) clazz.newInstance();
			alias = new ArrayList<String>();
			for(String arg : args) {
				alias.add(arg);
			}
		} catch (InstantiationException e) {
			ChatUtil.getLogger().severe("Error while instantiating a command! InstantiationException");
			return;
		} catch (IllegalAccessException e) {
			ChatUtil.getLogger().severe("Error while instantiating a command! IllegalAccessException");
			return;
		}
	}
	
	private BaseCommand clazz;
	private List<String> alias;
	
	public BaseCommand get() {
		return clazz;
	}
	
	public void run(String[] args) {
		clazz.run(args);
	}
	
	public boolean isCommand(String arg) {
		return alias.contains(arg);
	}
	
	public void run(String arg) {
		String[] args = {"", arg};
		clazz.run(args);
	}
	
	public void getHelp() {
		clazz.getHelp();
	}
}