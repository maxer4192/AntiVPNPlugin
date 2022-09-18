package org.antivpn.antivpnplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiVPN extends JavaPlugin {

    @Override
    public void onEnable() {
        FileConfiguration config = this.getConfig();
        config.addDefault("message", "&c&lYour IP appears to be coming from a carrier mobile network, VPN, or datacenter. For security reasons, this is not allowed.");
        config.options().copyDefaults(true);
        saveConfig();
        getServer().getPluginManager().registerEvents(new DetectionListener(), this);
        getLogger().info("AntiVPN has been enabled!");

    }

    @Override
    public void onDisable() {
        // Likely will have more added in a future release
        getLogger().info("AntiVPN has been disabled!");


    }

}