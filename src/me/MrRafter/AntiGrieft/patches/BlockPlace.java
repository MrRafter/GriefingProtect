package me.MrRafter.AntiGrieft.patches;

import me.MrRafter.AntiGrieft.Main;
import me.MrRafter.AntiGrieft.utils.Utils;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.Listener;

public class BlockPlace implements Listener
{
    Main plugin;
    
    public BlockPlace(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        try {
            final Player player = event.getPlayer();
            if (this.plugin.getConfig().getBoolean("IllegalBlock-Place.Enabled")) {
                switch (event.getBlock().getType()) {
                    case BEDROCK: {
                        event.setCancelled(true);
                        Utils.sendMessage(player, this.plugin.getConfig().getString("IllegalBlock-Place.Bedrock"));
                        event.getPlayer().getInventory().getItemInHand().setType(Material.AIR);
                        break;
                    }
                    case ENDER_PORTAL_FRAME: {
                        if (player.getInventory().getItemInHand().getType() != Material.EYE_OF_ENDER && player.getInventory().getItemInHand().getType() != Material.EYE_OF_ENDER) {
                            event.setCancelled(true);
                            Utils.sendMessage(player, this.plugin.getConfig().getString("IllegalBlock-Place.End_Portal_Frame"));
                            event.getPlayer().getInventory().getItemInHand().setType(Material.AIR);
                            break;
                        }
                        break;
                    }
                    case BARRIER: {
                        event.setCancelled(true);
                        Utils.sendMessage(player, this.plugin.getConfig().getString("IllegalBlock-Place.Barrier"));
                        event.getPlayer().getInventory().getItemInHand().setType(Material.AIR);
                        break;
                    }
                    case MOB_SPAWNER: {
                        event.setCancelled(true);
                        Utils.sendMessage(player, this.plugin.getConfig().getString("IllegalBlock-Place.Mob_Spawner"));
                        event.getPlayer().getInventory().getItemInHand().setType(Material.AIR);
                        break;
                    }
                }
            }
        }
        catch (Error error) {}
        catch (Exception ex) {}
    }
    
    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        try {
            if (event.getBlock().getType() == Material.BEDROCK) {
                event.setCancelled(true);
            }
        }
        catch (Error error) {}
        catch (Exception ex) {}
    }
}
