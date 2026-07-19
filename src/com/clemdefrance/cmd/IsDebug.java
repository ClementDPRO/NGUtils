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
package com.clemdefrance.cmd;

import com.clemdefrance.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IsDebug
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player && commandSender.isOp()) {
            Main.isDebug = !Main.isDebug;
            boolean bl = Main.isDebug;
            if (Main.isDebug) {
                commandSender.sendMessage(ChatColor.GREEN + "Debug on");
            } else {
                commandSender.sendMessage(ChatColor.RED + "Debug off");
            }
            return true;
        }
        return true;
    }
}

