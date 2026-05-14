/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Chunk
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.Chest
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.clemdefrance.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListChests
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Commande r\u00e9serv\u00e9e aux joueurs.");
            return true;
        }
        Player p = (Player)sender;
        World world = p.getWorld();
        p.sendMessage(ChatColor.GOLD + "===== Coffres trouv\u00e9s dans le monde : " + world.getName() + " =====");
        int count = 0;
        for (Chunk chunk : world.getLoadedChunks()) {
            for (int x = 0; x < 16; ++x) {
                for (int y = 0; y < world.getMaxHeight(); ++y) {
                    for (int z = 0; z < 16; ++z) {
                        Block block = chunk.getBlock(x, y, z);
                        if (block.getType() != Material.CHEST) continue;
                        Chest chest = (Chest)block.getState();
                        int bx = block.getX();
                        int by = block.getY();
                        int bz = block.getZ();
                        String json = "{\"text\":\"[Coffre] " + bx + " " + by + " " + bz + "\"," + "\"color\":\"yellow\"," + "\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tp " + p.getName() + " " + bx + " " + by + " " + bz + "\"}" + "}";
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("tellraw " + p.getName() + " " + json));
                        ++count;
                    }
                }
            }
        }
        p.sendMessage(ChatColor.GREEN + "Total : " + count + " coffres trouv\u00e9s.");
        return true;
    }
}

