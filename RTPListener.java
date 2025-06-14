package com.axo.axortp;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RTPListener implements Listener {
    private final RTPCommand rtpCommand;

    public RTPListener(RTPCommand rtpCommand) {
        this.rtpCommand = rtpCommand;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        rtpCommand.handleInventoryClick(event);
    }
}