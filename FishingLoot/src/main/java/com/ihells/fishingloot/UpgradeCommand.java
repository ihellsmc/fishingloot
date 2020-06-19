package com.ihells.fishingloot;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class UpgradeCommand implements CommandExecutor {

    public YamlConfiguration mainConfig = FishingLoot.getInstance().getMainConfig().getConfiguration();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            JavaPlugin.getPlugin(FishingLoot.class).getLogger().info("This command cannot be run from console!");
        } else {
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
