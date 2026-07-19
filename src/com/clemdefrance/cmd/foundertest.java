/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.scoreboard.Scoreboard
 *  org.bukkit.scoreboard.ScoreboardManager
 *  org.bukkit.scoreboard.Team
 */
package com.clemdefrance.cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class foundertest
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        if (args.length < 1) {
            return true;
        }
        String teamname = args[0];
        Team team = board.getTeam(teamname);
        if (team == null) {
            team = board.registerNewTeam(teamname);
        }
        Player player = (Player)sender;
        for (Team teams : board.getTeams()) {
            if (!teams.hasPlayer((OfflinePlayer)player)) continue;
            teams.removePlayer((OfflinePlayer)player);
        }
        team.addPlayer((OfflinePlayer)player);
        return true;
    }
}

