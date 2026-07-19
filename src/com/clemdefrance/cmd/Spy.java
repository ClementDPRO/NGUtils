package com.clemdefrance.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Spy implements CommandExecutor {
    public static HashMap<String, List<CommandSender>> spyMap = new HashMap<String, List<CommandSender>>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /spy <pseudo>");
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);

        if(target == null) {
            sender.sendMessage(ChatColor.RED + "Ce joueur n'est pas connecté.");
            return true;
        }

        if (!spyMap.containsKey(target.getName())) {
            spyMap.put(target.getName(), new ArrayList<CommandSender>());
        }

        List<CommandSender> spies = spyMap.get(target.getName());

        if (spies.contains(sender)) {
            spies.remove(sender);
            sender.sendMessage(ChatColor.GREEN + "Vous n'espionnez plus " + target.getName() + ".");

            if (spies.isEmpty()) {
                spyMap.remove(target.getName());
            }
        } else {
            spies.add(sender);
            sender.sendMessage(ChatColor.GREEN + "Vous espionnez désormais les commandes de " + target.getName() + ".");
        }
        return true;
    }
}