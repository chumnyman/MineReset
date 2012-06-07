package com.wolvencraft.MineReset.util;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import com.wolvencraft.MineReset.MineReset;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.mine.SignClass;

public class SignUtils {
	/**
	 * Checks if a sign exists at a specified location
	 * @param loc Location to check
	 * @return true if a sign exists, false otherwise
	 */
	public static boolean exists(Location loc) {
		//TODO Stub
		return true;
	}
	
	/**
	 * Returns the sign at a specified location if it exists
	 * @param loc Location to check
	 * @return SignClass object if it exists, null otherwise
	 */
	public static SignClass getSignAt(Location loc) {
		//TODO Stub
		return null;
	}
	
	/**
	 * Checks if a specified sign is defined as a SignClass
	 * @param sign Sign to check
	 * @return SignClass object if it is defined, null otherwise
	 */
	public static SignClass getSign(Sign sign) {
		//TODO stub
		return null;
	}
	
	/**
	 * Updates a sign specified with the values of the variables of its parent mine
	 * @param signBlock Sign to update
	 * @return An updated sign
	 */
	public static Sign update(Sign signBlock) {
		SignClass sign = SignUtils.getSign(signBlock);
		for(int i = 0; i < 4; i++) {
			String line = sign.getLines().get(i);
			if(!line.equals("")) {
				line = Util.parseVars(line, sign.getParent().getParent());
				signBlock.setLine(i, line);
			}
		}
		
		return signBlock;
	}
	
	/**
	 * Updates all the signs related to the mine specified.<br />
	 * If no mine is specified, updates everything.
	 * @param curMine Mine to update the signs of or <i>null</i> to update all the signs
	 */
	public static void updateAll(Mine curMine) {
		List<SignClass> signList = MineReset.getSigns();

		if(curMine != null) {
			for(SignClass sign : signList) {
				if(sign.getParent().equals(curMine)) {
					BlockState b = sign.getLocation().getBlock().getState();
					if(b instanceof Sign) {
						Sign signBlock = (Sign) b;
						signBlock = update(signBlock);
						signBlock.update(true);
					}
				}
			}
		}
		else {
            Message.debug("Updating everything");
			for(SignClass sign : signList) {
				BlockState b = sign.getLocation().getBlock().getState();
				if(b instanceof Sign) {
					Sign signBlock = (Sign) b;
					signBlock = update(signBlock);
					signBlock.update(true);
				}
			}
		}
		return;
	}
}
