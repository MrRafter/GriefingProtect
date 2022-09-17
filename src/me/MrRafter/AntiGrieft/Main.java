package me.MrRafter.AntiGrieft;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import lombok.Getter;
import me.MrRafter.AntiGrieft.events.CommandPreProcess;
import me.MrRafter.AntiGrieft.patches.BlockPlace;
import me.MrRafter.AntiGrieft.patches.DeopOnLeave;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.*;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class Main extends JavaPlugin implements Listener
{

    ProtocolManager protocolManager;

    FileConfiguration config;

    List<String> plugins = new ArrayList<String>();
    List<String> version = new ArrayList<String>();
    List<String> about = new ArrayList<String>();
    List<String> question = new ArrayList<String>();

    Boolean BlockPlugins, BlockVersion, BlockAbout, BlockQuestionMark;

    String pluginsDeny, versionDeny, aboutDeny, qmDeny;

    @Getter
    private static Main instance;
    @Getter
    private final long startTime = System.currentTimeMillis();
    public PluginManager pluginManager;

    public static Main getInstance() {
        return Main.instance;
    }

    public void onEnable() {
        config = getConfig();

        saveDefaultConfig();

        plugins.add("pl");
        plugins.add("bukkit:pl");
        plugins.add("plugins");
        plugins.add("bukkit:plugins");
        version.add("ver");
        plugins.add("bukkit:ver");
        version.add("version");
        plugins.add("bukkit:version");
        about.add("about");
        plugins.add("bukkit:about");
        question.add("?");
        plugins.add("bukkit:?");

        BlockPlugins = config.getBoolean("BlockPlugins");
        BlockVersion = config.getBoolean("BlockVersion");
        BlockAbout = config.getBoolean("BlockAbout");
        BlockQuestionMark = config.getBoolean("BlockQuestionMark");

        pluginsDeny = config.getString("Plugins").replaceAll("&", "§");
        versionDeny = config.getString("Version").replaceAll("&", "§");
        aboutDeny = config.getString("About").replaceAll("&", "§");
        qmDeny = config.getString("QuestionMark").replaceAll("&", "§");

        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.protocolManager.addPacketListener(new PacketAdapter(this,
                ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE)
                    try {
                        if (event.getPlayer().hasPermission(
                                "lib.commandtab.bypass"))
                            return;
                        PacketContainer packet = event.getPacket();
                        String message = (String) packet
                                .getSpecificModifier(String.class).read(0)
                                .toLowerCase();


                        if ((message.startsWith("/") && !message.contains(" "))
                                || (message.startsWith("/" + plugins) && !message.contains(" "))
                                || (message.startsWith("/" + version) && !message.contains(" "))
                                || (message.startsWith("/" + about) && !message.contains(" "))
                                || (message.startsWith("/" + question) && !message.contains(" "))){
                            event.setCancelled(true);
                        }
                    } catch (FieldAccessException e) {
                        Main.this.getLogger().log(Level.SEVERE,
                                "Couldn't access field.", e);
                    }
            }
        });

        //Register Events
        getLogger().info("Registering Events...");
        pluginManager = getServer().getPluginManager();
        register (

                new BlockPlace(this),
                new CommandPreProcess(this),
                new DeopOnLeave(this)
        );

        if (this.getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            this.getLogger().info("Detected ProtocolLib!");
        }
        else {
            this.getLogger().warning("Cannot find ProtocolLib. Please Install ProtocolLib.");
        }
        this.getLogger().info("is Loaded and Enabled!");

    }
    
    public void onDisable() {
        Main.instance = null;
        this.getLogger().info(" is Unloaded and Disabled!");
    }

    private void register(final Listener... list) {
        this.pluginManager.registerEvents((Listener)this, (Plugin)this);
        for (final Listener listener : list) {
            this.pluginManager.registerEvents(listener, (Plugin)this);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent evt) {
        if (evt.getMessage().toLowerCase().startsWith("/help")) {
            this.getConfig().getList("help").forEach(b -> evt.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', (String)b)));
            evt.setCancelled(true);
        }
    }
    
    @EventHandler
    private void commandEvent(final ServerCommandEvent event) {
        if (event.getCommand().split(" ")[0].equalsIgnoreCase("say")) {
            event.setCancelled(true);
            final String output = event.getCommand().substring(3);
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Say.Format").replace("%message%", output)));
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String[] arrayOfString;
        int j = (arrayOfString = event.getMessage().split(" ")).length;

        for(int i = 0; i < j; ++i) {
            String bcmd = arrayOfString[i];
            if (this.getConfig().getStringList("BLOCK_COMMANDS").contains(bcmd.toLowerCase())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.getConfig().getString("NO_PERMISSIONS").replace("&", "§"));
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();

        String[] msg = event.getMessage().split(" ");

        if (!player.hasPermission("lib.commandtab.bypass")) {

            if (BlockPlugins) {
                for (String Loop : plugins) {
                    if (msg[0].equalsIgnoreCase("/" + Loop)) {
                        player.sendMessage(pluginsDeny.replaceAll("%player",
                                player.getName()));
                        event.setCancelled(true);
                    }
                }
            }

            if (BlockVersion) {
                for (String Loop : version) {
                    if (msg[0].equalsIgnoreCase("/" + Loop)) {
                        player.sendMessage(versionDeny.replaceAll("%player",
                                player.getName()));
                        event.setCancelled(true);
                    }
                }
            }

            if (BlockAbout) {
                for (String Loop : about) {
                    if (msg[0].equalsIgnoreCase("/" + Loop)) {
                        player.sendMessage(aboutDeny.replaceAll("%player",
                                player.getName()));
                        event.setCancelled(true);
                    }
                }
            }

            if (BlockQuestionMark) {
                for (String Loop : question) {
                    if (msg[0].equalsIgnoreCase("/" + Loop)) {
                        player.sendMessage(qmDeny.replaceAll("%player",
                                player.getName()));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("gp")) {
            if (sender.hasPermission("gp.reload")) {
                sender.sendMessage("§4[§bGriefingProtect§4] §cReloaded Configuration File");
                reloadConfig();
            } else {
                sender.sendMessage("&b&lGriefingProtect&8&l >> &cYou do not have sufficient permissions");
            }
        }
        return false;
    }

    public void registerCommand(String name, CommandExecutor... commands) {
        CraftServer cs = (CraftServer) Bukkit.getServer();
        for (CommandExecutor command : commands) {
            cs.getCommandMap().register(name, new Command(name) {
                @Override
                public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                    command.onCommand(sender, this, commandLabel, args);
                    return true;
                }
            });
        }
    }

    public static Main getPlugin(){
        return getPlugin(Main.class);
    }
}
