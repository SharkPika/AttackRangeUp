package cn.sky.attackrangeup;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AttackRangeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            AttackRangeUp.instance.reloadConfig();
            AttackRangeUp.instance.saveDefaultConfig();
            sender.sendMessage("§a成功重载配置文件");
        }
        else if (args.length == 3) {
            if (AttackRangeUp.isNumeric(args[0]) && Double.parseDouble(args[1]) >= 0.0 && Double.parseDouble(args[2]) >= 0.0 && sender instanceof Player) {
                if (sender.hasPermission("attrange.add") && ((Player) sender).getItemInHand().getType() != Material.AIR) {
                    if (((Player) sender).getItemInHand().getItemMeta().hasLore()) {
                        ItemMeta im = ((Player) sender).getItemInHand().getItemMeta();
                        List<String> l = im.getLore();
                        for (String s : l) {
                            if (s.indexOf("§a范围: §6") != 0) continue;
                            int i = l.indexOf(s);
                            l.remove(i);
                            l.add(i, "§a范围: §6" + args[0] + " §c威力: §6" + args[1] + " §b冷却: §6" + args[2]);
                            im.setLore(l);
                            ((Player) sender).getItemInHand().setItemMeta(im);
                            sender.sendMessage("§a成功修改手中武器的范围攻击能力");
                            return true;
                        }
                        l.add("§a范围: §6" + args[0] + " §c威力: §6" + args[1] + " §b冷却: §6" + args[2]);
                        im.setLore(l);
                        ((Player) sender).getItemInHand().setItemMeta(im);
                        sender.sendMessage("§a成功为手中武器添加范围攻击能力");
                        return true;
                    }
                    ItemMeta im = ((Player) sender).getItemInHand().getItemMeta();
                    ArrayList<String> l = new ArrayList<>();
                    l.add("§a范围: §6" + args[0] + " §c威力: §6" + args[1] + " §b冷却: §6" + args[2]);
                    im.setLore(l);
                    ((Player) sender).getItemInHand().setItemMeta(im);
                    sender.sendMessage("§a成功为手中武器添加范围攻击能力");
                    return true;
                }
                if (((Player) sender).getItemInHand().getType() == Material.AIR) {
                    sender.sendMessage("§c手持物品再使用该指令");
                } else {
                    sender.sendMessage("§c你没有attrange.add权限");
                }
                return true;
            }
            if (sender instanceof Player) {
                sender.sendMessage("§c正确格式：/attrange [范围] [威力] [冷却时间]");
                return true;
            }
            sender.sendMessage("§b恭喜你，现在你的控制台拥有了范围攻击能力");
            return true;
        } else {
            sender.sendMessage("§c正确格式：/attrange [范围] [威力] [冷却时间]");
        }
        return false;
    }
}
