package com.clemdefrance.event;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerPickCount
implements Listener {
    public static int PlayerPick = 0;

    @EventHandler
    public void onjoin(PlayerJoinEvent e) {
        if (Bukkit.getOnlinePlayers().length > PlayerPick) {
            PlayerPick = Bukkit.getOnlinePlayers().length;
        }
    }
}

