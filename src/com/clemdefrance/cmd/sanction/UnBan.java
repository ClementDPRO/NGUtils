/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.clemdefrance.cmd.sanction;

import com.clemdefrance.DB.SanctionDB;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnBan
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Utilisation : /unban <pseudo>");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer((String)args[0]);
        if (target == null || target.getName() == null) {
            sender.sendMessage(ChatColor.RED + "Joueur introuvable.");
            return true;
        }
        UUID uuid = SanctionDB.getOfflineUUID(target.getName());
        if (!SanctionDB.isBanned(uuid)) {
            sender.sendMessage(ChatColor.RED + "Ce joueur n'est pas banni.");
            return true;
        }
        boolean success = SanctionDB.unban(uuid);
        if (!success) {
            sender.sendMessage(ChatColor.RED + "Erreur : impossible d'unban ce joueur.");
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "Le joueur " + target.getName() + " a \u00e9t\u00e9 unban avec succ\u00e8s !");
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (!pl.hasPermission("ngutils.log") && !pl.isOp()) continue;
            pl.sendMessage(ChatColor.GREEN + "[*banner id=\"staff\"] Le joueur " + target.getName() + " a \u00e9t\u00e9 unban par " + sender.getName());
        }
        if (target.isOnline()) {
            target.getPlayer().kickPlayer(ChatColor.GREEN + "Ton ban vient d'\u00eatre lev\u00e9, reconnecte-toi !");
        }
        return true;
    }
}

