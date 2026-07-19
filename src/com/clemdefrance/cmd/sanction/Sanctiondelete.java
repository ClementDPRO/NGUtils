/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 */
package com.clemdefrance.cmd.sanction;

import com.clemdefrance.DB.SanctionDB;
import com.clemdefrance.sanction.Sanction;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Sanctiondelete
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int id;
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "please use /sanctiondelete <pseudo> <id>");
            return true;
        }
        UUID uuid = SanctionDB.getOfflineUUID(args[0]);
        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + "ERROR: l'unique ID n'est pas initi\u00e9.");
            return true;
        }
        try {
            id = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Merci de rentr\u00e9e un ID valide! " + e);
            e.printStackTrace();
            return true;
        }
        Sanction sanction = SanctionDB.getSanctionById(id);
        if (sanction == null) {
            sender.sendMessage(ChatColor.RED + "Aucune sanction trouv\u00e9e avec l'ID #" + id);
            return true;
        }
        boolean success = SanctionDB.deleteSanction(id);
        if (success) {
            sender.sendMessage(ChatColor.GREEN + "La sanction #" + id + " (" + (Object)((Object)sanction.getType()) + ") a \u00e9t\u00e9 supprim\u00e9e avec succ\u00e8s.");
        } else {
            sender.sendMessage(ChatColor.RED + "Une erreur est survenue lors de la suppression de la sanction #" + id + ".");
        }
        return true;
    }
}

