package com.ihells.fishingloot;

import com.ihells.fishingloot.guis.CaptchaGUI;
import com.ihells.fishingloot.guis.UpgradeGUI;
import com.ihells.fishingloot.utils.ItemBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class InventoryListener implements Listener {

    public YamlConfiguration mainConfig = FishingLoot.getInstance().getMainConfig().getConfiguration();
    private final CaptchaGUI captchaGUI = FishingLoot.getInstance().getCaptchaGUI();
    private final UpgradeGUI upgradeGUI = FishingLoot.getInstance().getUpgradeGUI();

    private ItemStack alreadyPurchased; private ItemStack tierOne; private ItemStack tierTwo; private ItemStack tierThree;

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        try {
            if (e.getCurrentItem().getType().equals(Material.FISHING_ROD)) {
                if (!e.getCurrentItem().hasItemMeta()) {
                    ItemMeta meta = e.getCurrentItem().getItemMeta();
                    meta.setDisplayName(colour(mainConfig.getString("rod-name").replace("{tier}", "0")));
                    e.getCurrentItem().setItemMeta(meta);
                }
            }
        } catch (NullPointerException ignored) {}

        if (e.getView().getTitle().equals(colour("&a&lClick the green glass!"))) {
            e.setCancelled(true);
            ItemStack green = new ItemBuilder(Material.STAINED_GLASS_PANE).name("&f").chatColor(ChatColor.GREEN).build();
            if (e.getCurrentItem().equals(green)) {
                player.closeInventory();
            } else {
                captchaGUI.applyCaptchaGUI(player);
            }
        }

        if (e.getView().getTitle().equals(colour(mainConfig.getString("gui.title")))) {

            ItemStack alreadyPurchased = new ItemBuilder(Material.STAINED_GLASS_PANE).chatColor(ChatColor.GREEN)
                    .name("&a&lYou have already purchased this upgrade!").build();
            ItemStack buyPrevious = new ItemBuilder(Material.STAINED_GLASS_PANE).chatColor(ChatColor.RED)
                    .name("&c&lYou must buy the previous tier first!").build();
            buildItems(player.getItemInHand());

            if (e.getCurrentItem().getType().equals(Material.ENCHANTED_BOOK)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(colour(mainConfig.getString("gui.upgrades.tier-1.name")))) {

                    int cost = mainConfig.getInt("gui.upgrades.tier-1.cost");
                    Economy economy = FishingLoot.getInstance().getEconomy();
                    double bal = economy.getBalance(player);

                    if (bal >= cost) {
                        FishingLoot.getInstance().getUpgradeUtil().upgradeFishingRod(player, 1);
                        economy.withdrawPlayer(player, cost);
                        FishingLoot.getInstance().getUpgradeGUI().applyUpgradeGUI(player);
                    } else {
                        player.sendMessage(colour(mainConfig.getString("no-funds")));
                        player.closeInventory();
                    }

                    e.setCancelled(true);

                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(colour(mainConfig.getString("gui.upgrades.tier-2.name")))) {
                    if (getTier(player.getItemInHand().getItemMeta().getDisplayName()) == 0) {
                        ItemStack temp = e.getCurrentItem();
                        e.setCurrentItem(buyPrevious);
                        e.setCancelled(true);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(FishingLoot.getInstance(), () -> {
                            e.setCancelled(false);
                            e.setCurrentItem(getUpgradeItem(temp));
                            e.setCancelled(true);
                        }, 3*20L);
                    } else {

                        int cost = mainConfig.getInt("gui.upgrades.tier-2.cost");
                        Economy economy = FishingLoot.getInstance().getEconomy();
                        double bal = economy.getBalance(player);

                        if (bal >= cost) {
                            FishingLoot.getInstance().getUpgradeUtil().upgradeFishingRod(player, 2);
                            economy.withdrawPlayer(player, cost);
                            FishingLoot.getInstance().getUpgradeGUI().applyUpgradeGUI(player);
                        } else {
                            player.sendMessage(colour(mainConfig.getString("no-funds")));
                            player.closeInventory();
                        }

                        e.setCancelled(true);
                    }
                } else {
                    if (getTier(player.getItemInHand().getItemMeta().getDisplayName()) == 0) {
                        ItemStack temp = e.getCurrentItem();
                        e.setCurrentItem(buyPrevious);
                        e.setCancelled(true);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(FishingLoot.getInstance(), () -> {
                            e.setCancelled(false);
                            e.setCurrentItem(getUpgradeItem(temp));
                            e.setCancelled(true);
                        }, 3*20L);
                    } else if (getTier(player.getItemInHand().getItemMeta().getDisplayName()) == 1) {
                        ItemStack temp = e.getCurrentItem();
                        e.setCurrentItem(buyPrevious);
                        e.setCancelled(true);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(FishingLoot.getInstance(), () -> {
                            e.setCancelled(false);
                            e.setCurrentItem(getUpgradeItem(temp));
                            e.setCancelled(true);
                        }, 3*20L);
                    } else {

                        int cost = mainConfig.getInt("gui.upgrades.tier-3.cost");
                        Economy economy = FishingLoot.getInstance().getEconomy();
                        double bal = economy.getBalance(player);

                        if (bal >= cost) {
                            FishingLoot.getInstance().getUpgradeUtil().upgradeFishingRod(player, 3);
                            economy.withdrawPlayer(player, cost);
                            FishingLoot.getInstance().getUpgradeGUI().applyUpgradeGUI(player);
                        } else {
                            player.sendMessage(colour(mainConfig.getString("no-funds")));
                            player.closeInventory();
                        }

                        e.setCancelled(true);

                    }
                }
            } else {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(colour("&f"))) {
                    e.setCancelled(true);
                } else {
                    if (isAnUpgrade(e.getCurrentItem())) {
                        ItemStack temp = e.getCurrentItem();
                        e.setCurrentItem(alreadyPurchased);
                        e.setCancelled(true);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(JavaPlugin.getPlugin(FishingLoot.class), () -> {
                            e.setCancelled(false);
                            e.setCurrentItem(getUpgradeItem(temp));
                            e.setCancelled(true);
                        }, 3*20L);
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }

    }

    private boolean isAnUpgrade(ItemStack item) {
        String displayName = item.getItemMeta().getDisplayName();
        String upgradeOne = colour(mainConfig.getString("gui.upgrades.tier-1.name"));
        String upgradeTwo = colour(mainConfig.getString("gui.upgrades.tier-2.name"));
        String upgradeThree = colour(mainConfig.getString("gui.upgrades.tier-3.name"));
        return displayName.equals(upgradeOne) || displayName.equals(upgradeTwo) || displayName.equals(upgradeThree);
    }

    private ItemStack getUpgradeItem(ItemStack item) {
        String displayName = item.getItemMeta().getDisplayName();
        String upgradeOne = colour(mainConfig.getString("gui.upgrades.tier-1.name"));
        String upgradeTwo = colour(mainConfig.getString("gui.upgrades.tier-2.name"));
        String upgradeThree = colour(mainConfig.getString("gui.upgrades.tier-3.name"));
        if (displayName.equals(upgradeOne)) {
            return tierOne;
        } else if (displayName.equals(upgradeTwo)) {
            return tierTwo;
        } else {
            return tierThree;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem().getType().equals(Material.FISHING_ROD)) {
            if (!e.getItem().hasItemMeta()) {
                ItemMeta meta = e.getItem().getItemMeta();
                meta.setDisplayName(colour(mainConfig.getString("rod-name").replace("{tier}", "0")));
                e.getItem().setItemMeta(meta);
            }
        }
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
