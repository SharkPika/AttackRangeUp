package cn.sky.attackrangeup;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class AttackRangeUp extends JavaPlugin implements Listener {
    public static HashMap<String, String> particleName = new HashMap<>();
    public static List<Player> cdlist = new ArrayList<>();
    public static AttackRangeUp instance;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.onLoad();
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("attrange").setExecutor(new AttackRangeCommand());
        this.getCommand("sp").setExecutor(new SpCommand());
        Bukkit.getScheduler().runTaskLater(this, () -> {
            CC.send("&a插件加载成功");
            CC.send("&e作者&7: &5pi_ka");
        }, 21L);
    }

    @Override
    public void onDisable() {
        CC.send("&c插件卸载成功");
        CC.send("&e作者&7: &5pi_ka");
    }

    public static void SweepAttack(Player p, Double range, Double damage) {
        Location ploc = p.getLocation();
        block0:
        for (Entity e : p.getNearbyEntities(range, range, range)) {
            if (!(e instanceof LivingEntity) || !inSight(p, e)) continue;
            LivingEntity le = (LivingEntity) e;
            Location loc = ploc.clone();
            loc.setYaw(loc.getYaw() - 90.0f);
            double maxYaw = loc.getYaw() + 180.0f;
            float i = loc.getYaw();
            while ((double) i < maxYaw) {
                loc.setYaw(i);
                if ((double) loc.getDirection().angle(le.getLocation().clone().subtract(ploc).toVector()) < (le.getEyeLocation().getY() - le.getLocation().getY()) / 5.0) {
                    le.damage(damage, p);
                    continue block0;
                }
                i += 1.0f;
            }
        }
    }

    public static boolean inSight(Entity e1, Entity e2) {
        return e2.getLocation().clone().subtract(e1.getLocation()).toVector().angle(e1.getLocation().getDirection()) < 1.0f;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static String getPByName(String s) {
        return particleName.keySet().stream().filter(key -> particleName.get(key).equals(s)).findFirst().orElse("noFound");
    }

    public void onLoad() {
        particleName.put("CLOUD", "§b云");
        particleName.put("COLOURED_DUST", "§6彩§a云");
        particleName.put("CRIT", "§e暴击");
        particleName.put("ENDER_SIGNAL", "§5末影珍珠破碎");
        particleName.put("EXPLOSION", "§7微小爆炸");
        particleName.put("EXPLOSION_HUGE", "§6大爆炸");
        particleName.put("EXPLOSION_LARGE", "§c爆炸");
        particleName.put("FIREWORKS_SPARK", "§c烟花");
        particleName.put("FLAME", "§c火焰");
        particleName.put("FLYING_GLYPH", "§r象形文字");
        particleName.put("FOOTSTEP", "§a足迹");
        particleName.put("HAPPY_VILLAGER", "§a开心村民");
        particleName.put("HEART", "§c爱心");
        particleName.put("INSTANT_SPELL", "§r箭矢尾迹");
        particleName.put("LARGE_SMOKE", "§7");
        particleName.put("LAVA_POP", "§c岩浆飞溅");
        particleName.put("LAVADRIP", "§c岩浆滴落");
        particleName.put("MAGIC_CRIT", "§5膜法暴击");
        particleName.put("MOBSPAWNER_FLAMES", "§c刷怪火焰");
        particleName.put("NOTE", "§b音符");
        particleName.put("PARTICLE_SMOKE", "§7小烟雾颗粒");
        particleName.put("PORTAL", "§8传送门");
        particleName.put("POTION_BREAK", "§b水瓶破碎");
        particleName.put("POTION_SWIRL", "§6彩§a色§b泡泡");
        particleName.put("POTION_SWIRL_TRANSPARENT", "§7透明彩色泡泡");
        particleName.put("SLIME", "§a史莱姆");
        particleName.put("SMOKE", "§7大烟雾");
        particleName.put("SNOW_SHOVEL", "§r雪");
        particleName.put("SNOWBALL_BREAK", "§r雪球破碎");
        particleName.put("SPELL", "§6膜力");
        particleName.put("SPLASH", "§3小水滴");
        particleName.put("TILE_BREAK", "§9瓦砖破碎");
        particleName.put("TILE_DUST", "§8瓦砖屑");
        particleName.put("VILLAGER_THUNDERCLOUD", "§b雷云");
        particleName.put("VOID_FOG", "§7虚空迷雾");
        particleName.put("WATERDRIP", "§3水滴滴落");
        particleName.put("WITCH_MAGIC", "§d女巫膜法");
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) && !cdlist.contains(e.getPlayer()) && e.hasItem() && e.getItem().getItemMeta().hasLore()) {
            List<String> ll = e.getItem().getItemMeta().getLore();
            for (String s : ll) {
                if (s.indexOf("§a范围: §6") != 0) continue;
                double Range = Double.parseDouble(s.split("§a范围: §6")[1].split(" §c威力: §6")[0]);
                boolean found = false;
                for (String s1 : ll) {
                    if (s1.indexOf("§a横扫粒子: ") != 0) continue;
                    found = true;
                    this.particleCreate(e.getPlayer().getLocation(), Range, Particle.valueOf(getPByName(s1.split("§a横扫粒子: ")[1])));
                }
                String particle = this.getConfig().getString("DefaultParticle");
                if (!found && !particle.equals("NULL")) {
                    this.particleCreate(e.getPlayer().getLocation(), Range, Particle.valueOf(particle));
                }
                SweepAttack(e.getPlayer(), Range, Double.valueOf(s.split(" §c威力: §6")[1].split(" §b冷却: §6")[0]));
                cdlist.add(e.getPlayer());
                BukkitRunnable task = new BukkitRunnable() {
                    public void run() {
                        cdlist.remove(e.getPlayer());
                    }
                };
                long cd = (long) (Double.parseDouble(s.split(" §b冷却: §6")[1]) * 20.0);
                task.runTaskLater(this, cd);
                break;
            }
        }
    }

    public void particleCreate(Location Loc, double Radii, Particle Type) {
        double i = 1.5;
        while (i <= Radii) {
            double o = 90.0;
            while (o <= 270.0) {
                double x = i * (Math.cos(o / 180.0 * Math.PI) * (-Math.cos((double) ((90.0f - Loc.clone().getPitch() + 90.0f) / 180.0f) * Math.PI) * Math.sin((double) (Loc.clone().getYaw() / 180.0f) * Math.PI))) + i * (Math.sin(o / 180.0 * Math.PI) * -Math.sin((double) ((Loc.clone().getYaw() - 90.0f) / 180.0f) * Math.PI));
                double y = 0.8 + i * (Math.cos(o / 180.0 * Math.PI) * Math.sin((double) ((90.0f - Loc.clone().getPitch() + 90.0f) / 180.0f) * Math.PI));
                double z = i * (Math.cos(o / 180.0 * Math.PI) * (Math.cos((double) ((90.0f - Loc.clone().getPitch() + 90.0f) / 180.0f) * Math.PI) * Math.cos((double) (Loc.clone().getYaw() / 180.0f) * Math.PI)) + Math.sin(o / 180.0 * Math.PI) * Math.cos((double) ((Loc.clone().getYaw() - 90.0f) / 180.0f) * Math.PI));
                Location Location2 = Loc.clone().add(x, y, z);
                Loc.clone().getWorld().spawnParticle(Type, Location2, 0);
                o += 1.0;
            }
            i += 2.0;
        }
    }
}
