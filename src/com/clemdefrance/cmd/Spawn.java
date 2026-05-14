package com.clemdefrance.cmd;

import com.clemdefrance.event.OnHurt;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("spawn")) return false;

        if (OnHurt.isInCombat(p)) {
            p.sendMessage("§cTu ne peux pas retourner au spawn car tu es en combat !");
            return true;
        }

        Location loc = new Location(p.getWorld(), 667.0, 23.0, -266.0);
        p.teleport(loc);
        p.sendMessage("§aTéléportation au spawn !");

        return true;
    }
}
