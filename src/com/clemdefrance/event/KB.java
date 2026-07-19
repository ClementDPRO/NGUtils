/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package com.clemdefrance.event;

import com.clemdefrance.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class KB
implements Listener {
    private double horizontal = 0.5;
    private double vertical = 0.3;
    private double airborneMultiplier = 1.125;
    private double maxVelocity = 0.48;

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player victim = (Player)event.getEntity();
        if (victim.getNoDamageTicks() > 10) {
            return;
        }
        Bukkit.getScheduler().runTask((Plugin)Main.getInstance(), new Runnable(){

            @Override
            public void run() {
                double z;
                Vector vel = victim.getVelocity();
                double x = vel.getX();
                double speed = Math.sqrt(x * x + (z = vel.getZ()) * z);
                if (speed < 0.001) {
                    return;
                }
                boolean onGround = victim.isOnGround();
                double h = KB.this.horizontal;
                if (!onGround) {
                    h *= KB.this.airborneMultiplier;
                }
                double newX = x / speed * h;
                double newZ = z / speed * h;
                newX = Math.max(-KB.this.maxVelocity, Math.min(KB.this.maxVelocity, newX));
                newZ = Math.max(-KB.this.maxVelocity, Math.min(KB.this.maxVelocity, newZ));
                double newY = KB.this.vertical;
                Vector finalVel = new Vector(newX, newY, newZ);
                victim.setVelocity(finalVel);
            }
        });
    }
}

