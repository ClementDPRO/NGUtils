
package com.clemdefrance.event;

import com.clemdefrance.Main;
import com.clemdefrance.cmd.Event.EventData;
import com.clemdefrance.cmd.Event.JoinEvent;
import com.clemdefrance.cmd.Event.OpenEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScoreBoardEvent
implements Listener {
    public static Boolean isevent = false;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (isevent.booleanValue()) {
            EventData data = Main.getEventdb().getEvent(JoinEvent.openEvent);
            OpenEvent.createScoreboard(e.getPlayer(), data);
        }
    }
}

