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
package com.clemdefrance.cmd.sanction;

import com.clemdefrance.DB.SanctionDB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnMute
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Utilisation : /unmute <pseudo>");
            return true;
        }
        Player target = Bukkit.getPlayer((String)args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Joueur introuvable ou hors-ligne.");
            return true;
        }
        if (!SanctionDB.isMuted(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Ce joueur n'est pas mute.");
            return true;
        }
        boolean success = SanctionDB.unmute(target.getUniqueId());
        if (!success) {
            sender.sendMessage(ChatColor.RED + "Erreur : impossible d'unmute ce joueur.");
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "Le joueur a \u00e9t\u00e9 unmute avec succ\u00e8s !");
        target.sendMessage(ChatColor.YELLOW + "Tu peux de nouveau parler !");
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("ngutils.log") && !player.isOp()) continue;
            player.sendMessage(ChatColor.GREEN + "[*banner id=\"staff<\"]   Le joueur " + target.getName() + " a retrouv\u00e9 la parole gr\u00e2ce \u00e0 " + sender.getName());
        }
        return true;
    }
}

