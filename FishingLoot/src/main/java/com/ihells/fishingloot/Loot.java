package com.ihells.fishingloot;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Loot {

    private final YamlConfiguration mainConfig = FishingLoot.getInstance().getMainConfig().getConfiguration();

    public HashMap<ItemStack, Double> getLoot() {

        HashMap<ItemStack, Double> toReturn = new HashMap<>();

        List<String> highValue = new ArrayList<>(mainConfig.getConfigurationSection("loot.high-value").getKeys(false));
        List<String> lowValue = new ArrayList<>(mainConfig.getConfigurationSection("loot.low-value").getKeys(false));

        for (String item : highValue) {
            ItemStack loot;
            if (Material.getMaterial(item.toUpperCase()) != null) {
                loot = new ItemStack(Material.getMaterial(item.toUpperCase()));
            } else {
                loot = new ItemStack(Material.SPONGE);
            }
            double chance = Double.parseDouble(mainConfig.getString("loot.high-value."+item));
            toReturn.put(loot, chance);
        }
        for (String item : lowValue) {
            ItemStack loot;
            if (Material.getMaterial(item.toUpperCase()) != null) {
                loot = new ItemStack(Material.getMaterial(item.toUpperCase()));
            } else {
                loot = new ItemStack(Material.SPONGE);
            }
            double chance = Double.parseDouble(mainConfig.getString("loot.low-value."+item));
            toReturn.put(loot, chance);
        }

        return toReturn;

    }
}
