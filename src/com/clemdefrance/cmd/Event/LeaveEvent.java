/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.clemdefrance.cmd.Event;

import com.clemdefrance.cmd.Event.CloseEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LeaveEvent
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "please use this command without log!");
            return true;
        }
        Player player = (Player)sender;
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setItemInHand(null);
        player.updateInventory();
        player.sendMessage(ChatColor.GREEN + "Bon retour au spawn!");
        CloseEvent.playerinevent.remove(player);
        player.teleport(new Location(Bukkit.getWorld((String)"world"), 627.94434, 24.0, -593.57127));
        return true;
    }
}

