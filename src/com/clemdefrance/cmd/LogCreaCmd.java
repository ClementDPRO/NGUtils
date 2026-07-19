package com.clemdefrance.cmd;

import com.clemdefrance.event.LogCrea;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogCreaCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(LogCrea.crea.contains(player)) {
                LogCrea.crea.remove(player);
                player.sendMessage(ChatColor.GREEN + "Log Creatif Activé!");
            } else {
                LogCrea.crea.add(player);
                player.sendMessage(ChatColor.GREEN + "Log Creatif Déactivé!");
            }
        }
        return true;
    }
}
