package com.ihells.fishingloot;

import com.ihells.fishingloot.guis.CaptchaGUI;
import com.ihells.fishingloot.guis.UpgradeGUI;
import com.ihells.fishingloot.utils.LootUtil;
import com.ihells.fishingloot.utils.UpgradeUtil;
import com.qrakn.phoenix.lang.file.type.BasicConfigurationFile;
import lombok.Getter;
import lombok.Setter;
import me.realized.tokenmanager.api.TokenManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Fish;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FishingLoot extends JavaPlugin {

    @Getter @Setter BasicConfigurationFile mainConfig;
    @Getter Loot loot;
    @Getter CaptchaGUI captchaGUI;
    @Getter UpgradeGUI upgradeGUI;
    @Getter LootUtil lootUtil;
    @Getter UpgradeUtil upgradeUtil;

    @Getter public static FishingLoot instance;
    @Getter public Economy economy = null;
    @Getter public final TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");

    public List<UUID> inCaptcha = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        registerConfigs();
        registerInstances();
        Bukkit.getPluginManager().registerEvents(new FishingListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        getCommand("upgrade").setExecutor(new UpgradeCommand());

        if (!setupEconomy() ) {
            getLogger().severe("NO VAULT ECONOMY FOUND!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled successfully!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private void registerConfigs() {
        mainConfig = new BasicConfigurationFile(this, "config");
    }

    public void reloadConfig() {
        try {
            mainConfig.getConfiguration().load(getMainConfig().getFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void registerInstances() {
        loot = new Loot();
        captchaGUI = new CaptchaGUI();
        upgradeGUI = new UpgradeGUI();
        lootUtil = new LootUtil();
        upgradeUtil = new UpgradeUtil();
    }

    public void saveConfig() {
        try {
            getMainConfig().getConfiguration().save(getMainConfig().getFile());
        } catch (IOException ignored) {}
    }

}
