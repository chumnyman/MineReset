package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.block.Sign;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Signs;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class SignCmd
{
	public static void run(String args[])
	{
		if(!Util.senderHasPermission("edit"))
		{
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
		{
			Help.getSign();
			return;
		}
		else if(args.length != 2)
		{
			Message.sendInvalid(args);
			return;
		}
		
		Player player = (Player) CommandManager.getSender();
		Block b = player.getTargetBlock(null, 100);
		if(b.getType() != Material.WALL_SIGN && b.getType() != Material.SIGN_POST)
		{
			Message.sendError("The targeted block is not a sign");
			return;
		}
		
		
		if(args[1].equalsIgnoreCase("create"))
		{
			String mineName = CommandManager.getMine();
			if(mineName == null)
			{
				String error = Language.getString("general.mine-not-selected");
				Message.sendError(error);
				return;
			}
			
			if(Signs.signExists(b))
			{
				Message.sendError("This sign has been already defined");
				return;
			}
			
			List<String> signList = Signs.getList("data.list-of-signs");
			String id;
			if(signList.size() == 0)
			{
				id = "1";
			}
			else
			{
				id = ((Integer.parseInt(signList.get(signList.size() - 1))) + 1) + "";
			}
			Signs.setString("signs." + id + ".mine", mineName);
			Signs.setBoolean("signs." + id + ".reset", false);
			Signs.setString("signs." + id + ".world", b.getLocation().getWorld().getName());
			Signs.setInt("signs." + id + ".x", b.getLocation().getBlockX());
			Signs.setInt("signs." + id + ".y", b.getLocation().getBlockY());
			Signs.setInt("signs." + id + ".z", b.getLocation().getBlockZ());
			
			if(b.getState() instanceof Sign)
			{
				Sign sign = (Sign) b.getState();
				for(int i = 0; i < 4; i++)
				{
					Signs.setString("signs." + id + ".lines." + i, sign.getLine(i));
				}
			}
			
			signList.add(id);
			Signs.setList("data.list-of-signs", signList);
			Signs.saveData();
			
			Message.sendSuccess("A new sign was defined successfully");
			return;
		}
		else if(args[1].equalsIgnoreCase("reset"))
		{
			String mineName = CommandManager.getMine();
			if(mineName == null)
			{
				String error = Language.getString("general.mine-not-selected");
				Message.sendError(error);
				return;
			}
			
			if(!Signs.signExists(b))
			{
				Message.sendError("This sign has not been defined for this mine yet");
				return;
			}
			String id = Signs.getId(b);
			
			if(Signs.getBoolean("signs." + id + ".reset"))
			{
				Message.sendSuccess("Right-clicking on this sign will no longer result in a reset of " + mineName);
				Signs.setBoolean("signs." + id + ".reset", false);
			}
			else
			{
				Message.sendSuccess("Right-clicking on this sign will now result in a reset of " + mineName);
				Signs.setBoolean("signs." + id + ".reset", true);
			}
			
			Message.sendSuccess("The sign was successfully deleted");
			Signs.saveData();
			return;
		}
		else if(args[1].equalsIgnoreCase("remove"))
		{
			if(!Signs.signExists(b))
			{
				Message.sendError("This sign has not yet been defined");
				return;
			}
			
			String id = Signs.getId(b);
			
			if(b.getState() instanceof Sign)
			{
				Sign sign = (Sign) b.getState();

				for(int i = 0; i < 4; i++)
				{
					String line = Signs.getString("signs." + id + ".lines." + i);
					if(!line.equals(""))
					{
						sign.setLine(i, line);
					}
				}
				sign.update(true);
			}
			

			Signs.remove("signs." + id);
			List<String> signList = Signs.getList("data.list-of-signs");
			signList.remove(signList.indexOf(id));
			Signs.setList("data.list-of-signs", signList);
			
			Message.sendSuccess("The sign is no longer defined. You can destroy it now.");
			Signs.saveData();
			return;
		}
		else if(args[1].equalsIgnoreCase("update"))
		{
			updateAll(null);
			Message.sendSuccess("All signs were forced to update");
			return;
		}
		else
		{
			Message.sendInvalid(args);
			return;
		}
	}
	
	public static Sign update(Sign sign, String id, String mineName)
	{
		for(int i = 0; i < 4; i++)
		{
			String line = Signs.getString("signs." + id + ".lines." + i);
			if(!line.equals(""))
			{
				line = Util.parseVars(line, mineName);
				sign.setLine(i, line);
			}
		}
		
		return sign;
	}
	
	public static void updateAll(String mineName)
	{
		List<String> signList = Signs.getList("data.list-of-signs");

		if(mineName != null)
		{
			for(String id : signList)
			{
				if(Signs.getString("signs." + id + ".mine").equalsIgnoreCase(mineName))
				{
					Location loc = new Location(
							Bukkit.getWorld(Signs.getString("signs." + id + ".world")),
							(double) Signs.getInt("signs." + id + ".x"),
							(double) Signs.getInt("signs." + id + ".y"),
							(double) Signs.getInt("signs." + id + ".z"));
					BlockState b = loc.getBlock().getState();
					if(b instanceof Sign)
					{
						Sign sign = (Sign) b;
						sign = update(sign, id, Signs.getString("signs." + id + ".mine"));
						sign.update(true);
					}
				}
			}
		}
		else
		{
			if(Util.debugEnabled()) Message.log("Updating everything");
			for(String id : signList)
			{
				Location loc = new Location(
						Bukkit.getWorld(Signs.getString("signs." + id + ".world")),
						(double) Signs.getInt("signs." + id + ".x"),
						(double) Signs.getInt("signs." + id + ".y"),
						(double) Signs.getInt("signs." + id + ".z"));
				BlockState b = loc.getBlock().getState();
				if(b instanceof Sign)
				{
					Sign sign = (Sign) b;
					sign = update(sign, id, Signs.getString("signs." + id + ".mine"));
					sign.update(true);
				}
			}
		}
		return;
	}
}
