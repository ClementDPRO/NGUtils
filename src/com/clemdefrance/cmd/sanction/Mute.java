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
import com.clemdefrance.sanction.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Mute
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        long seconds;
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Utilise /mute <player> <time> <reason>");
            return true;
        }
        Player target = Bukkit.getPlayer((String)args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Joueur introuvable !");
            return true;
        }
        if (SanctionDB.isMuted(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Le joueur est d\u00e9j\u00e0 mute!");
            return true;
        }
        if (target.equals(sender)) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Tu ne peux pas te mute toi-m\u00eame !");
            return true;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        String reason = sb.toString().trim();
        String input = args[1];
        String numberPart = input.replaceAll("[^0-9]", "");
        String unitPart = input.replaceAll("[0-9]", "");
        if (numberPart.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Dur\u00e9e invalide !");
            return true;
        }
        int value = Integer.parseInt(numberPart);
        switch (unitPart) {
            case "s": {
                seconds = value;
                break;
            }
            case "m": {
                seconds = (long)value * 60L;
                break;
            }
            case "h": {
                seconds = (long)value * 3600L;
                break;
            }
            case "d": {
                seconds = (long)value * 86400L;
                break;
            }
            case "mo": {
                seconds = (long)value * 2592000L;
                break;
            }
            case "y": {
                seconds = (long)value * 31536000L;
                break;
            }
            default: {
                sender.sendMessage(ChatColor.RED + "Unit\u00e9 inconnue ! Utilise s, m, h, d, mo, y");
                return true;
            }
        }
        SanctionDB.addSanction(target.getUniqueId(), Type.MUTE, reason, sender.getName(), seconds);
        sender.sendMessage(ChatColor.GREEN + "Joueur mute avec succ\u00e8s !");
        target.sendMessage(ChatColor.RED + "Vous avez \u00e9t\u00e9 mute pour " + args[1] + " : " + reason);
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (!pl.hasPermission("ngutils.log") && !pl.isOp()) continue;
            pl.sendMessage(ChatColor.GREEN + "[*banner id=\"staff\"]   Le joueur " + target.getName() + "a \u00e9t\u00e9 rendu muet par " + sender.getName() + " pendant " + args[1] + " pour " + reason);
        }
        return true;
    }
}

