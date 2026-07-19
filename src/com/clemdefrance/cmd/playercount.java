/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 */
package com.clemdefrance.cmd;

import com.clemdefrance.Main;
import com.clemdefrance.event.PlayerPickCount;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class playercount
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String today = String.valueOf(PlayerPickCount.PlayerPick);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyy");
        cal.add(5, -5);
        String day5 = format.format(cal.getTime());
        cal.add(5, -4);
        String day4 = format.format(cal.getTime());
        cal.add(5, -3);
        String day3 = format.format(cal.getTime());
        cal.add(5, -2);
        String day2 = format.format(cal.getTime());
        sender.sendMessage(ChatColor.GOLD + " ----- Pic de joueur ses 5 dernier jours: ----- \n " + ChatColor.DARK_PURPLE + "Aujourd'hui: " + ChatColor.RED + today + "\n" + ChatColor.DARK_PURPLE + "Hier: " + ChatColor.RED + Main.getPlayercountDB().read(day2) + "\n" + ChatColor.DARK_PURPLE + day3 + ": " + ChatColor.RED + Main.getPlayercountDB().read(day3) + "\n" + ChatColor.DARK_PURPLE + day4 + ": " + ChatColor.RED + Main.getPlayercountDB().read(day4) + "\n" + ChatColor.DARK_PURPLE + day5 + ": " + ChatColor.RED + Main.getPlayercountDB().read(day5) + "\n");
        return true;
    }
}

