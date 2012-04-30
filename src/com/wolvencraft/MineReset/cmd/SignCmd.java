package com.wolvencraft.MineReset.cmd;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.block.Sign;
import org.bukkit.World;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Signs;

public class SignCmd
{
	public static void run(String args[])
	{
		if(!Util.senderHasPermission("edit"))
		{
			Util.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
		{
			Help.getSign();
			return;
		}
		else if(args.length != 2)
		{
			Util.sendInvalid(args);
			return;
		}

		String mineName = CommandManager.getMine();
		if(mineName == null)
		{
			String error = Language.getString("general.mine-not-selected");
			Util.sendError(error);
			return;
		}
		
		if(args[1].equalsIgnoreCase("create"))
		{
			Player player = (Player) CommandManager.getSender();
			Block b = player.getTargetBlock(null, 100);
			if(b.getType() != Material.WALL_SIGN && b.getType() != Material.SIGN_POST)
			{
				Util.sendError("The targeted block is not a sign");
				return;
			}
			
			if(Signs.signExists(b))
			{
				Util.sendError("This sign has been already defined");
				return;
			}
			
			int id = Signs.getInt(mineName + ".num");
			Signs.setBoolean(mineName + "." + id + ".reset", false);
			Signs.setString(mineName + "." + id + ".world", b.getLocation().getWorld().getName());
			Signs.setDouble(mineName + "." + id + ".x", b.getLocation().getBlockX());
			Signs.setDouble(mineName + "." + id + ".y", b.getLocation().getBlockY());
			Signs.setDouble(mineName + "." + id + ".z", b.getLocation().getBlockZ());
			id++;
			Signs.setInt(mineName + ".num", id);
			Signs.saveData();
			
			BlockState state = b.getState();
			if(state instanceof Sign)
			{
				Sign sign = (Sign) state;
				update(sign, mineName);
			}
			return;
		}
		else if(args[1].equalsIgnoreCase("reset"))
		{
			Player player = (Player) CommandManager.getSender();
			Block b = player.getTargetBlock(null, 100);
			if(b.getType() != Material.WALL_SIGN || b.getType() != Material.SIGN_POST)
			{
				Util.sendError("The targeted block is not a sign");
				return;
			}
			
			int id = Signs.getId(mineName, b);
			
			if(Signs.getBoolean(mineName + "." + id + ".reset"))
			{
				Util.sendMessage("Right-clicking on this sign will no longer result in a reset of " + mineName);
				Signs.setBoolean(mineName + "." + id + ".reset", false);
			}
			else
			{
				Util.sendMessage("Right-clicking on this sign will now result in a reset of " + mineName);
				Signs.setBoolean(mineName + "." + id + ".reset", true);
			}
			return;
		}
		else if(args[1].equalsIgnoreCase("remove"))
		{
			// Check if a sign exists
			return;
		}
		else
		{
			Util.sendInvalid(args);
			return;
		}
	}
	
	public static void update(Sign sign, String mineName)
	{
		for(int i = 0; i < 4; i++)
		{
			String line = sign.getLine(i);
			if(!line.equals(""))
			{
				line = Util.parseVars(line, mineName);
				sign.setLine(i, line);
			}
		}
	}
	
	public static void updateAll(String mineName)
	{
		int id = Signs.getInt(mineName + ".num");
		for (int i = 0; i < id; i++)
		{
			World world = CommandManager.getPlugin().getServer().getWorld(mineName + "." + i + ".world");
			Block b = new Location(
					world,
					Signs.getDouble(mineName + "." + i + ".x"),
					Signs.getDouble(mineName + "." + i + ".y"),
					Signs.getDouble(mineName + "." + i + ".z")).getBlock();
			
			if(b.getState() instanceof Sign)
			{
				update((Sign) b.getState(), mineName);
			}
		}
	}
}
