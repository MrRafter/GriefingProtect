package me.MrRafter.AntiGrieft.patches;

import me.MrRafter.AntiGrieft.Main;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;

public class DeopOnLeave implements Listener
{
    Main plugin;
    
    public DeopOnLeave(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onLeave(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (this.plugin.getConfig().getBoolean("DeopOnLeave") && player.isOp()) {
            player.setOp(false);
        }
    }
    
    @EventHandler
    public void onKick(final PlayerKickEvent event) {
        final Player player = event.getPlayer();
        if (this.plugin.getConfig().getBoolean("DeopOnLeave") && player.isOp()) {
            player.setOp(false);
        }
    }
}
