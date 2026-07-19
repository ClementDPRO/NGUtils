package com.clemdefrance.event;

import com.clemdefrance.Main;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.ilexiconn.nationsgui.forge.NationsGUI;
import net.ilexiconn.nationsgui.forge.server.packet.PacketRegistry;
import net.ilexiconn.nationsgui.forge.server.packet.impl.StaffListDataPacket;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JoinListener implements Listener {
    private static final Gson GSON = new Gson();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        this.clearPlayerAttributes(p);

        String uuidStr = p.getUniqueId().toString();
        Bukkit.getConsoleSender().sendMessage("UUID: " + uuidStr);
        p.sendMessage("UUID: " + uuidStr);

        if (p.isOp()) {
            for (String badgeId : NationsGUI.BADGES_RESOURCES.keySet()) {
                p.sendMessage("Badge NG : " + badgeId);
            }

            int packetId = PacketRegistry.INSTANCE.packetList.indexOf(StaffListDataPacket.class);
            p.sendMessage("PACKET ID REEL DANS LE MOD = " + packetId);
        }

        setupStaff();
    }

    public void setupStaff() {
        LinkedHashMap<String, ArrayList<String>> staffByGroup = new LinkedHashMap<>();
        ArrayList<String> ops = new ArrayList<>();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.isOp()) {
                ops.add(onlinePlayer.getName());
            }
        }

        staffByGroup.put("Staff En Ligne", ops);
        String json = GSON.toJson(staffByGroup);

        int packetId = PacketRegistry.INSTANCE.packetList.indexOf(StaffListDataPacket.class);

        if (packetId == -1) {
            Bukkit.getLogger().warning("[Plugin] Le paquet StaffListDataPacket n'est pas enregistré dans le mod !");
            return;
        }

        for (Player recipient : Bukkit.getOnlinePlayers()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeByte((byte) packetId);

            out.writeUTF(json);
            recipient.sendPluginMessage(Main.getInstance(), "nationsgui", out.toByteArray());
        }
    }

    public void clearPlayerAttributes(Player player) {
    }
}