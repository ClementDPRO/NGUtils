package com.clemdefrance.event;

import com.clemdefrance.cmd.Spy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onPlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Spy.spyMap.remove(e.getPlayer().getName());
        LogCrea.crea.remove(e.getPlayer());
        JoinGradeEvent.listPlayer.remove(e.getPlayer().getName());
    }
}