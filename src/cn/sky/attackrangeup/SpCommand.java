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

public class SpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if (sender.hasPermission("sp.change")) {
            if (args.length == 2 && ((Player) sender).getItemInHand().getType() != Material.AIR) {
                if (args[0].equals("add")) {
                    if (AttackRangeUp.particleName.containsKey(args[1])) {
                        ItemMeta im = ((Player) sender).getItemInHand().getItemMeta();
                        if (im.hasLore()) {
                            List<String> l = im.getLore();
                            l.add("§a横扫粒子: " + AttackRangeUp.particleName.get(args[1]));
                            im.setLore(l);
                        } else {
                            ArrayList<String> l = new ArrayList<>();
                            l.add("§a横扫粒子: " + AttackRangeUp.particleName.get(args[1]));
                            im.setLore(l);
                        }
                        ((Player) sender).getItemInHand().setItemMeta(im);
                        sender.sendMessage("§a成功添加" + AttackRangeUp.particleName.get(args[1]) + "§a横扫粒子效果");
                    } else {
                        sender.sendMessage("§c没有找到您输入的粒子代号，请到config.yml查看粒子代号");
                    }
                } else if (args[0].equals("remove")) {
                    if (AttackRangeUp.particleName.containsKey(args[1])) {
                        ItemMeta im = ((Player) sender).getItemInHand().getItemMeta();
                        if (im.hasLore()) {
                            List<String> l = im.getLore();
                            boolean found = false;
                            for (String s : l) {
                                if (s.indexOf("§a横扫粒子: ") != 0 || !AttackRangeUp.getPByName(s.split("§a横扫粒子: ")[1]).equals(args[1]))
                                    continue;
                                found = true;
                                l.remove(s);
                                break;
                            }
                            im.setLore(l);
                            ((Player) sender).getItemInHand().setItemMeta(im);
                            if (!found) {
                                sender.sendMessage("§c没有找到名为" + AttackRangeUp.particleName.get(args[1]) + "§c的横扫粒子效果");
                            } else {
                                sender.sendMessage("§a已去除" + AttackRangeUp.particleName.get(args[1]) + "§a横扫粒子效果");
                            }
                        } else {
                            sender.sendMessage("§c没有找到名为" + AttackRangeUp.particleName.get(args[1]) + "§c的横扫粒子效果");
                        }
                        return true;
                    }
                    sender.sendMessage("§c没有找到您输入的粒子代号，请到config.yml查看粒子代号");
                } else {
                    sender.sendMessage("§c正确格式：/sp add/remove [粒子代号]");
                }
            } else if (((Player) sender).getItemInHand().getType() != Material.AIR) {
                sender.sendMessage("§c正确格式：/sp add/remove [粒子代号]");
            } else {
                sender.sendMessage("§c手持物品再使用该指令");
            }
        } else {
            sender.sendMessage("§c你没有sp.change权限");
        }
        return false;
    }
}
