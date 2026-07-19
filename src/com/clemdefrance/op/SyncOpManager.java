/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.clemdefrance.op;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SyncOpManager {
    private static final Set<String> syncedPlayers = new HashSet<String>();

    public static boolean isSyncedOp(Player player) {
        return syncedPlayers.contains(player.getName()) || player.isOp();
    }

    public static void sendOpPacket(Player player, byte level) {
        try {
            Method getHandle = player.getClass().getMethod("getHandle", new Class[0]);
            Object entityPlayer = getHandle.invoke(player, new Object[0]);
            Object connection = null;
            for (Field f : entityPlayer.getClass().getFields()) {
                if (!f.getType().getSimpleName().contains("NetServerHandler")) continue;
                connection = f.get(entityPlayer);
                break;
            }
            if (connection == null) {
                return;
            }
            Object networkManager = null;
            for (Field f : connection.getClass().getDeclaredFields()) {
                if (!f.getType().getSimpleName().contains("NetworkManager") && !f.getType().getSimpleName().contains("INetworkManager")) continue;
                f.setAccessible(true);
                networkManager = f.get(connection);
                break;
            }
            Class<?> packetClass = Class.forName("net.minecraft.network.packet.Packet38EntityStatus");
            Constructor<?> con = packetClass.getConstructor(Integer.TYPE, Byte.TYPE);
            Object packet = con.newInstance(player.getEntityId(), level);
            if (networkManager != null) {
                Method sendMethod = null;
                for (Method m : networkManager.getClass().getMethods()) {
                    if (m.getParameterTypes().length != 1 || !m.getParameterTypes()[0].getName().contains("Packet")) continue;
                    sendMethod = m;
                    break;
                }
                if (sendMethod != null) {
                    sendMethod.invoke(networkManager, packet);
                }
            } else {
                Bukkit.getLogger().warning("[NGUtils] NetworkManager introuvable, tentative d'injection directe...");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setOpStatus(Player player, boolean status) {
        player.setOp(status);
        try {
            Method getHandle = player.getClass().getMethod("getHandle", new Class[0]);
            Object entityPlayer = getHandle.invoke(player, new Object[0]);
            Field serverField = entityPlayer.getClass().getField("field_71133_b");
            Object mcServer = serverField.get(entityPlayer);
            Method getConfigMethod = mcServer.getClass().getMethod("func_71203_ab", new Class[0]);
            Object configManager = getConfigMethod.invoke(mcServer, new Object[0]);
            if (status) {
                Method addOp = configManager.getClass().getMethod("func_72363_e", String.class);
                addOp.invoke(configManager, player.getName());
            } else {
                Method removeOp = configManager.getClass().getMethod("func_72363_f", String.class);
                removeOp.invoke(configManager, player.getName());
            }
        }
        catch (Exception e) {
            if (status) {
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("op " + player.getName()));
            }
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("deop " + player.getName()));
        }
        SyncOpManager.sendOpPacket(player, (byte)(status ? 28 : 24));
    }
}

