/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.clemdefrance.cmd.Event;

import com.clemdefrance.Main;
import com.clemdefrance.cmd.Event.EventData;
import com.clemdefrance.cmd.Event.JoinEvent;
import com.clemdefrance.event.ScoreBoardEvent;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CloseEvent
implements CommandExecutor {
    public static List<Player> playerinevent = new ArrayList<Player>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "please use this command without log!");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Please use /closevent <true/false>");
            return true;
        }
        if (JoinEvent.openEvent == null) {
            sender.sendMessage(ChatColor.RED + "Erreur: aucun \u00e9vent n'est actuellement ouvert :/");
            return true;
        }
        String type = args[0];
        EventData data = Main.getEventdb().getEvent(JoinEvent.openEvent);
        if (type.equalsIgnoreCase("true")) {
            for (Player player : playerinevent) {
                player.teleport(new Location(Bukkit.getWorld((String)"world"), 627.94434, 24.0, -593.57127));
                player.sendMessage(ChatColor.GREEN + "tu as \u00e9t\u00e9 t\u00e9l\u00e9porter au spawn suite \u00e0 la fermeture de l'\u00e9v\u00e8nement. ");
                player.getInventory().clear();
                player.getInventory().setArmorContents(new ItemStack[4]);
                player.getInventory().setItemInHand(null);
                player.updateInventory();
            }
            sender.sendMessage(ChatColor.GOLD + "Tous les joueurs ont \u00e9t\u00e9 tp au spawn!");
            Player player = (Player)sender;
            player.teleport(new Location(Bukkit.getWorld((String)"world"), 627.94434, 24.0, -593.57127));
            ScoreBoardEvent.isevent = false;
        }
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(ChatColor.GOLD + "[*banner id=\"event\"]  L'event " + data.getName() + " est d\u00e9sorm\u00e9 ferm\u00e9!");
            players.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
        JoinEvent.openEvent = null;
        return true;
    }
}

