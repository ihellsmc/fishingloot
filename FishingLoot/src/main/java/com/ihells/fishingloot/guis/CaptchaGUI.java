package com.ihells.fishingloot.guis;

import com.ihells.fishingloot.FishingLoot;
import com.ihells.fishingloot.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class CaptchaGUI {

    public YamlConfiguration mainConfig = FishingLoot.getInstance().getMainConfig().getConfiguration();

    public void applyCaptchaGUI(Player player) {

        Inventory gui = Bukkit.createInventory(null, InventoryType.DISPENSER, colour("&a&lClick the green glass!"));

        int correct = new Random().nextInt(9); // 0 - 8

        ItemStack green = new ItemBuilder(Material.STAINED_GLASS_PANE).name("&f").chatColor(ChatColor.GREEN).build();
        ItemStack red = new ItemBuilder(Material.STAINED_GLASS_PANE).name("&f").chatColor(ChatColor.RED).build();

        gui.setItem(correct, green);
        for (int i = 0; i < 9; i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, red);
            }
        }

        player.openInventory(gui);

        HashMap<UUID, BukkitTask> captchaTimer = FishingLoot.getInstance().getCaptchaTimer();
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                    if (target.hasPermission("captcha.alerts")) {
                        target.sendMessage(colour(mainConfig.getString("captcha-alert").replace("{player}", player.getName())));
                    }
                }
            }
        }.runTaskLaterAsynchronously(FishingLoot.getInstance(), 5*60*20L);

        captchaTimer.put(player.getUniqueId(), task);
        FishingLoot.getInstance().setCaptchaTimer(captchaTimer);

    }

    private String colour(String args) {
        return ChatColor.translateAlternateColorCodes('&', args);
    }

    private List<String> colour(List<String> args) {
        List<String> toReturn = new ArrayList<>();
        for (String arg : args) {
            toReturn.add(colour(arg));
        }
        return toReturn;
    }

}
