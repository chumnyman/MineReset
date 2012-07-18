package com.wolvencraft.MineReset.generation;

import com.wolvencraft.MineReset.mine.Mine;

public interface BaseGenerator {
	
	/**
	 * Resets the mine specified as a parameter.
	 * @param mine Mine to be reset
	 * @return true is reset was successful, false otherwise
	 */
	public boolean run(Mine mine);
	
	/**
	 * Block that is run when the generator is being initialized. If your generator does not require initialization, 
	 * leave this method empty, returning true.
	 * @param mine Mine to be used with the generator
	 * @return true if initialization was successful, false otherwise
	 */
	public boolean init(Mine mine);
	
	/**
	 * Returns the name of the generator
	 * @return The name of the generator
	 */
	public String getName();
	
	/**
	 * Returns the description of the generator
	 * @return Description of the generator
	 */
	public String getDescription();
	
}
