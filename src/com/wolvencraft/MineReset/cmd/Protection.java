package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class Protection
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("edit"))
		{
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
		{
			Help.getProtection();
			return;
		}
		if(args.length > 5)
		{
			Message.sendInvalid(args);
		}
		
		String curMine = CommandManager.getMine();
		
		if(curMine == null)
		{
			String error = Language.getString("general.mine-not-selected");
			Message.sendError(error);
			return;
		}
		
		if(args[1].equalsIgnoreCase("pvp"))
		{
			String baseNode = "mines." + curMine + ".protection.pvp";
			if(Regions.getBoolean(baseNode + ".enabled"))
			{
				Regions.setBoolean(baseNode + ".enabled", false);
				Message.sendSuccess("PVP has been turned OFF for mine '" + curMine + "'");
			}
			else
			{
				Regions.setBoolean(baseNode + ".enabled", true);
				Message.sendSuccess("PVP has been turned ON for mine '" + curMine + "'");
			}
			Regions.saveData();
		}
		else if(args[1].equalsIgnoreCase("breaking"))
		{
			if(args.length < 3)
			{
				Message.sendInvalid(args);
				return;
			}
			String baseNode = "mines." + curMine + ".protection.breaking";
			if(args[2].equalsIgnoreCase("toggle"))
			{
				if(Regions.getBoolean(baseNode + ".enabled"))
				{
					Regions.setBoolean(baseNode + ".enabled", false);
					Message.sendSuccess("Mine protection has been turned OFF for mine '" + curMine + "'");
				}
				else
				{
					Regions.setBoolean(baseNode + ".enabled", true);
					Message.sendSuccess("Mine breaking protection has been turned ON for mine '" + curMine + "'");
				}
			}
			else if(args[2].equalsIgnoreCase("blacklist"))
			{
				if(args[3].equalsIgnoreCase("toggle"))
				{
					if(Regions.getBoolean(baseNode + ".blacklist.enabled"))
					{
						Regions.setBoolean(baseNode + ".blacklist.enabled", false);
						Message.sendSuccess("Block breaking protection blacklist has been turned OFF for mine '" + curMine + "'");
					}
					else
					{
						Regions.setBoolean(baseNode + ".blacklist.enabled", true);
						Message.sendSuccess("Block breaking protection blacklist has been turned ON for mine '" + curMine + "'");
					}
					
				}
				else if(args[3].equalsIgnoreCase("whitelist"))
				{
					if(Regions.getBoolean(baseNode + ".blacklist.whitelist"))
					{
						Regions.setBoolean(baseNode + ".blacklist.whitelist", false);
						Message.sendSuccess("Block breaking blacklist is no longer treated as a whitelist for mine '" + curMine + "'");
					}
					else
					{
						Regions.setBoolean(baseNode + ".blacklist.whitelist", true);
						Message.sendSuccess("Block breaking blacklist is now treated as a whitelist for mine '" + curMine + "'");
					}
				}
				else if(args[3].equalsIgnoreCase("add"))
				{
					String blockName = args[4];
					MaterialData block = Util.getBlock(args[4]);
					
					if(block == null)
					{
						Message.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<String> blockList = Regions.getList(baseNode + ".blacklist.blocks");
					
					
					// Writing everything down
					blockList.add(block.getItemTypeId() + ":" + block.getData());
					Regions.setList(baseNode + ".blacklist.blocks", blockList);
					
					Regions.saveData();
					
					Message.sendSuccess(blockName + " was added to the block breaking protection blacklist of '" + curMine + "'");
					return;
				}
				else if(args[3].equalsIgnoreCase("remove"))
				{
					MaterialData block = Util.getBlock(args[4]);
					
					if(block == null)
					{
						Message.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<String> blockList = Regions.getList(baseNode + ".blacklist.blocks");
					
					
					int index = blockList.indexOf(block.getItemTypeId() + ":" + block.getData());
					if(index == -1)
					{
						Message.sendError("There is no '" + args[4] + "' in protection blacklist of mine '" + curMine + "'");
						return;
					}
					blockList.remove(index);
					

					Regions.setList(baseNode + ".blacklist.blocks", blockList);
					
					Regions.saveData();
					Message.sendSuccess(args[4] + " was successfully removed from block breaking protection of mine '" + curMine + "'");
					return;
				}
				else
				{
					Message.sendInvalid(args);
					return;
				}
				Regions.saveData();
				return;
			}
			else
			{
				Message.sendInvalid(args);
			}
		}
		else if(args[1].equalsIgnoreCase("placement"))
		{
			String baseNode = "mines." + curMine + ".protection.placement";
			if(args[2].equalsIgnoreCase("toggle"))
			{
				if(Regions.getBoolean(baseNode + ".enabled"))
				{
					Regions.setBoolean(baseNode + ".enabled", false);
					Message.sendSuccess("Block placement protection has been turned OFF for mine '" + curMine + "'");
				}
				else
				{
					Regions.setBoolean(baseNode + ".enabled", true);
					Message.sendSuccess("Block placement protection has been turned ON for mine '" + curMine + "'");
				}
			}
			else if(args[2].equalsIgnoreCase("blacklist"))
			{
				if(args[3].equalsIgnoreCase("toggle"))
				{
					if(Regions.getBoolean(baseNode + ".blacklist.enabled"))
					{
						Regions.setBoolean(baseNode + ".blacklist.enabled", false);
						Message.sendSuccess("Block placement protection blacklist has been turned OFF for mine '" + curMine + "'");
					}
					else
					{
						Regions.setBoolean(baseNode + ".blacklist.enabled", true);
						Message.sendSuccess("Block placement protection blacklist has been turned ON for mine '" + curMine + "'");
					}
					
				}
				else if(args[3].equalsIgnoreCase("whitelist"))
				{
					if(Regions.getBoolean(baseNode + ".blacklist.whitelist"))
					{
						Regions.setBoolean(baseNode + ".blacklist.whitelist", false);
						Message.sendSuccess("Block breaking blacklist is no longer treated as a whitelist for mine '" + curMine + "'");
					}
					else
					{
						Regions.setBoolean(baseNode + ".blacklist.whitelist", true);
						Message.sendSuccess("Block breaking blacklist is now treated as a whitelist for mine '" + curMine + "'");
					}
				}
				else if(args[3].equalsIgnoreCase("add"))
				{
					String blockName = args[4];
					MaterialData block = Util.getBlock(args[4]);
					
					if(block == null)
					{
						Message.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<String> blockList = Regions.getList(baseNode + ".blacklist.blocks");
					
					
					// Writing everything down
					blockList.add(block.getItemTypeId() + ":" + block.getData());
					Regions.setList(baseNode + ".blacklist.blocks", blockList);
					
					Regions.saveData();
					
					Message.sendSuccess(blockName + " was added to the block placement protection blacklist of '" + curMine + "'");
					return;
				}
				else if(args[3].equalsIgnoreCase("remove"))
				{
					MaterialData block = Util.getBlock(args[4]);
					
					if(block == null)
					{
						Message.sendError("Block '"+ args[4] + "' does not exist");
						return;
					}
					
					List<String> blockList = Regions.getList(baseNode + ".blacklist.blocks");
					
					
					int index = blockList.indexOf(block.getItemTypeId() + ":" + block.getData());
					if(index == -1)
					{
						Message.sendError("There is no '" + args[4] + "' in protection blacklist of mine '" + curMine + "'");
						return;
					}
					blockList.remove(index);
					

					Regions.setList(baseNode + ".blacklist.blocks", blockList);
					
					Regions.saveData();
					Message.sendSuccess(args[4] + " was successfully removed from block placement protection of mine '" + curMine + "'");
					return;
				}
				else
				{
					Message.sendInvalid(args);
					return;
				}

				Regions.saveData();
				return;
			}
			else
			{
				Message.sendInvalid(args);
				return;
			}
		}
		else
		{
			Message.sendInvalid(args);
			return;
		}
	}
}
