package com.ihells.fishingloot.utils;

import com.ihells.fishingloot.FishingLoot;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UpgradeUtil {

    public YamlConfiguration mainConfig = FishingLoot.getInstance().getMainConfig().getConfiguration();

    public void upgradeFishingRod(Player player, int level) {

        String path = "gui.upgrades.tier-"+level+".";

        List<String> enchants = new ArrayList<>(mainConfig.getConfigurationSection(path+"enchants").getKeys(false));
        ItemBuilder rodItem = new ItemBuilder(Material.FISHING_ROD);

        for (String enchant : enchants) {
            if (Enchantment.getByName(enchant.toUpperCase()) != null) {
                rodItem.enchantment(Enchantment.getByName(enchant.toUpperCase()), mainConfig.getInt(path+"enchants."+enchant));
            } else if (enchant.toUpperCase().equals("LOOT")) {
                int multi = mainConfig.getInt(path+"enchants."+enchant);
                if (multi == 1) {
                    rodItem.lore(colour("&bLoot I"));
                } else if (multi == 2) {
                    rodItem.lore(colour("&bLoot II"));
                } else if (multi == 3) {
                    rodItem.lore(colour("&bLoot III"));
                }
            }
        }

        rodItem.name(colour(mainConfig.getString("rod-name").replace("{tier}", Integer.toString(level))));

        ItemStack rod = rodItem.durability(player.getItemInHand().getDurability()).build();

        int cost = mainConfig.getInt(path+"cost");

        player.setItemInHand(rod);
        player.sendMessage(colour(mainConfig.getString("on-purchase").replace("{tier}", Integer.toString(level))
                .replace("{cost}", Integer.toString(cost))));

    }

    private String colour(String arg) {
        return ChatColor.translateAlternateColorCodes('&', arg);
    }

}
