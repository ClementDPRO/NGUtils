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

import com.clemdefrance.Main;
import com.clemdefrance.cmd.Event.CloseEvent;
import com.clemdefrance.cmd.Event.EventData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JoinEvent
implements CommandExecutor {
    public static String openEvent = null;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "please use this command without log!");
            return true;
        }
        if (openEvent == null) {
            sender.sendMessage(ChatColor.RED + "Erreur: aucun event n'est ouvert pour le moment.");
            return true;
        }
        EventData data = Main.getEventdb().getEvent(openEvent);
        CloseEvent.playerinevent.add((Player)sender);
        Player player = Bukkit.getPlayer((String)sender.getName());
        if (data.getWorld() == null) {
            sender.sendMessage("Erreur, le monde auquel l'\u00e9vent \u00e0 \u00e9t\u00e9 enregistrer n'existe plus.");
            return true;
        }
        player.teleport(new Location(Bukkit.getWorld((String)data.getWorld()), data.getX(), data.getY(), data.getZ()));
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setItemInHand(null);
        player.updateInventory();
        Bukkit.dispatchCommand((CommandSender)sender, (String)("/title " + sender.getName() + "\u00a74Vous avez rejoins un \u00e9vent! $$$ \u00a7eEvent " + data.getName()));
        return true;
    }
}

