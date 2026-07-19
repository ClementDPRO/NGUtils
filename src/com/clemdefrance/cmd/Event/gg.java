/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 */
package com.clemdefrance.cmd.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class gg
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Please execute /gg <player win> <winnable>");
            return true;
        }
        StringBuilder builder = new StringBuilder();
        for (String string : args) {
            if (string == args[0]) continue;
            builder.append(string + " ");
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("[*banner id=\"event\"]  " + ChatColor.LIGHT_PURPLE + "un grands GG \u00e0 " + ChatColor.GOLD + args[0] + ChatColor.LIGHT_PURPLE + " qui \u00e0 gagn\u00e9 l'event d'aujourd'hui et donc qui remporte " + ChatColor.BLUE + builder.toString());
        }
        return true;
    }
}

