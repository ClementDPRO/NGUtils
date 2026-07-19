/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.clemdefrance.cmd.Event;

import com.clemdefrance.Main;
import com.clemdefrance.cmd.Event.EventData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateEvent
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        double sz;
        double sy;
        double sx;
        double z;
        double y;
        double x;
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "please use this command without log!");
            return true;
        }
        if (args.length != 7) {
            sender.sendMessage(ChatColor.RED + "Please use /createevent <name> <X> <Y> <Z> <X> <Y> <Z>");
            return true;
        }
        String name = args[0];
        try {
            x = Integer.parseInt(args[1]);
            y = Integer.parseInt(args[2]);
            z = Integer.parseInt(args[3]);
            sx = Integer.parseInt(args[4]);
            sy = Integer.parseInt(args[5]);
            sz = Integer.parseInt(args[6]);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Error: please enter correct number! " + e);
            return true;
        }
        if (name.length() > 14) {
            sender.sendMessage(ChatColor.RED + "Le nom de ton \u00e9vent ne dois pas d\u00e9pass\u00e9 14 caract\u00e8re.");
            return true;
        }
        if (Main.getEventdb().createEvent(new EventData(name, x, y, z, sx, sy, sz, ((Player)sender).getWorld().getName()))) {
            sender.sendMessage(ChatColor.GREEN + "Event cr\u00e9e avec succ\u00e8s: " + args[0] + "!");
        } else {
            sender.sendMessage(ChatColor.RED + "Erreur lors de la cr\u00e9ation de l'\u00e9vent " + args[0] + "!");
        }
        return true;
    }
}

