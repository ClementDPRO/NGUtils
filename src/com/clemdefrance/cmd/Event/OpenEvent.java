/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 *  org.bukkit.scoreboard.Score
 *  org.bukkit.scoreboard.Scoreboard
 *  org.bukkit.scoreboard.ScoreboardManager
 */
package com.clemdefrance.cmd.Event;

import com.clemdefrance.Main;
import com.clemdefrance.cmd.Event.EventData;
import com.clemdefrance.cmd.Event.JoinEvent;
import com.clemdefrance.event.ScoreBoardEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class OpenEvent
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Please use /openevent <event>");
                return true;
            }
            if (Main.getEventdb().getEvent(args[0]) == null) {
                sender.sendMessage(ChatColor.RED + "Event incorrect! fais /eventlist pour voir la list des \u00e9vent!");
                return true;
            }
            EventData data = Main.getEventdb().getEvent(args[0]);
            if (Bukkit.getWorld((String)data.getWorld()) == null) {
                sender.sendMessage("Erreur lors du lancement de l'\u00e9vent: le monde auquel l'\u00e9vent \u00e0 \u00e9t\u00e9 cr\u00e9e n'existe plus.");
                return true;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1.0f, 1.0f);
                OpenEvent.createScoreboard(player, data);
                player.sendMessage("[*banner id=\"event\"] L'\u00e9vent " + data.getName() + " est d\u00e9sorm\u00e9 ouvert! faite /joinevent pour le rejoindre.");
            }
            JoinEvent.openEvent = data.getName();
            Player player = (Player)sender;
            player.teleport(new Location(Bukkit.getWorld((String)data.getWorld()), data.getStaffX(), data.getStaffY(), data.getStaffZ()));
            ScoreBoardEvent.isevent = true;
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Please use it in-game!");
        return true;
    }

    public static void createScoreboard(Player player, EventData data) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("stats", "dummy");
        objective.setDisplayName("PvP Flamengo");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score ligne1 = objective.getScore(Bukkit.getOfflinePlayer((String)"\u00a7cEvent en cours"));
        Score ligne2 = objective.getScore(Bukkit.getOfflinePlayer((String)("\u00a7e" + data.getName())));
        Score ligne3 = objective.getScore(Bukkit.getOfflinePlayer((String)"\u00a7b--------------"));
        Score ligne4 = objective.getScore(Bukkit.getOfflinePlayer((String)"\u00a7bRejoindre via:"));
        Score ligne5 = objective.getScore(Bukkit.getOfflinePlayer((String)"\u00a7a/joinevent"));
        ligne1.setScore(5);
        ligne2.setScore(4);
        ligne3.setScore(3);
        ligne4.setScore(2);
        ligne5.setScore(1);
        player.setScoreboard(scoreboard);
    }
}

