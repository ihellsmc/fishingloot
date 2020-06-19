package com.ihells.fishingloot.utils;

import com.ihells.fishingloot.FishingLoot;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LootUtil {

    public YamlConfiguration mainConfig = FishingLoot.getInstance().getMainConfig().getConfiguration();

    public boolean isHighValue(ItemStack item) {

        Material material = item.getType();
        List<String> highValue = new ArrayList<>(mainConfig.getConfigurationSection("loot.high-value").getKeys(false));
        List<Material> toCheck = new ArrayList<>();
        for (String string : highValue) {
            if (Material.getMaterial(string.toUpperCase()) != null) {
                toCheck.add(Material.getMaterial(string.toUpperCase()));
            }
        }
        return toCheck.contains(material);

    }

}
