package com.clemdefrance.event;

import com.clemdefrance.cmd.Spy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class OnMessageSend implements Listener {
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player target = event.getPlayer();
        String commandString = event.getMessage();

        if (Spy.spyMap.containsKey(target.getName())) {
            List<CommandSender> spies = Spy.spyMap.get(target.getName());
            List<CommandSender> toRemove = new ArrayList<CommandSender>();

            for (CommandSender spy : spies) {
                if (spy instanceof Player) {
                    Player spyPlayer = (Player) spy;
                    if (spyPlayer.isOnline()) {
                        spyPlayer.sendMessage(ChatColor.DARK_PURPLE + "[Spy] " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + ": " + commandString);
                    } else {
                        toRemove.add(spy);
                    }
                } else {
                    spy.sendMessage("[Spy] " + target.getName() + ": " + commandString);
                }
            }

            if (!toRemove.isEmpty()) {
                spies.removeAll(toRemove);
                if (spies.isEmpty()) {
                    Spy.spyMap.remove(target.getName());
                }
            }
        }
    }
}