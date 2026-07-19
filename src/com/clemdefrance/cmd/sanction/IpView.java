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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IpView
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "USAGE: /ipview <pseudo>");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer((String)args[0]);
        if (target == null || target.getName() == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }
        UUID uuid = SanctionDB.getOfflineUUID(target.getName());
        sender.sendMessage(ChatColor.GOLD + "===== IpView : " + target.getName() + " =====");
        sender.sendMessage(ChatColor.YELLOW + "UUID Offline: " + ChatColor.GREEN + uuid.toString());
        long lastPlayed = target.getLastPlayed();
        if (lastPlayed > 0L) {
            String last = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(lastPlayed));
            sender.sendMessage(ChatColor.YELLOW + "Derni\u00e8re connexion: " + ChatColor.AQUA + last);
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Derni\u00e8re connexion: " + ChatColor.RED + "Jamais");
        }
        long firstPlayed = target.getFirstPlayed();
        if (firstPlayed > 0L) {
            String first = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(firstPlayed));
            sender.sendMessage(ChatColor.YELLOW + "Premi\u00e8re connexion: " + ChatColor.AQUA + first);
        }
        if (target.isOnline()) {
            Player p = target.getPlayer();
            String realIp = p.getAddress().getAddress().getHostAddress();
            String proxyIp = p.getAddress().getHostName();
            int port = p.getAddress().getPort();
            String hostString = p.getAddress().getHostString();
            sender.sendMessage(ChatColor.YELLOW + "IP r\u00e9elle: " + ChatColor.WHITE + realIp);
            sender.sendMessage(ChatColor.YELLOW + "Proxy / Hostname: " + ChatColor.RED + proxyIp);
            sender.sendMessage(ChatColor.YELLOW + "Port: " + ChatColor.WHITE + port);
            sender.sendMessage(ChatColor.YELLOW + "HostString: " + ChatColor.AQUA + hostString);
        } else {
            sender.sendMessage(ChatColor.RED + "Le joueur est hors-ligne \u2192 IP indisponible.");
        }
        sender.sendMessage(ChatColor.GOLD + "==============================");
        return true;
    }
}

