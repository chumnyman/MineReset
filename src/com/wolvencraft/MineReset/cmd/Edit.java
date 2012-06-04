package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.util.MineUtils;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class Edit
{
	private static String curMine;
	
	public static void run(String[] args)
	{
		if(Util.isPlayer())
		{
			if(!Util.senderHasPermission("edit"))
			{
				Message.sendDenied(args);
				return;
			}
		}
		
		if(!args[0].equalsIgnoreCase("none") && args.length == 1)
		{
			Help.getEdit();
			return;
		}
		if(args.length > 3)
		{
			Message.sendInvalid(args);
		}
		
		curMine = CommandManager.getMine();
		
		if(args[0].equalsIgnoreCase("edit"))
		{
			if(args.length != 2)
			{
				Message.sendInvalid(args);
				return;
			}
			String mineName = args[1];
			if(!MineUtils.exists(mineName))
			{
				String error = Language.getString("general.mine-name-invalid");
				error = Util.parseString(error, "%MINE%", mineName);
				Message.sendError(error);
				return;
			}
			CommandManager.setMine(mineName);
			String message = Language.getString("general.mine-selected-successfully");
			String displayName = Regions.getString("mines." + mineName + ",display-name");
			message = Util.parseString(message, "%MINE%", mineName);
			message = Util.parseString(message, "%MINENAME%", displayName);
			Message.sendSuccess(message);
		}
		else if(args[0].equalsIgnoreCase("none"))
		{
			if(args.length != 1)
			{
				Message.sendInvalid(args);
				return;
			}
			String mineName = CommandManager.getMine();
			String message = Language.getString("general.mine-deselected-successfully");
			String displayName = Regions.getString("mines." + mineName + ",display-name");
			message = Util.parseString(message, "%MINE%", mineName);
			message = Util.parseString(message, "%MINENAME%", displayName);
			CommandManager.setMine(null);
			Message.sendSuccess(message);
		}
		else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("+"))
		{
			if(curMine == null)
			{
				String error = Language.getString("general.mine-not-selected");
				Message.sendError(error);
				return;
			}
			
			if(args.length != 3)
			{
				Message.sendError("I find your lack of parameters disturbing");
				return;
			}
			
			String blockName = args[1];
			MaterialData block = Util.getBlock(blockName);
			
			if(block == null)
			{
				Message.sendError("Block '"+ args[1] + "' does not exist");
				return;
			}
			
			
			List<String> itemList = Regions.getList("mines." + curMine + ".materials.blocks");
			List<String> weightList = Regions.getList("mines." + curMine + ".materials.weights");
			
			if(itemList.size() != weightList.size())
			{
				Message.sendError("Your regions.yml file has been corrupted");
				return;
			}
			
			if(itemList.size() == 0)
			{
				itemList.add("0:0");
				weightList.add("100");
			}
			
			if(block.getItemTypeId() == Integer.parseInt(itemList.get(0).split(":")[0]))
			{
				Message.sendError("You do not need to do this. The weight of the default block is calculated automatically.");
				return;
			}
			
			double percent;
			if(Util.isNumeric(args[2]))
			{
				percent = Double.parseDouble(args[2]);
				if(percent <= 0)
				{
					Message.sendInvalid(args);
					return;
				}
				percent = (double)(Math.round(percent * 1000)) / 1000;
			}
			else {
				Message.debug("Argument is not numeric, attempting to parse");
				String awkwardValue = args[2];
				String[] awkArray = awkwardValue.split("%");
				try
				{
				percent = Double.parseDouble(awkArray[0]);
				}
				catch(NumberFormatException nfe)
				{
					Message.sendInvalid(args);
					return;
				}
			}
			Message.debug("Percent value is " + percent);
			
			double percentAvailable = Double.parseDouble(weightList.get(0));
			double newStonePercent;
			if((percentAvailable - percent) < 0)
			{
				Message.sendError("Invalid percentage. Use /mine info " + curMine + " to review the percentages");
				return;
			}
			else newStonePercent = percentAvailable - percent;
			
			newStonePercent = (double)(Math.round(newStonePercent * 1000)) / 1000;
			int index = itemList.indexOf(block.getItemTypeId() + ":" + block.getData());
			Message.debug(block.getItemTypeId() + " ? " + index);
			if(index == -1)
			{
				itemList.add(block.getItemTypeId() + ":" + block.getData());
				weightList.add(""+percent);
			}
			else
			{
				String weight = Double.parseDouble(weightList.get(index)) + percent + "";
				weightList.set(index, weight);
			}
			// Writing everything down
			
			weightList.set(0, ""+newStonePercent);
			Regions.setList("mines." + curMine + ".materials.blocks", itemList);
			Regions.setList("mines."+ curMine + ".materials.weights", weightList);
			
			Regions.saveData();
			
			Message.sendSuccess(percent + "% of " + blockName + " added to " + curMine);
			Message.sendSuccess("Reset the mine for the changes to take effect");
			return;
		}
		else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("-"))
		{
			if(args.length != 2)
			{
				Message.sendError("I find your lack of parameters disturbing");
				return;
			}
			
			if(curMine == null)
			{
				String error = Language.getString("general.mine-not-selected");
				Message.sendError(error);
				return;
			}
			
			MaterialData block = Util.getBlock(args[1]);
			
			if(block == null)
			{
				Message.sendError("Block '"+ args[1] + "' does not exist");
				return;
			}
			
			List<String> itemList = Regions.getList("mines." + curMine + ".materials.blocks");
			List<String> weightList = Regions.getList("mines." + curMine + ".materials.weights");

			if(block.getItemTypeId() == Integer.parseInt(itemList.get(0)))
			{
				Message.sendError("You cannot remove the default block from the mine");
				return;
			}
			
			int index = itemList.indexOf(block.getItemTypeId() + ":" + block.getData());
			Message.debug(block.getItemTypeId() + " ? " + index);
			if(index == -1)
			{
				Message.sendError("There is no '" + args[2] + "' in mine '" + curMine + "'");
				return;
			}
			double oldStoneWeight = Double.parseDouble(weightList.get(0));
			double newStoneWeight = oldStoneWeight + Double.parseDouble("" + weightList.get(index));
			weightList.set(0, "" + newStoneWeight);
			itemList.remove(index);
			weightList.remove(index);
			

			Regions.setList("mines." + curMine + ".materials.blocks", itemList);
			Regions.setList("mines." + curMine + ".materials.weights", weightList);
			
			Regions.saveData();
			Message.sendSuccess(args[1] + " was successfully removed from mine '" + curMine + "'");
			return;
		}
		else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del"))
		{
			CommandManager.getPlugin().getRegionData().set("mines." + curMine, null);
			List<String> regionList = Regions.getList("data.list-of-mines");
			regionList.remove(regionList.indexOf(args[1]));
			Regions.setList("data.list-of-mines", regionList);
			Regions.saveData();
			CommandManager.setMine(null);
			Message.sendSuccess("Mine '" + args[1] + "' was successfully deleted.");
			return;
		}
		else if(args[0].equalsIgnoreCase("name"))
		{
			if(curMine == null)
			{
				String error = Language.getString("general.mine-not-selected");
				Message.sendError(error);
				return;
			}
			
			String name = args[1];
			for(int i = 2; i < args.length; i++)
			{
				name = name + " " + args[i];
			}
			Regions.setString("mines." + curMine + ".display-name", name);
			Message.sendSuccess("Mine '" + curMine + "' now has a display name '" + name + "'");
			Regions.saveData();
			return;
		}
		else if(args[0].equalsIgnoreCase("silent"))
		{
			if(curMine == null)
			{
				String error = Language.getString("general.mine-not-selected");
				Message.sendError(error);
				return;
			}
			
			if(Regions.getBoolean("mines." + curMine + ".silent"))
			{
				Message.sendSuccess("Silent mode has been turned off for mine '" + curMine + "'");
				Regions.setBoolean("mines." + curMine + ".silent", false);
			}
			else
			{
				Message.sendSuccess("'" + curMine + "' will no longer broadcast any notifications");
				Regions.setBoolean("mines." + curMine + ".silent", true);
			}
		}
		else if(args[0].equalsIgnoreCase("generator"))
		{
			if(args.length != 2)
			{
				Message.sendInvalid(args);
				return;
			}
			
			if(curMine == null)
			{
				String error = Language.getString("general.mine-not-selected");
				Message.sendError(error);
				return;
			}
			
			String generator = args[1];
			Regions.setString("mines." + curMine + ".reset.generator", generator.toUpperCase());
		}
		else if(args[0].equalsIgnoreCase("link"))
		{
			if(args.length != 2)
			{
				Message.sendInvalid(args);
				return;
			}
			
			if(curMine == null)
			{
				String error = Language.getString("general.mine-not-selected");
				Message.sendError(error);
				return;
			}
			
			if(!args[1].equalsIgnoreCase("none") && !MineUtils.exists(args[1]))
			{
				String error = Language.getString("general.mine-name-invalid");
				error = Util.parseString(error, "%MINE%", args[1]);
				error = Util.parseString(error, "%MINENAME%", args[1]);
				Message.sendError(error);
				return;
			}
			
			if(args[1].equalsIgnoreCase("none"))
			{
				Regions.remove("mines." + curMine + ".parent");
				Message.sendSuccess("Mine '" + curMine + "' is no longer linked to any mine");
				return;
			}
			
			Regions.setString("mines." + curMine + ".parent", args[1]);
			Message.sendSuccess("Mines '" + curMine + "' will now reset with '" + args[1] + "'");
			return;
		}
		else
		{
			Message.sendInvalid(args);
			return;
		}
	}
}
