/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.clemdefrance.cmd;

import com.clemdefrance.Main;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RoutineInv
implements CommandExecutor {
    private Main plugin = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Merci d'ex\u00e9cuter cette commande en jeu !");
            return true;
        }
        final Player viewer = (Player)sender;
        final ArrayList<Player> players = new ArrayList<Player>(Arrays.asList(Bukkit.getOnlinePlayers()));
        sender.sendMessage(ChatColor.GREEN + "D\u00e9but de la routine inventaire du joueur " + sender.getName());
        Bukkit.getLogger().info("D\u00e9but de la routine inventaire du joueur " + sender.getName());
        new BukkitRunnable(){
            int index = 0;

            public void run() {
                if (this.index >= players.size()) {
                    viewer.sendMessage(ChatColor.GREEN + "Fin de la routine inventaire !");
                    Bukkit.getLogger().info("Fin de la routine inventaire du joueur " + viewer.getName());
                    viewer.closeInventory();
                    this.cancel();
                    return;
                }
                Player player = (Player)players.get(this.index);
                ++this.index;
                if (player == null) {
                    return;
                }
                viewer.closeInventory();
                Inventory inv = Bukkit.createInventory(null, (int)54, (String)("Inventaire de " + player.getName()));
                ItemStack[] contents = player.getInventory().getContents();
                for (int i = 0; i < contents.length; ++i) {
                    inv.setItem(i, contents[i]);
                }
                ItemStack[] armor = player.getInventory().getArmorContents();
                for (int i = 0; i < armor.length; ++i) {
                    inv.setItem(36 + i, armor[i]);
                }
                viewer.sendMessage(ChatColor.GOLD + "Joueur : " + ChatColor.RED + player.getName());
                viewer.openInventory(inv);
            }
        }.runTaskTimer((Plugin)this.plugin, 0L, 60L);
        return true;
    }
}

