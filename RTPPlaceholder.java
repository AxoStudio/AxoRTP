package com.axo.axortp;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class RTPPlaceholder extends PlaceholderExpansion {
    private final AxoRTP plugin;
    private final RTPCommand rtpCommand;

    public RTPPlaceholder(AxoRTP plugin) {
        this.plugin = plugin;
        this.rtpCommand = new RTPCommand(plugin);
    }

    @Override
    public String getIdentifier() {
        return "axortp";
    }

    @Override
    public String getAuthor() {
        return "Axo";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "";
        if (identifier.equals("rtp1_cooldown")) {
            return String.valueOf(rtpCommand.getRemainingCooldown(player, "rtp1"));
        }
        if (identifier.equals("rtp2_cooldown")) {
            return String.valueOf(rtpCommand.getRemainingCooldown(player, "rtp2"));
        }
        if (identifier.equals("rtp3_cooldown")) {
            return String.valueOf(rtpCommand.getRemainingCooldown(player, "rtp3"));
        }
        if (identifier.equals("cooldown")) {
            long minCooldown = Math.min(
                    Math.min(rtpCommand.getRemainingCooldown(player, "rtp1"),
                            rtpCommand.getRemainingCooldown(player, "rtp2")),
                    rtpCommand.getRemainingCooldown(player, "rtp3")
            );
            return String.valueOf(minCooldown);
        }
        return null;
    }
}