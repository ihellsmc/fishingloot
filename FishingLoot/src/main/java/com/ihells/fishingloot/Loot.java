package com.ihells.fishingloot;

import com.ihells.fishingloot.utils.ItemStackSerializer;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Loot {

    private final YamlConfiguration mainConfig = FishingLoot.getInstance().getMainConfig().getConfiguration();

    public HashMap<ItemStack, Integer> getLoot() {

        HashMap<ItemStack, Integer> toReturn = new HashMap<>();

        List<String> highValue = new ArrayList<>(mainConfig.getConfigurationSection("loot.high-value").getKeys(false));
        List<String> lowValue = new ArrayList<>(mainConfig.getConfigurationSection("loot.low-value").getKeys(false));

        for (String item : highValue) {
            int chance = mainConfig.getInt("loot.high-value."+item);
            ItemStack itemStack = ItemStackSerializer.deserialize(item);
            toReturn.put(itemStack, chance);
        }
        for (String item : lowValue) {
            int chance = mainConfig.getInt("loot.low-value."+item);
            ItemStack itemStack = ItemStackSerializer.deserialize(item);
            toReturn.put(itemStack, chance);
        }

        return toReturn;

    }

}
