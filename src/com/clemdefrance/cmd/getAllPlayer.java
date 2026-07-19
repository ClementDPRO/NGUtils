package com.clemdefrance.cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class getAllPlayer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        StringBuilder builder = new StringBuilder(" ");
        for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            builder.append("§6" + player.getName() + " / ");
        }

        commandSender.sendMessage("§5----- All Player + " + Bukkit.getOfflinePlayers().length + "-----\n"  + builder.toString() + "§5----------------");
        return true;
    }
}
