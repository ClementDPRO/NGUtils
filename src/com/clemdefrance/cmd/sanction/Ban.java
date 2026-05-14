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

public class Ban implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Utilise /ban <player> <reason>");
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

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String reason = sb.toString().trim();

        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

        SanctionDB.addSanction(uuid, Type.BAN, reason, sender.getName(), 0L);

        Player online = Bukkit.getPlayer(name);
        if (online != null) {
            online.kickPlayer(
                    ChatColor.RED + "Ban permanent !\n"
                            + ChatColor.GRAY + "Raison : " + ChatColor.YELLOW + reason + "\n"
                            + ChatColor.GRAY + "Date : " + ChatColor.AQUA + date + "\n"
                            + ChatColor.BLUE + "Unban : discord.gg/RM36JsXrN6"
            );
        }

        sender.sendMessage(ChatColor.GREEN + "Joueur banni avec succès !");

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.hasPermission("ngutils.log") || pl.isOp()) {
                pl.sendMessage(ChatColor.GREEN + "[*banner id=\"staff\"] Le joueur "
                        + name + " a été définitivement banni par "
                        + sender.getName() + " pour " + reason);
            }
        }

        return true;
    }

}

