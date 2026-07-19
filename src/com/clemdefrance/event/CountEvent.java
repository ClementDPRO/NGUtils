package com.clemdefrance.event;

import com.clemdefrance.Main;
import com.clemdefrance.event.PlayerPickCount;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class CountEvent {
    public static int Second = 0;

    public void count() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)Main.getInstance(), new Runnable(){

            @Override
            public void run() {
                long tempsMillisecondes = System.currentTimeMillis();
                Second = (int)(tempsMillisecondes / 1000L);
                if (Second == 86399) {
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String today = format.format(date);
                    Main.getPlayercountDB().add(PlayerPickCount.PlayerPick, today);
                    PlayerPickCount.PlayerPick = 0;
                }
            }
        }, 0L, 20L);
    }
}

