package com.ihells.fishingloot;

import com.ihells.fishingloot.utils.ItemStackSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class UpgradeCommand implements CommandExecutor {

    public YamlConfiguration mainConfig = FishingLoot.getInstance().getMainConfig().getConfiguration();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            JavaPlugin.getPlugin(FishingLoot.class).getLogger().info("This command cannot be run from console!");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("upgrade")) {
            Player player = (Player) sender;

            if (player.hasPermission("rod.upgrade")) {

                if (player.getItemInHand().getType().equals(Material.FISHING_ROD)) {
                    JavaPlugin.getPlugin(FishingLoot.class).getUpgradeGUI().applyUpgradeGUI(player);
                } else {
                    player.sendMessage(colour(mainConfig.getString("wrong-item")));
                }

            } else {
                player.sendMessage(colour(mainConfig.getString("no-permission")));
            }


        } else if (args.length == 3) {

            if (args[0].equalsIgnoreCase("add")) {

                try {
                    int chance = Integer.parseInt(args[2]);

                    if (args[1].equalsIgnoreCase("high") || args[1].equalsIgnoreCase("low")) {

                        if (sender.hasPermission("rod.add")) {

                            if (((Player) sender).getItemInHand() != null && (!((Player) sender).getItemInHand().getType().equals(Material.AIR))) {

                                String item = ItemStackSerializer.serialize(((Player) sender).getItemInHand());

                                if (args[1].equalsIgnoreCase("high")) {

                                    mainConfig.set("loot.high-value."+item, chance);
                                    FishingLoot.getInstance().saveConfigs();
                                    FishingLoot.getInstance().reloadConfigs();

                                    sender.sendMessage(colour("&aSuccess! This item has been added to the loot"));

                                } else {

                                    mainConfig.set("loot.low-value."+item, chance);
                                    FishingLoot.getInstance().saveConfigs();
                                    FishingLoot.getInstance().reloadConfigs();

                                    sender.sendMessage(colour("&aSuccess! This item has been added to the loot"));

                                }

                            } else {
                                sender.sendMessage(colour("&cPlease hold a valid item to add!"));
                            }

                        } else {
                            sender.sendMessage(colour(mainConfig.getString("no-permission")));
                        }

                    } else {
                        sender.sendMessage(colour(mainConfig.getString("invalid-command")));
                    }

                } catch (NumberFormatException e) {
                    sender.sendMessage(colour(mainConfig.getString("invalid-command")));
                }

            } else {
                sender.sendMessage(colour(mainConfig.getString("invalid-command")));
            }

        } else {
            sender.sendMessage(colour(mainConfig.getString("invalid-command")));
        }

        return false;
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
