/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 */
package com.clemdefrance.cmd;

import com.clemdefrance.cmd.Assaut;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopAssaut
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("assaut.stop")) {
            sender.sendMessage(ChatColor.RED + "Tu n'as pas la permission.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Please use /stopassaut <reason>");
            return true;
        }
        Assaut activeAssaut = Assaut.getInstance();
        if (activeAssaut != null) {
            activeAssaut.stopAssaut(args[0], "", "");
            sender.sendMessage(ChatColor.GREEN + "Assaut stopp\u00e9 avec succ\u00e8s.");
        } else {
            sender.sendMessage(ChatColor.RED + "Aucun assaut n'est instanci\u00e9.");
        }
        return true;
    }
}

