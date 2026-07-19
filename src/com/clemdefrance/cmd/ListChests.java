/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Chest
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package com.clemdefrance.cmd;

import com.clemdefrance.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ListChests
implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        final World world;
        Location center;
        final int targetId;
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Utilisation : /listchest <id_item> [rayon_en_blocs]");
            return true;
        }
        try {
            targetId = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "L'ID doit \u00eatre un nombre entier.");
            return true;
        }
        int radius = 200000;
        if (args.length >= 2) {
            try {
                radius = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        if (sender instanceof Player) {
            center = ((Player)sender).getLocation();
            world = center.getWorld();
        } else {
            world = (World)Bukkit.getWorlds().get(0);
            center = world.getSpawnLocation();
        }
        int centerChunkX = center.getBlockX() >> 4;
        int centerChunkZ = center.getBlockZ() >> 4;
        int chunkRadius = radius >> 4;
        sender.sendMessage(ChatColor.YELLOW + "D\u00e9but de la recherche intensive asynchrone (Rayon: " + radius + " blocs)...");
        final int minX = centerChunkX - chunkRadius;
        final int maxX = centerChunkX + chunkRadius;
        final int minZ = centerChunkZ - chunkRadius;
        final int maxZ = centerChunkZ + chunkRadius;
        Bukkit.getScheduler().runTaskTimer((Plugin)Main.getInstance(), new Runnable(){
            private int currentX;
            private int currentZ;
            private int totalFound;
            private int chunksAnalysed;
            {
                this.currentX = minX;
                this.currentZ = minZ;
                this.totalFound = 0;
                this.chunksAnalysed = 0;
            }

            @Override
            public void run() {
                for (int i = 0; i < 40; ++i) {
                    if (this.currentX > maxX) {
                        this.currentX = minX;
                        ++this.currentZ;
                    }
                    if (this.currentZ > maxZ) {
                        sender.sendMessage(ChatColor.GOLD + "Recherche totale termin\u00e9e ! " + this.totalFound + " coffre(s) trouv\u00e9(s).");
                        Bukkit.getScheduler().cancelTasks((Plugin)Main.getInstance());
                        return;
                    }
                    boolean wasLoaded = world.isChunkLoaded(this.currentX, this.currentZ);
                    if (!wasLoaded) {
                        world.loadChunk(this.currentX, this.currentZ, false);
                    }
                    Chunk chunk = world.getChunkAt(this.currentX, this.currentZ);
                    if (this.chunksAnalysed % 500 == 0) {
                        sender.sendMessage(ChatColor.GRAY + "chunk v\u00e9rifi\u00e9: X:" + this.currentX + " Z:" + this.currentZ + " (Total: " + this.chunksAnalysed + ")");
                    }
                    block1: for (BlockState blockState : chunk.getTileEntities()) {
                        if (!(blockState instanceof Chest)) continue;
                        Chest chest = (Chest)blockState;
                        for (ItemStack item : chest.getInventory().getContents()) {
                            if (item == null || item.getTypeId() != targetId) continue;
                            sender.sendMessage(ChatColor.GREEN + "[Trouv\u00e9] " + ChatColor.WHITE + "X: " + chest.getX() + " Y: " + chest.getY() + " Z: " + chest.getZ());
                            ++this.totalFound;
                            continue block1;
                        }
                    }
                    if (!wasLoaded) {
                        world.unloadChunkRequest(this.currentX, this.currentZ);
                    }
                    ++this.chunksAnalysed;
                    ++this.currentX;
                }
            }
        }, 1L, 1L);
        return true;
    }
}

