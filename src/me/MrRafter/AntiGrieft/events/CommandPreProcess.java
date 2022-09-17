package me.MrRafter.AntiGrieft.events;

import lombok.RequiredArgsConstructor;
import me.MrRafter.AntiGrieft.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@RequiredArgsConstructor
public class CommandPreProcess implements Listener {
    private final Main plugin;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent evt) {
        FileConfiguration config = plugin.getConfig();

        String msg = evt.getMessage().toLowerCase();
        if (config.getBoolean("AllowOPStobypass")) {
            if (config.getBoolean("CommandWhitelist") && !config.getList("CommandsWhitelisted").contains(msg.split(" ")[0])) {
                if (!evt.getPlayer().isOp()) {
                    {
                        evt.getPlayer().sendMessage(ChatColor.DARK_RED + "Mal comando. Escriba /help para todos los comandos.");
                    }
                    evt.setCancelled(true);
                }
            }
        } else {
            if (config.getBoolean("CommandWhitelist") && !config.getList("CommandsWhitelisted").contains(msg.split(" ")[0])) {
                {
                    evt.getPlayer().sendMessage(ChatColor.DARK_RED + "Mal comando. Escriba /help para todos los comandos.");
                }

                evt.setCancelled(true);
            }
        }
    }
}
