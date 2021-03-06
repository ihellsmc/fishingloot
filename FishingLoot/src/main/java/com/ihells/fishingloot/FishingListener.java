package com.ihells.fishingloot;

import com.ihells.fishingloot.utils.LootUtil;
import com.ihells.fishingloot.utils.RandomCollection;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class FishingListener implements Listener {

    Loot loot = FishingLoot.getInstance().getLoot();
    LootUtil lootUtil = FishingLoot.getInstance().getLootUtil();

    public YamlConfiguration mainConfig = FishingLoot.getInstance().getMainConfig().getConfiguration();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFish(PlayerFishEvent e) {

        // captcha
        int rand = new Random().nextInt(50);
        if (rand == 1) { // 2% chance
            FishingLoot.getInstance().getCaptchaGUI().applyCaptchaGUI(e.getPlayer());
        }

        if (e.getCaught() != null) {
            Item item = (Item) e.getCaught();
            item.setItemStack(getRandomDrop(e.getPlayer()));

            long tokens;
            if (mainConfig.getConfigurationSection("loot.high-value").getKeys(false).contains(item.getItemStack().getType().toString())) {
                tokens = mainConfig.getInt("tokens-on-high-value");
            } else {
                tokens = mainConfig.getInt("tokens-on-low-value");
            }

            if (tokens > 1) {
                e.getPlayer().sendMessage(colour(mainConfig.getString("on-token").replace("{tokens}", Long.toString(tokens))
                        .replace("Token", "Tokens")));
            } else {
                e.getPlayer().sendMessage(colour(mainConfig.getString("on-token").replace("{tokens}", Long.toString(tokens))));
            }

            FishingLoot.getInstance().getTokenManager().addTokens(e.getPlayer(), tokens);

        }

    }

    private ItemStack getRandomDrop(Player player) {

        RandomCollection<ItemStack> randomLoot = new RandomCollection<>();

        for (Map.Entry<ItemStack, Double> entrySet : loot.getLoot().entrySet()) {

            if (lootUtil.isHighValue(entrySet.getKey())) {
                if (getLoot(player.getItemInHand()) == 1) {
                    randomLoot.add(entrySet.getValue() * 1.15, entrySet.getKey());
                } else if (getLoot(player.getItemInHand()) == 2) {
                    randomLoot.add(entrySet.getValue() * 1.3, entrySet.getKey());
                } else if (getLoot(player.getItemInHand()) == 3) {
                    randomLoot.add(entrySet.getValue() * 1.45, entrySet.getKey());
                } else {
                    randomLoot.add(entrySet.getValue(), entrySet.getKey());
                }
            } else {
                randomLoot.add(entrySet.getValue(), entrySet.getKey());
            }

        }

        return randomLoot.next();
    }

    private int getLoot(ItemStack item) {
        try {
            String enchant = null;
            List<String> lore = item.getItemMeta().getLore();
            for (String line : lore) {
                if (line.contains(colour("&bLoot"))) {
                    enchant = line;
                }
            }

            String level = enchant.split(" ")[1];
            if (level.equals(colour("&bI"))) {
                return 1;
            } else if (level.equals(colour("&bII"))) {
                return 2;
            } else {
                return 3;
            }
        } catch (NullPointerException e) {
            return 0;
        }
    }

    private String colour(String arg) {
        return ChatColor.translateAlternateColorCodes('&', arg);
    }

}
