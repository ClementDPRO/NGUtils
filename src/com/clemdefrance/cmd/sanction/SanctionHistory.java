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
import com.clemdefrance.sanction.Sanction;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SanctionHistory
implements CommandExecutor {
    public static List<Sanction> getPage(List<Sanction> list, int page, int pageSize) {
        int from = (page - 1) * pageSize;
        int to = Math.min(from + pageSize, list.size());
        if (from >= list.size() || from < 0) {
            return new ArrayList<Sanction>();
        }
        return list.subList(from, to);
    }

    public static void showSanctions(Player viewer, List<Sanction> sanctions, int page) {
        int pageSize = 10;
        int maxPage = Math.max(1, (int)Math.ceil((double)sanctions.size() / (double)pageSize));
        if (page < 1) {
            page = 1;
        }
        if (page > maxPage) {
            page = maxPage;
        }
        viewer.sendMessage(ChatColor.GOLD + "===== Sanctions (Page " + page + "/" + maxPage + ") =====");
        for (Sanction s : SanctionHistory.getPage(sanctions, page, pageSize)) {
            viewer.sendMessage(ChatColor.YELLOW + String.valueOf(s.getId()) + ChatColor.GRAY + " | " + ChatColor.RED + (Object)((Object)s.getType()) + ChatColor.GRAY + " | " + ChatColor.WHITE + s.getReason() + ChatColor.GRAY + " | " + ChatColor.AQUA + SanctionDB.formatDuration(s.getTime()) + ChatColor.GRAY + " | " + ChatColor.GREEN + s.getDate() + ChatColor.GRAY + " | " + ChatColor.YELLOW + s.getModerator());
        }
        viewer.sendMessage(ChatColor.GOLD + "==============================");
    }

    private boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "[Sanction] Tu dois \u00eatre un joueur !");
            return true;
        }
        Player viewer = (Player)sender;
        UUID viewerUUID = viewer.getUniqueId();
        if (args.length == 0) {
            SanctionHistory.showSanctions(viewer, SanctionDB.getSanctions(viewerUUID), 1);
            return true;
        }
        if (args.length == 1 && this.isNumber(args[0])) {
            int page = Integer.parseInt(args[0]);
            SanctionHistory.showSanctions(viewer, SanctionDB.getSanctions(viewerUUID), page);
            return true;
        }
        if (args.length == 1 && !this.isNumber(args[0])) {
            UUID uuid;
            String targetName = args[0];
            Player targetPlayer = Bukkit.getPlayer((String)targetName);
            UUID uUID = uuid = targetPlayer != null ? targetPlayer.getUniqueId() : SanctionDB.getOfflineUUID(targetName);
            if (uuid == null) {
                viewer.sendMessage(ChatColor.RED + "[Sanction] Joueur introuvable !");
                return true;
            }
            SanctionHistory.showSanctions(viewer, SanctionDB.getSanctions(uuid), 1);
            return true;
        }
        if (args.length == 2) {
            UUID uuid;
            if (!this.isNumber(args[1])) {
                viewer.sendMessage(ChatColor.RED + "[Sanction] La page doit \u00eatre un nombre !");
                return true;
            }
            String targetName = args[0];
            Player targetPlayer = Bukkit.getPlayer((String)targetName);
            UUID uUID = uuid = targetPlayer != null ? targetPlayer.getUniqueId() : SanctionDB.getOfflineUUID(targetName);
            if (uuid == null) {
                viewer.sendMessage(ChatColor.RED + "[Sanction] Joueur introuvable !");
                return true;
            }
            int page = Integer.parseInt(args[1]);
            SanctionHistory.showSanctions(viewer, SanctionDB.getSanctions(uuid), page);
            return true;
        }
        viewer.sendMessage(ChatColor.RED + "[Sanction] Utilisation : /punisher [player] [page]");
        return true;
    }
}

