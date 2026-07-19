/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_6_R3.NBTBase
 *  net.minecraft.server.v1_6_R3.NBTCompressedStreamTools
 *  net.minecraft.server.v1_6_R3.NBTTagCompound
 *  net.minecraft.server.v1_6_R3.NBTTagList
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.clemdefrance.cmd;

import com.clemdefrance.Main;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import net.minecraft.server.v1_6_R3.NBTBase;
import net.minecraft.server.v1_6_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldborderManager {
    private static Main plugin = Main.getInstance();

    public static NBTTagCompound createBorderNBT(int posX, int posY, int posZ, int width, int length, float safeRatio) {
        NBTTagCompound root = new NBTTagCompound();
        NBTTagList borders = new NBTTagList();
        NBTTagCompound borderEntry = new NBTTagCompound();
        borderEntry.setString("id", "aff");
        NBTTagCompound data = new NBTTagCompound();
        data.setInt("posX", posX);
        data.setInt("posZ", posZ);
        data.setInt("posY", posY);
        data.setDouble("width", (double)width);
        data.setDouble("length", (double)length);
        data.setDouble("safeRatio", (double)safeRatio);
        data.setDouble("wantedSafeRatio", 1.0);
        data.setInt("heighTop", 256);
        data.setInt("heighDown", 64);
        data.setInt("color", 0xFFFFFF);
        data.setInt("safeZoneColor", 0xFF0000);
        data.setString("cancelActionThrough", "false");
        data.setFloat("damage", 0.0f);
        borderEntry.set("data", (NBTBase)data);
        borders.add((NBTBase)borderEntry);
        root.set("borders", (NBTBase)borders);
        return root;
    }

    public static void sendBorderPacket(Player player, NBTTagCompound nbt) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(3);
            NBTCompressedStreamTools.a((NBTTagCompound)nbt, (DataOutput)dos);
            player.sendPluginMessage((Plugin)plugin, "ngupgrades", baos.toByteArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeBorder(Player player) {
        NBTTagCompound root = new NBTTagCompound();
        root.set("borders", (NBTBase)new NBTTagList());
        WorldborderManager.sendBorderPacket(player, root);
    }
}

