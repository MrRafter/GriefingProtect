package me.MrRafter.AntiGrieft.utils;

import java.lang.reflect.Method;
import org.bukkit.ChatColor;
import java.text.DecimalFormat;

public class Utils
{
    private static final DecimalFormat format;
    private static final String PREFIX = "&b&lGriefingProtect&8&l";

    public static String translateChars(final String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void sendMessage(final Object obj, final String message) {
        sendMessage(obj, message, true);
    }

    public static void sendMessage(final Object obj, String msg, final boolean prefix) {
        if (prefix) {
            msg = String.format("%s &7\u27a0&r %s", "&b&lGriefingProtect&8&l", msg);
        }
        msg = translateChars(msg);
        try {
            final Method method = obj.getClass().getMethod("sendMessage", String.class);
            method.setAccessible(true);
            method.invoke(obj, msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        format = new DecimalFormat("#.##");
    }
}
