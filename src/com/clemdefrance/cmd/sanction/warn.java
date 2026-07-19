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
import com.clemdefrance.Enum.Type;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class warn
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Utilise : /warn <player> <raison>");
            return true;
        }
        Player target = Bukkit.getPlayer((String)args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Joueur introuvable !");
            return true;
        }
        if (target.equals(sender)) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Tu ne peux pas te warn toi-m\u00eame !");
            return true;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        String reason = sb.toString().trim();
        String playerName = target.getName();
        UUID uuid = SanctionDB.getOfflineUUID(playerName);
        SanctionDB.addSanction(uuid, Type.WARN, reason, sender.getName(), -1L);
        sender.sendMessage(ChatColor.GREEN + "Avertissement envoy\u00e9 avec succ\u00e8s !");
        target.sendMessage(ChatColor.RED + "Vous avez re\u00e7u un avertissement de " + ChatColor.YELLOW + sender.getName() + ChatColor.RED + " pour : " + ChatColor.GRAY + reason);
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (!pl.hasPermission("ngutils.log") && !pl.isOp()) continue;
            pl.sendMessage(ChatColor.GREEN + "[*banner id=\"staff\"] Le joueur " + ChatColor.YELLOW + playerName + ChatColor.GREEN + " a re\u00e7u un avertissement pour " + ChatColor.RED + reason + ChatColor.GREEN + " par " + ChatColor.YELLOW + sender.getName());
        }
        return true;
    }
}

