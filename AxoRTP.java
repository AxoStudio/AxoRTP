package com.axo.axortp;

import org.bukkit.plugin.java.JavaPlugin;

public class AxoRTP extends JavaPlugin {
    private RTPCommand rtpCommand;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        rtpCommand = new RTPCommand(this);
        getCommand("rtp").setExecutor(rtpCommand);
        getCommand("axortp").setExecutor(new AdminCommand(this));
        getServer().getPluginManager().registerEvents(new RTPListener(rtpCommand), this);
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new RTPPlaceholder(this).register();
        }
    }
}