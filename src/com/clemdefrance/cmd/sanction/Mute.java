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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Mute
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        UUID targetUUID;
        if (!sender.hasPermission("ngutils.mute")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /mute <pseudo> <temps> <raison>");
            sender.sendMessage(ChatColor.GRAY + "Exemple de temps: 30m (minutes), 2h (heures), 1d (jours), 0 (permanent)");
            return true;
        }
        String targetName = args[0];
        String timeStr = args[1];
        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 2; i < args.length; ++i) {
            reasonBuilder.append(args[i]).append(" ");
        }
        String reason = reasonBuilder.toString().trim();
        Player targetPlayer = Bukkit.getPlayer((String)targetName);
        if (targetPlayer != null) {
            targetUUID = targetPlayer.getUniqueId();
            targetName = targetPlayer.getName();
        } else {
            targetUUID = SanctionDB.getOfflineUUID(targetName);
        }
        if (targetUUID == null) {
            sender.sendMessage(ChatColor.RED + "Impossible de trouver l'UUID de ce joueur.");
            return true;
        }
        long durationSeconds = this.parseDuration(timeStr);
        if (durationSeconds == -2L) {
            sender.sendMessage(ChatColor.RED + "Format de temps invalide ! Utilisez par exemple: 10m, 2h, 5d, ou 0 pour permanent.");
            return true;
        }
        String moderator = sender.getName();
        int sanctionId = SanctionDB.addSanction(targetUUID, Type.MUTE, reason, moderator, durationSeconds);
        if (sanctionId != -1) {
            String timeFormatted = SanctionDB.formatDuration(durationSeconds);
            sender.sendMessage(ChatColor.GREEN + targetName + " a \u00e9t\u00e9 r\u00e9duit au silence pour : " + timeFormatted + " (Raison: " + reason + ")");
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (!pl.hasPermission("ngutils.log") && !pl.isOp()) continue;
                pl.sendMessage(ChatColor.GREEN + "[*banner id=\"staff\"] Le joueur " + ChatColor.YELLOW + targetName + ChatColor.GREEN + " a \u00e9t\u00e9 rendu muet pour " + ChatColor.RED + reason + ChatColor.GREEN + " par " + ChatColor.YELLOW + sender.getName() + ChatColor.GREEN + "pendant " + ChatColor.LIGHT_PURPLE + args[1]);
            }
            if (targetPlayer != null) {
                targetPlayer.sendMessage(ChatColor.RED + "Vous avez \u00e9t\u00e9 rendu muet par " + moderator + " pendant " + timeFormatted + ".");
                targetPlayer.sendMessage(ChatColor.RED + "Raison: " + reason);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Une erreur est survenue lors de l'insertion dans la base de donn\u00e9es.");
        }
        return true;
    }

    private long parseDuration(String timeStr) {
        String unit;
        if (timeStr.equals("0") || timeStr.equalsIgnoreCase("perm")) {
            return 0L;
        }
        Pattern pattern = Pattern.compile("^(\\d+)([mhjdwy])$", 2);
        Matcher matcher = pattern.matcher(timeStr);
        if (!matcher.matches()) {
            return -2L;
        }
        long value = Long.parseLong(matcher.group(1));
        switch (unit = matcher.group(2).toLowerCase()) {
            case "m": {
                return value * 60L;
            }
            case "h": {
                return value * 3600L;
            }
            case "j": 
            case "d": {
                return value * 86400L;
            }
            case "w": {
                return value * 604800L;
            }
            case "mo": {
                return value * 2592000L;
            }
            case "y": {
                return value * 31536000L;
            }
        }
        return -2L;
    }
}

