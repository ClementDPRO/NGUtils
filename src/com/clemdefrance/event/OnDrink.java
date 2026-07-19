package com.clemdefrance.event;

import com.clemdefrance.Main;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OnDrink
implements Listener {
    private final Map<UUID, Integer> cooldownTime = new HashMap<UUID, Integer>();

    public void startCountdown(Player p) {
        final UUID id = p.getUniqueId();
        this.cooldownTime.put(id, 150);
        new BukkitRunnable(){

            public void run() {
                Integer t = (Integer)OnDrink.this.cooldownTime.get(id);
                if (t == null || t <= 0) {
                    OnDrink.this.cooldownTime.remove(id);
                    this.cancel();
                    return;
                }
                OnDrink.this.cooldownTime.put(id, t - 1);
            }
        }.runTaskTimer((Plugin)Main.getInstance(), 0L, 20L);
    }

    public boolean canDrink(Player p) {
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
    public void onDrink(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        if (event.getItem() == null) {
            return;
        }
        ItemStack stack = event.getItem();
        int id = stack.getTypeId();
        if (id == 3668 || id == 3677 || id == 3678) {
            if (!this.canDrink(p)) {
                p.sendMessage(ChatColor.translateAlternateColorCodes((char)'\u00a7', (String)("\u00a77Vous devez patienter \u00a7c" + this.getRemaining(p) + "\u00a7cs \u00a77avant de pouvoir de nouveau utiliser cet item.")));
                event.setCancelled(true);
            } else {
                this.startCountdown(event.getPlayer());
            }
        }
    }
}

