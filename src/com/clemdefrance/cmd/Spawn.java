/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.clemdefrance.cmd;

import com.clemdefrance.event.OnHurt;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player)sender;
        if (!cmd.getName().equalsIgnoreCase("spawn")) {
            return false;
        }
        if (OnHurt.isInCombat(p)) {
            p.sendMessage("\u00a7cTu ne peux pas retourner au spawn car tu es en combat !");
            return true;
        }
        Location loc = new Location(p.getWorld(), 627.94434, 24.0, -593.57127);
        p.teleport(loc);
        p.sendMessage("\u00a7aT\u00e9l\u00e9portation au spawn !");
        return true;
    }
}

