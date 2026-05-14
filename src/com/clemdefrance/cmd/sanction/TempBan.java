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
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TempBan
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Utilise : /tempban <player> <durée> <raison>");
            return true;
        }

        String name = args[0];

        UUID uuid = SanctionDB.getOfflineUUID(name);
        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Impossible de générer l'UUID offline !");
            return true;
        }

        if (SanctionDB.isBanned(uuid)) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Le joueur est déjà banni !");
            return true;
        }

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.getName().equalsIgnoreCase(name)) {
                sender.sendMessage(ChatColor.RED + "[Sanction] Tu ne peux pas te ban toi-même !");
                return true;
            }
        }

        String input = args[1].toLowerCase();
        String numberPart = input.replaceAll("[^0-9]", "");
        String unitPart = input.replaceAll("[0-9]", "");

        if (numberPart.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Durée invalide !");
            return true;
        }

        int value = Integer.parseInt(numberPart);
        long seconds;

        switch (unitPart) {
            case "s": seconds = value; break;
            case "m": seconds = value * 60L; break;
            case "h": seconds = value * 3600L; break;
            case "d": seconds = value * 86400L; break;
            case "mo": seconds = value * 2592000L; break;
            case "y": seconds = value * 31536000L; break;
            default:
                sender.sendMessage(ChatColor.RED + "[Sanction] Unité inconnue ! Utilise : s, m, h, d, mo, y");
                return true;
        }

        if (seconds <= 0) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Durée invalide !");
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String reason = sb.toString().trim();

        SanctionDB.addSanction(uuid, Type.BAN, reason, sender.getName(), seconds);

        Player online = Bukkit.getPlayer(name);
        if (online != null) {
            online.kickPlayer(
                    ChatColor.RED + "Ban temporaire !\n"
                            + ChatColor.GRAY + "Raison : " + ChatColor.YELLOW + reason + "\n"
                            + ChatColor.GRAY + "Durée : " + ChatColor.AQUA + args[1] + "\n"
                            + ChatColor.BLUE + "Unban : discord.gg/RM36JsXrN6"
            );
        }

        sender.sendMessage(ChatColor.GREEN + "Joueur temporairement banni avec succès !");

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.hasPermission("ngutils.log") || pl.isOp()) {
                pl.sendMessage(ChatColor.GREEN + "[*banner id=\"staff\"] Le joueur "
                        + ChatColor.YELLOW + name + ChatColor.GREEN
                        + " a été temporairement banni pendant "
                        + ChatColor.AQUA + args[1] + ChatColor.GREEN
                        + " par " + ChatColor.YELLOW + sender.getName()
                        + ChatColor.GREEN + " pour : " + ChatColor.RED + reason);
            }
        }

        return true;
    }

}

