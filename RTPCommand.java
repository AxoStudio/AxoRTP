package com.axo.axortp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RTPCommand implements CommandExecutor {
    private final AxoRTP plugin;
    private final HashMap<UUID, Long> cooldowns1 = new HashMap<>();
    private final HashMap<UUID, Long> cooldowns2 = new HashMap<>();
    private final HashMap<UUID, Long> cooldowns3 = new HashMap<>();
    private final Random random = new Random();

    public RTPCommand(AxoRTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }
        Player player = (Player) sender;
        openRTPGui(player);
        return true;
    }

    private void openRTPGui(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE + "Random Teleport");
        ItemStack rtp1 = createRtpItem("rtp1", player);
        ItemStack rtp2 = createRtpItem("rtp2", player);
        ItemStack rtp3 = createRtpItem("rtp3", player);
        gui.setItem(11, rtp1);
        gui.setItem(13, rtp2);
        gui.setItem(15, rtp3);
        player.openInventory(gui);
    }

    private ItemStack createRtpItem(String rtpType, Player player) {
        String materialName = plugin.getConfig().getString(rtpType + ".material");
        String name = plugin.getConfig().getString(rtpType + ".name");
        List<String> lore = plugin.getConfig().getStringList(rtpType + ".lore");
        Material material = Material.getMaterial(materialName);
        ItemStack item = new ItemStack(material != null ? material : Material.GRASS_BLOCK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            List<String> coloredLore = new ArrayList<>();
            for (String line : lore) {
                coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            if (!canUseRTP(player, rtpType)) {
                coloredLore.add(ChatColor.RED + "Pozostało " + getRemainingCooldown(player, rtpType) + " sekund");
            }
            meta.setLore(coloredLore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public void handleInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.DARK_PURPLE + "Random Teleport")) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        String rtpType;
        if (slot == 11) rtpType = "rtp1";
        else if (slot == 13) rtpType = "rtp2";
        else if (slot == 15) rtpType = "rtp3";
        else return;
        if (!canUseRTP(player, rtpType)) {
            String cooldownMessage = plugin.getConfig().getString("messages.cooldown_message");
            String remainingTime = String.valueOf(getRemainingCooldown(player, rtpType));
            String finalMessage = cooldownMessage.replace("%axortp_cooldown%", remainingTime);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', finalMessage));
            player.closeInventory();
            return;
        }
        if (rtpType.equals("rtp2") && !player.hasPermission("axortp.rtp2")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no_permission")));
            player.closeInventory();
            return;
        }
        if (rtpType.equals("rtp3") && !player.hasPermission("axortp.rtp3")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no_permission")));
            player.closeInventory();
            return;
        }
        player.closeInventory();
        startTeleport(player, rtpType);
    }

    private boolean canUseRTP(Player player, String rtpType) {
        HashMap<UUID, Long> cooldowns = getCooldownMap(rtpType);
        long cooldown = plugin.getConfig().getLong(rtpType + ".cooldown") * 1000;
        UUID uuid = player.getUniqueId();
        if (cooldowns.containsKey(uuid)) {
            long lastUsed = cooldowns.get(uuid);
            return System.currentTimeMillis() - lastUsed >= cooldown;
        }
        return true;
    }

    public long getRemainingCooldown(Player player, String rtpType) {
        HashMap<UUID, Long> cooldowns = getCooldownMap(rtpType);
        long cooldown = plugin.getConfig().getLong(rtpType + ".cooldown") * 1000;
        UUID uuid = player.getUniqueId();
        if (cooldowns.containsKey(uuid)) {
            long lastUsed = cooldowns.get(uuid);
            long remaining = cooldown - (System.currentTimeMillis() - lastUsed);
            return remaining > 0 ? remaining / 1000 : 0;
        }
        return 0;
    }

    private HashMap<UUID, Long> getCooldownMap(String rtpType) {
        switch (rtpType) {
            case "rtp1": return cooldowns1;
            case "rtp2": return cooldowns2;
            case "rtp3": return cooldowns3;
            default: return cooldowns1;
        }
    }

    private void startTeleport(Player player, String rtpType) {
        int distance = plugin.getConfig().getInt(rtpType + ".distance");
        int countdown = plugin.getConfig().getInt("countdown", 5);
        Location destination = findSafeLocation(player, distance);
        if (destination == null) {
            player.sendMessage(ChatColor.RED + "Nie znaleziono bezpiecznego miejsca!");
            return;
        }
        new BukkitRunnable() {
            int timeLeft = countdown;
            @Override
            public void run() {
                if (timeLeft <= 0) {
                    player.teleport(destination);
                    player.sendTitle(
                            ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.success_title")),
                            ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.success_subtitle")),
                            10, 70, 20
                    );
                    getCooldownMap(rtpType).put(player.getUniqueId(), System.currentTimeMillis());
                    cancel();
                    return;
                }
                String secondsText = timeLeft == 1 ? "sekundę" : timeLeft <= 4 ? "sekundy" : "sekund";
                player.sendTitle(
                        ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.teleporting_title")),
                        ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.teleporting_subtitle").replace("%seconds%", String.valueOf(timeLeft) + " " + secondsText)),
                        0, 25, 5
                );
                timeLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private Location findSafeLocation(Player player, int distance) {
        List<String> safeBlocks = plugin.getConfig().getStringList("safe_blocks");
        List<String> unsafeBlocks = plugin.getConfig().getStringList("unsafe_blocks");
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(distance * 2) - distance;
            int z = random.nextInt(distance * 2) - distance;
            Location loc = new Location(player.getWorld(), x, 0, z);
            loc.setY(player.getWorld().getHighestBlockYAt(loc));
            Material block = loc.getBlock().getType();
            Material below = loc.clone().subtract(0, 1, 0).getBlock().getType();
            Material above = loc.clone().add(0, 1, 0).getBlock().getType();
            if (safeBlocks.contains(below.toString()) && !unsafeBlocks.contains(block.toString()) && above == Material.AIR) {
                loc.add(0.5, 1, 0.5);
                return loc;
            }
        }
        return null;
    }
}