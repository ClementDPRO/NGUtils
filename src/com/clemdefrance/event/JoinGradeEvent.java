package com.clemdefrance.event;

import net.minecraft.server.v1_6_R3.Packet201PlayerInfo;
import net.minecraft.server.v1_6_R3.PlayerConnection;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.LinkedHashMap;

public class JoinGradeEvent implements Listener {
    public static LinkedHashMap<String, String> listPlayer = new LinkedHashMap<>();

    public static void sendPacket(Player target, Player playerToUpdate, String displayName) {
        int ping = ((CraftPlayer) playerToUpdate).getHandle().ping;

        Packet201PlayerInfo packet = new Packet201PlayerInfo(displayName, true, ping);

        PlayerConnection connection = ((CraftPlayer) target).getHandle().playerConnection;
        if (connection != null && !connection.disconnected) {
            connection.sendPacket(packet);
        }
    }

    public static String getPlayerGroup(Player player) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("GroupManager");

        if (plugin != null && plugin.isEnabled()) {
            GroupManager groupManager = (GroupManager) plugin;
            WorldsHolder worldsHolder = groupManager.getWorldsHolder();

            if (worldsHolder != null) {
                return worldsHolder.getWorldData(player).getUser(player.getName()).getGroup().getName();
            }
        }

        return "Default";
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        switch (getPlayerGroup(player)) {
            case "SuperModo":
                listPlayer.put(player.getName(), "[*banner id=\"supermodo\"]");
                break;
            case "Fondateur":
                listPlayer.put(player.getName(), "[*banner id=\"fondateur\"]");
                break;
            case "Cofondateur":
                listPlayer.put(player.getName(), "[*banner id=\"co-fonda\"]");
                break;
            case "premium":
                listPlayer.put(player.getName(), "[*banner id=\"premium\"]");
                break;
            case "Dev":
                listPlayer.put(player.getName(), "[*banner id=\"dev\"]");
                break;
            case "Moderateur":
                listPlayer.put(player.getName(), "[*banner id=\"moderateur\"]");
                break;
            case "Moderateur_Test":
                listPlayer.put(player.getName(), "[*banner id=\"moderateur_test\"]");
                break;
            case "Admin":
                listPlayer.put(player.getName(), "[*banner id=\"admin\"]");
                break;
            case "Moderateur_plus":
                listPlayer.put(player.getName(), "[*banner id=\"moderateur_plus\"]");
                break;
            case "builder":
                listPlayer.put(player.getName(), "[*banner id=\"builder\"]");
                break;
            case "Joueur":
                listPlayer.put(player.getName(), "[*banner id=\"Joueur\"]");
                break;

        }
    }
}
