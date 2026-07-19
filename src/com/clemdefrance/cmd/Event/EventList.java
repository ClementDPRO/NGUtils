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
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventList
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "please use this command without log!");
            return true;
        }
        List<EventData> events = Main.getEventdb().getAllEvents();
        if (events.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Aucun n'event n'a encore \u00e9t\u00e9 cr\u00e9e.");
            return true;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\u00a76Liste des \u00e9vents : \n \u00a7e");
        for (int i = 0; i < events.size(); ++i) {
            EventData event = events.get(i);
            builder.append(event.getName() + "\n");
            if (i >= events.size() - 1) continue;
            builder.append("\u00a77, \u00a7e");
        }
        String message = builder.toString();
        sender.sendMessage(message);
        return true;
    }
}

