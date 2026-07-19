/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.clemdefrance.cmd.Event;

import com.clemdefrance.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteEvent
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "please use this command without log!");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "please use /deletevent <name>");
            return true;
        }
        String name = args[0];
        if (Main.getEventdb().getEvent(name) == null) {
            sender.sendMessage(ChatColor.RED + "Erreur: le nom de l'event est incorrect ou inexistant.");
            return true;
        }
        Main.getEventdb().deleteEvent(name);
        return true;
    }
}

