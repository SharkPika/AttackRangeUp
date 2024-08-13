package cn.sky.attackrangeup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class CC {
    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static void send(String message, Object ... objects) {
        message = String.format(message, objects);
        message = CC.translate(message);
        console.sendMessage("§eAttackRangeUp §7>> " + message);
    }

    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }
}
