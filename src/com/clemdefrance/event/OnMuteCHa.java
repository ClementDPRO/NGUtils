/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 */
package com.clemdefrance.event;

import com.clemdefrance.DB.SanctionDB;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnMuteCHa
implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (SanctionDB.isMuted(p.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "Tu ne peux pas parler, tu es mute !");
            e.setCancelled(true);
        }
    }
}

