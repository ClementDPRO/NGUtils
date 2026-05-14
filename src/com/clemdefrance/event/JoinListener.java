/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.ilexiconn.nationsgui.forge.NationsGUI
 *  net.ilexiconn.nationsgui.forge.server.packet.PacketRegistry
 *  net.ilexiconn.nationsgui.forge.server.packet.impl.GetGroupAndPrimePacket
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package com.clemdefrance.event;

import net.ilexiconn.nationsgui.forge.NationsGUI;
import net.ilexiconn.nationsgui.forge.server.packet.PacketRegistry;
import net.ilexiconn.nationsgui.forge.server.packet.impl.GetGroupAndPrimePacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener
implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        this.clearPlayerAttributes(p);
        p.sendMessage("ID GET_GROUP_AND_PRIME = " + PacketRegistry.INSTANCE.packetList.indexOf(GetGroupAndPrimePacket.class));
        if (p.isOp()) {
            for (String badgeId : NationsGUI.BADGES_RESOURCES.keySet()) {
                e.getPlayer().sendMessage("Badge NG : " + badgeId);
            }
        }
        Bukkit.getConsoleSender().sendMessage("UUID: " + String.valueOf(e.getPlayer().getUniqueId()));
        p.sendMessage("UUID: " + String.valueOf(e.getPlayer().getUniqueId()));
    }

    public void clearPlayerAttributes(Player player) {
    }
}

