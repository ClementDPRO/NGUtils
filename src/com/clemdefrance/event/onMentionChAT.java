package com.clemdefrance.event;

import com.clemdefrance.DB.SanctionDB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onMentionChAT implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player sender = event.getPlayer();
        String originalMessage = event.getMessage();

        if(SanctionDB.isMuted(sender.getUniqueId())) {
            return;
        }

        String displayName = sender.getDisplayName();
        String prefixOnly = displayName.replace(sender.getName(), "").replace("@", "").trim();

        String customSenderName = ChatColor.GRAY + prefixOnly + " " + sender.getName();

        for (Player recipient : Bukkit.getOnlinePlayers()) {
            String processedMessage = originalMessage;

            if (originalMessage.toLowerCase().contains(recipient.getName().toLowerCase())) {
                String mention = ChatColor.DARK_RED + "@" + recipient.getName() + ChatColor.GRAY;
                processedMessage = originalMessage.replaceAll("(?i)" + recipient.getName(), mention);

                recipient.playSound(recipient.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
            }

            String finalFormat = customSenderName + ChatColor.GRAY + " >> " + ChatColor.GRAY + processedMessage;

            recipient.sendMessage(finalFormat);
        }

        Bukkit.getConsoleSender().sendMessage("[Chat] " + sender.getName() + ": " + originalMessage);
    }
}