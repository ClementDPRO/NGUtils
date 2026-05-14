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

public class KB implements Listener {

    private double horizontal = 0.65;
    private double vertical = 0.25;
    private double airborneMultiplier = 0.75;
    private double maxVelocity = 0.48;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerHit(EntityDamageByEntityEvent event) {

        if (event.isCancelled()) return;
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        final Player victim = (Player) event.getEntity();

        if (victim.getNoDamageTicks() > 10) return;

        Bukkit.getScheduler().runTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {

                Vector vel = victim.getVelocity();
                double x = vel.getX();
                double z = vel.getZ();

                double speed = Math.sqrt(x * x + z * z);
                if (speed < 0.001) return;

                boolean onGround = victim.isOnGround();

                double h = horizontal;
                if (!onGround) h *= airborneMultiplier;

                double newX = (x / speed) * h;
                double newZ = (z / speed) * h;

                newX = Math.max(-maxVelocity, Math.min(maxVelocity, newX));
                newZ = Math.max(-maxVelocity, Math.min(maxVelocity, newZ));

                double newY = vertical;

                Vector finalVel = new Vector(newX, newY, newZ);
                victim.setVelocity(finalVel);
            }
        });
    }
}
