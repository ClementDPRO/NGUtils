/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 */
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class onMentionChat
implements Listener {
    LinkedHashMap<String, String> gradecolor = new LinkedHashMap<>();

    public onMentionChat() {
        gradecolor.put("Joueur", "§7");
        gradecolor.put("Fondateur", "§3");
        gradecolor.put("Cofondateur", "§b");
        gradecolor.put("premium", "§e");
        gradecolor.put("Dev", "§d");
        gradecolor.put("Moderateur_plus", "§a");
        gradecolor.put("legende", "§9");
        gradecolor.put("heros", "§8");
        gradecolor.put("Moderateur", "§a");
        gradecolor.put("SuperModo", "§c");
        gradecolor.put("Moderateur_Test", "§a");
        gradecolor.put("Admin", "§c");
        gradecolor.put("builder", "§2");
        gradecolor.put("RP", "§7");
    }




    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String color = gradecolor.get(JoinGradeEvent.getPlayerGroup(event.getPlayer()));
        event.setCancelled(true);
        Player sender = event.getPlayer();
        String originalMessage = event.getMessage();
        if (SanctionDB.isMuted(sender.getUniqueId())) {
            return;
        }
        String displayName = sender.getDisplayName();
        String prefixOnly = displayName.replace(sender.getName(), "").replace("@", "").trim();
        String customSenderName = ChatColor.GRAY + prefixOnly + " " + color + sender.getName();
        for (Player recipient : Bukkit.getOnlinePlayers()) {
            String processedMessage = originalMessage;
            if (originalMessage.toLowerCase().contains(recipient.getName().toLowerCase())) {
                String mention = ChatColor.DARK_RED + "@" + recipient.getName() + ChatColor.GRAY;
                processedMessage = originalMessage.replaceAll("(?i)" + recipient.getName(), mention);
                recipient.playSound(recipient.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
            }
            String finalFormat = customSenderName + ChatColor.GRAY + ChatColor.BOLD + " » " + ChatColor.GRAY + processedMessage;
            recipient.sendMessage(finalFormat);
        }
        Bukkit.getConsoleSender().sendMessage("[Chat] " + sender.getName() + ": " + originalMessage);
    }
}

