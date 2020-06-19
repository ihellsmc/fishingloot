package com.ihells.fishingloot.guis;

import com.ihells.fishingloot.FishingLoot;
import com.ihells.fishingloot.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UpgradeGUI {

    private ItemStack tierOne; private ItemStack tierTwo; private ItemStack tierThree;

    public YamlConfiguration mainConfig = FishingLoot.getInstance().getMainConfig().getConfiguration();

    public void applyUpgradeGUI(Player player) {

        Inventory gui = Bukkit.createInventory(null, 27, colour(mainConfig.getString("gui.title")));

        buildItems(player.getItemInHand());

        ItemStack other = new ItemBuilder(Material.STAINED_GLASS_PANE).chatColor(ChatColor.DARK_GRAY).name("&f").build();

        gui.setItem(11, tierOne);
        gui.setItem(13, tierTwo);
        gui.setItem(15, tierThree);

        for (int i = 0; i < 27; i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, other);
            }
        }

        player.openInventory(gui);

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

    private int getTier(String displayName) {
        int tier = 0; int index = 0;
        String[] words = displayName.split(" ");
        for (String word : words) {
            if (word.contains("Tier")) {
                tier = Integer.parseInt(words[index+1].substring(0, 1));
            }
            index++;
        }
        return tier;
    }

    private void buildItems(ItemStack rod) {
        if (getTier(rod.getItemMeta().getDisplayName()) == 0) {
            tierOne = new ItemBuilder(Material.ENCHANTED_BOOK).name(mainConfig.getString("gui.upgrades.tier-1.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-1.lore")).build();
            tierTwo = new ItemBuilder(Material.ENCHANTED_BOOK).name(mainConfig.getString("gui.upgrades.tier-2.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-2.lore")).build();
            tierThree = new ItemBuilder(Material.ENCHANTED_BOOK).name(mainConfig.getString("gui.upgrades.tier-3.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-3.lore")).build();
        } else if (getTier(rod.getItemMeta().getDisplayName()) == 1) {
            tierOne = new ItemBuilder(Material.STAINED_GLASS_PANE).name(mainConfig.getString("gui.upgrades.tier-1.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-1.lore")).chatColor(ChatColor.GREEN).build();
            tierTwo = new ItemBuilder(Material.ENCHANTED_BOOK).name(mainConfig.getString("gui.upgrades.tier-2.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-2.lore")).build();
            tierThree = new ItemBuilder(Material.ENCHANTED_BOOK).name(mainConfig.getString("gui.upgrades.tier-3.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-3.lore")).build();
        } else if (getTier(rod.getItemMeta().getDisplayName()) == 2) {
            tierOne = new ItemBuilder(Material.STAINED_GLASS_PANE).name(mainConfig.getString("gui.upgrades.tier-1.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-1.lore")).chatColor(ChatColor.GREEN).build();
            tierTwo = new ItemBuilder(Material.STAINED_GLASS_PANE).name(mainConfig.getString("gui.upgrades.tier-2.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-2.lore")).chatColor(ChatColor.GREEN).build();
            tierThree = new ItemBuilder(Material.ENCHANTED_BOOK).name(mainConfig.getString("gui.upgrades.tier-3.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-3.lore")).build();
        } else {
            tierOne = new ItemBuilder(Material.STAINED_GLASS_PANE).name(mainConfig.getString("gui.upgrades.tier-1.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-1.lore")).chatColor(ChatColor.GREEN).build();
            tierTwo = new ItemBuilder(Material.STAINED_GLASS_PANE).name(mainConfig.getString("gui.upgrades.tier-2.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-2.lore")).chatColor(ChatColor.GREEN).build();
            tierThree = new ItemBuilder(Material.STAINED_GLASS_PANE).name(mainConfig.getString("gui.upgrades.tier-3.name"))
                    .lore(mainConfig.getStringList("gui.upgrades.tier-3.lore")).chatColor(ChatColor.GREEN).build();
        }
    }

}
