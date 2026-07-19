/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.clemdefrance.event;

import com.clemdefrance.Main;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OnHurt
implements Listener {
    private Main plugin;
    private final long COMBAT_TIME = 15000L;
    private static HashMap<UUID, Long> combat = new HashMap();
    private static HashMap<UUID, BukkitRunnable> combatTimers = new HashMap();

    public OnHurt(Main plugin) {
        this.plugin = plugin;
    }

    public static boolean isInCombat(Player p) {
        if (!combat.containsKey(p.getUniqueId())) {
            return false;
        }
        return System.currentTimeMillis() < combat.get(p.getUniqueId());
    }

    public void setCombat(Player p) {
        combat.put(p.getUniqueId(), System.currentTimeMillis() + 15000L);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        final Player victim = (Player)event.getEntity();
        final Player damager = (Player)event.getDamager();
        boolean victimWasInCombat = OnHurt.isInCombat(victim);
        boolean damagerWasInCombat = OnHurt.isInCombat(damager);
        this.setCombat(victim);
        this.setCombat(damager);
        if (!victimWasInCombat) {
            victim.sendMessage("\u00a7cVous avez \u00e9t\u00e9 mis en combat par \u00a74" + damager.getName());
        }
        if (!damagerWasInCombat) {
            damager.sendMessage("\u00a7cVous \u00eates actuellement en combat avec \u00a74" + victim.getName());
        }
        if (combatTimers.containsKey(victim.getUniqueId())) {
            combatTimers.get(victim.getUniqueId()).cancel();
        }
        BukkitRunnable victimTimer = new BukkitRunnable(){

            public void run() {
                if (!OnHurt.isInCombat(victim)) {
                    victim.sendMessage("\u00a7aVous n'\u00eates plus en combat");
                    combatTimers.remove(victim.getUniqueId());
                    this.cancel();
                }
            }
        };
        victimTimer.runTaskTimer((Plugin)this.plugin, 20L, 20L);
        combatTimers.put(victim.getUniqueId(), victimTimer);
        if (combatTimers.containsKey(damager.getUniqueId())) {
            combatTimers.get(damager.getUniqueId()).cancel();
        }
        BukkitRunnable damagerTimer = new BukkitRunnable(){

            public void run() {
                if (!OnHurt.isInCombat(damager)) {
                    damager.sendMessage("\u00a7aVous n'\u00eates plus en combat");
                    combatTimers.remove(damager.getUniqueId());
                    this.cancel();
                }
            }
        };
        damagerTimer.runTaskTimer((Plugin)this.plugin, 20L, 20L);
        combatTimers.put(damager.getUniqueId(), damagerTimer);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        String cmd = event.getMessage().toLowerCase();
        if ((cmd.equals("/spawn") || cmd.startsWith("/spawn ")) && OnHurt.isInCombat(p)) {
            event.setCancelled(true);
            p.sendMessage("\u00a7cTu ne peux pas retourner au spawn car tu es en combat !");
        }
    }
}

