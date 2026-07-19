/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.clemdefrance.event;

import com.clemdefrance.Main;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EnderPear
implements Listener {
    private final Map<UUID, Integer> cooldownTime = new HashMap<UUID, Integer>();

    public void startCountdown(Player p) {
        final UUID id = p.getUniqueId();
        this.cooldownTime.put(id, 10);
        new BukkitRunnable(){

            public void run() {
                Integer t = (Integer)EnderPear.this.cooldownTime.get(id);
                if (t == null || t <= 0) {
                    EnderPear.this.cooldownTime.remove(id);
                    this.cancel();
                    return;
                }
                EnderPear.this.cooldownTime.put(id, t - 1);
            }
        }.runTaskTimer((Plugin)Main.getInstance(), 0L, 20L);
    }

    public boolean canUse(Player p) {
        return !this.cooldownTime.containsKey(p.getUniqueId());
    }

    public int getRemaining(Player p) {
        UUID id = p.getUniqueId();
        if (!this.cooldownTime.containsKey(id)) {
            return 0;
        }
        return this.cooldownTime.get(id);
    }

    @EventHandler
    public void onEnder(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getItem() == null) {
            return;
        }
        ItemStack stack = event.getItem();
        if (stack.getType() == Material.ENDER_PEARL) {
            if (!this.canUse(p)) {
                p.sendMessage(ChatColor.translateAlternateColorCodes((char)'\u00a7', (String)("\u00a77Cooldown restant : \u00a77" + this.getRemaining(p))));
                event.setCancelled(true);
            } else {
                this.startCountdown(event.getPlayer());
            }
        }
    }
}

