/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.plugin.Plugin
 */
package com.clemdefrance.event;

import com.clemdefrance.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class onChatConfirm
implements Listener {
    public static Boolean isConfirm = false;
    public static Player player = null;
    private Main main;

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        this.main = Main.getInstance();
        String msg = e.getMessage();
        Player p = e.getPlayer();
        if (msg.equalsIgnoreCase("/restart")) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "merci d'utiliser /redem au lieu de /restart");
            return;
        }
        if (msg.equalsIgnoreCase("raisin")) {
            e.setCancelled(true);
            for (int i = 0; i < 5000; ++i) {
                e.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "RAISIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIN");
            }
        }
        if (isConfirm.booleanValue() && p == player) {
            if (msg.equalsIgnoreCase("confirm")) {
                e.setCancelled(true);
                for (Player ps : Bukkit.getOnlinePlayers()) {
                    ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                    ps.sendMessage(ChatColor.GOLD + "       Red\u00e9marrage du serveur dans 5 minutes !");
                    ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                    ps.playSound(player.getLocation(), "portal.trigger", 1.0f, 1.0f);
                }
                Bukkit.getScheduler().runTaskTimer((Plugin)this.main, new Runnable(){
                    int time = 300;

                    @Override
                    public void run() {
                        if (this.time <= 0) {
                            for (Player ps : Bukkit.getOnlinePlayers()) {
                                ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                                ps.sendMessage(ChatColor.GOLD + "       Red\u00e9marrage du serveur");
                                ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                            }
                            Bukkit.shutdown();
                            return;
                        }
                        switch (this.time) {
                            case 60: {
                                Player[] arr$ = Bukkit.getOnlinePlayers();
                                int len$ = arr$.length;
                                int i$ = 0;
                                if (i$ < len$) {
                                    Player ps = arr$[i$];
                                    ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                                    ps.sendMessage(ChatColor.GOLD + "       Red\u00e9marrage du serveur dans 1 minutes !");
                                    ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                                }
                            }
                            case 120: {
                                for (Player ps : Bukkit.getOnlinePlayers()) {
                                    ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                                    ps.sendMessage(ChatColor.GOLD + "       Red\u00e9marrage du serveur dans 2 minutes !");
                                    ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                                }
                                break;
                            }
                            case 180: {
                                for (Player ps : Bukkit.getOnlinePlayers()) {
                                    ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                                    ps.sendMessage(ChatColor.GOLD + "       Red\u00e9marrage du serveur dans 3 minutes !");
                                    ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                                }
                                break;
                            }
                            case 240: {
                                for (Player ps : Bukkit.getOnlinePlayers()) {
                                    ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                                    ps.sendMessage(ChatColor.GOLD + "       Red\u00e9marrage du serveur dans 4 minutes !");
                                    ps.sendMessage(ChatColor.LIGHT_PURPLE + "---------------------------------------------");
                                }
                                break;
                            }
                        }
                        --this.time;
                    }
                }, 20L, 20L);
            } else if (msg.equalsIgnoreCase("cancel")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "annul\u00e9!");
            } else {
                e.setCancelled(true);
            }
            isConfirm = false;
        }
    }
}

