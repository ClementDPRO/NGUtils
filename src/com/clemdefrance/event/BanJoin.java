/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 */
package com.clemdefrance.event;

import com.clemdefrance.DB.SanctionDB;
import com.clemdefrance.cmd.sanction.BanInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class BanJoin
implements Listener {
    public static List<String> convoc = new ArrayList<String>();

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        UUID uuid = SanctionDB.getOfflineUUID(player.getName());
        if (SanctionDB.isBanned(uuid)) {
            BanInfo info = SanctionDB.getBanInfo(uuid);
            if (info.remainingSeconds == -1L) {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "Vous \u00eates banni !\n" + ChatColor.GRAY + "Raison : " + ChatColor.YELLOW + info.reason + "\n" + ChatColor.GRAY + "Dur\u00e9e : " + ChatColor.RED + "PERMANENT\n" + ChatColor.BLUE + "Unban : discord.gg/RM36JsXrN6");
                return;
            }
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "Ban temporaire !\n" + ChatColor.GRAY + "Raison : " + ChatColor.YELLOW + info.reason + "\n" + ChatColor.GRAY + "Temps restant : " + ChatColor.AQUA + SanctionDB.formatDuration(info.remainingSeconds) + "\n" + ChatColor.BLUE + "Unban : discord.gg/RM36JsXrN6");
        }
    }
}

