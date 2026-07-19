package com.clemdefrance.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class LogCrea implements Listener {
    public static List<Player> crea = new ArrayList<>();

    @EventHandler
    public void onCreativeItemAction(InventoryCreativeEvent event) {
        Player player = (Player) event.getWhoClicked();

        ItemStack item = event.getCursor();

        if (item == null || item.getTypeId() == 0) {
            item = event.getCurrentItem();
        }

        if (item != null && item.getTypeId() != 0) {
            String playerName = player.getName();
            String itemName = item.getType().toString();
            int amount = item.getAmount();

            Bukkit.getLogger().info("[CreativeLog] " + playerName + " s'est donné " + amount + "x " + itemName);

            for (Player player1 : crea) {
                if (player1 != null && player1.isOnline()) {
                    player1.sendMessage(ChatColor.DARK_PURPLE + "[CreativeLog] " + ChatColor.RED + playerName + ChatColor.GOLD + " s'est donné " + ChatColor.BLUE + amount + "x " + itemName);
                }
            }
        }
    }
}
