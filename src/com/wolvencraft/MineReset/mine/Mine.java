package com.wolvencraft.MineReset.mine;

import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.RandomBlock;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.MaterialData;

import java.util.List;

/**
 * @author jjkoletar
 */
public class Mine {
    private Location one;
    private Location two;
    private World world;
    private Location tpPoint;
    private String name;
    private List<MineBlock> blocks;
    private boolean silent;
    private boolean automatic;
    private int automaticSeconds;
    private boolean warned;
    private List<Integer> warningTimes;
    private List<Protection> enabledProtection;

    public Mine(Location one, Location two, World world, Location tpPoint, String name, List<MineBlock> blocks, boolean isSilent, boolean isAutomatic, int automaticSeconds, boolean isWarned, List<Integer> warningTimes, List<Protection> enabledProtection) {
        this.one = one;
        this.two = two;
        this.world = world;
        this.tpPoint = tpPoint;
        this.name = name;
        this.blocks = blocks;
        silent = isSilent;
        automatic = isAutomatic;
        this.automaticSeconds = automaticSeconds;
        warned = isWarned;
        this.warningTimes = warningTimes;
        this.enabledProtection = enabledProtection;
    }

    public void reset() {
        removePlayers();
        //TODO: Implement whitelist/blacklist
        RandomBlock pattern = new RandomBlock(blocks);
        for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
                    MaterialData block = pattern.next();
                    world.getBlockAt(x, y, z).setType(block.getItemType());
                    world.getBlockAt(x, y, z).setData(block.getData());
                }
            }
        }
    }

    private void removePlayers() {
        for (Player p : world.getPlayers()) {
            if (isLocationInMine(p.getLocation())) {
                p.teleport(tpPoint, PlayerTeleportEvent.TeleportCause.PLUGIN);
                Message.sendPlayer(p, Language.getMessage("teleportation.mine-teleport", name));
            }
        }
    }

    public boolean isLocationInMine(Location l) {
        return (l.getX() >= one.getX() && l.getX() <= two.getX())     //if x matches
                && (l.getY() >= one.getY() && l.getY() <= two.getY()) //and y
                && (l.getZ() >= one.getZ() && l.getZ() <= two.getZ());//and z
    }
}
