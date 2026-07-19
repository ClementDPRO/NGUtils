/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.clemdefrance.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FML
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("ngutils.log") || sender.isOp()) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Please use /fml <message>");
                return true;
            }
            StringBuilder msg = new StringBuilder();
            for (int i = 0; i < args.length; ++i) {
                msg.append(args[i]).append(" ");
            }
            String msgfinal = "[*banner id=\"staff\"] " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "[" + sender.getName() + "] ==> " + ChatColor.RESET + msg.toString().trim();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("ngutils.log") && !player.isOp()) continue;
                player.sendMessage(msgfinal);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have enough permission to execute this command.");
        }
        return true;
    }
}

