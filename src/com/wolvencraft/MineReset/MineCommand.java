/*
 * MineCommand.java
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
    
    public boolean run(String[] args) {
        return clazz.run(args);
    }
    
    public boolean isCommand(String arg) {
        return alias.contains(arg);
    }
    
    public boolean run(String arg) {
        String[] args = {"", arg};
        return clazz.run(args);
    }
    
    public void getHelp() {
        clazz.getHelp();
    }
}