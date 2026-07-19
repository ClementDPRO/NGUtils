
package com.clemdefrance;

import com.clemdefrance.DB.EventDB;
import com.clemdefrance.DB.PlayercountDB;
import com.clemdefrance.DB.SanctionDB;
import com.clemdefrance.cmd.*;
import com.clemdefrance.cmd.Event.CloseEvent;
import com.clemdefrance.cmd.Event.CreateEvent;
import com.clemdefrance.cmd.Event.DeleteEvent;
import com.clemdefrance.cmd.Event.EventList;
import com.clemdefrance.cmd.Event.JoinEvent;
import com.clemdefrance.cmd.Event.LeaveEvent;
import com.clemdefrance.cmd.Event.OpenEvent;
import com.clemdefrance.cmd.Event.gg;
import com.clemdefrance.cmd.sanction.Ban;
import com.clemdefrance.cmd.sanction.IpView;
import com.clemdefrance.cmd.sanction.Mute;
import com.clemdefrance.cmd.sanction.SanctionHistory;
import com.clemdefrance.cmd.sanction.Sanctiondelete;
import com.clemdefrance.cmd.sanction.TempBan;
import com.clemdefrance.cmd.sanction.UnBan;
import com.clemdefrance.cmd.sanction.UnMute;
import com.clemdefrance.cmd.sanction.warn;
import com.clemdefrance.event.*;
import com.clemdefrance.listener.OnMuteChat;
import java.sql.SQLException;
import java.util.UUID;

import com.clemdefrance.market.Catalog;
import com.clemdefrance.market.Hdv;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main
extends JavaPlugin {
    private static Main instance;
    public static boolean isDebug;
    private OnHurt onHurtInstance;
    private static SanctionDB sanctionDB;
    private static EventDB eventdb;
    private static PlayercountDB playercountDB;

    public void onClockGrade() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
                        String prefix = JoinGradeEvent.listPlayer.get(targetPlayer.getName());

                        if (prefix == null) {
                            prefix = "[*banner id=\"joueur\"]";
                        }

                        String formattedName = prefix + "  " + targetPlayer.getName();

                        for (Player receiver : Bukkit.getOnlinePlayers()) {
                            net.minecraft.server.v1_6_R3.Packet201PlayerInfo removePacket =
                                    new net.minecraft.server.v1_6_R3.Packet201PlayerInfo(formattedName, false, 9999);
                            ((org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer) receiver).getHandle().playerConnection.sendPacket(removePacket);

                            JoinGradeEvent.sendPacket(receiver, targetPlayer, formattedName);
                        }
                    }
                } catch(Exception e) {
                    Bukkit.getLogger().severe("Erreur lors du rafraîchissement automatique du TabList.");
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        if(player.isOp()) {
                            player.sendMessage(ChatColor.RED + "Erreur lors du rafraîchissement automatique du TabList.");
                        }
                    }
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(this, 0L, 100L);
    }

    public void onEnable() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "nationsgui");
        System.out.println("[NGUtils] Loading Instance!");
        instance = this;
        this.onHurtInstance = new OnHurt(this);
        sanctionDB = new SanctionDB(this);
        sanctionDB.connect();
        eventdb = new EventDB(this);
        eventdb.connect();
        playercountDB = new PlayercountDB(this.getDataFolder());
        playercountDB.connect();
        System.out.println("[NGUtils] Loading nationsgui Packet");
        this.getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "ngupgrades");
        System.out.println("[NGUtils] Loading onDrink event");
        this.getServer().getPluginManager().registerEvents((Listener)new OnDrink(), (Plugin)this);
        System.out.println("[NGUtils] Loading EnderPear event");
        this.getServer().getPluginManager().registerEvents((Listener)new EnderPear(), (Plugin)this);
        System.out.println("[NGUtils] Loading KB event");
        this.getServer().getPluginManager().registerEvents((Listener)new KB(), (Plugin)this);
        System.out.println("[NGUtils] Loading JoinListener event");
        this.getServer().getPluginManager().registerEvents((Listener)new JoinListener(), (Plugin)this);
        System.out.println("[NGUtils] Loading ScoreBoardEvent event");
        this.getServer().getPluginManager().registerEvents((Listener)new ScoreBoardEvent(), (Plugin)this);
        System.out.println("[NGUtils] Loading onChatConfirm event");
        this.getServer().getPluginManager().registerEvents((Listener)new onChatConfirm(), (Plugin)this);
        System.out.println("[NGUtils] Loading onMentionChAT event");
        this.getServer().getPluginManager().registerEvents((Listener)new onMentionChat(), (Plugin)this);
        System.out.println("[NGUtils] Loading gradeListener event");
        this.getServer().getPluginManager().registerEvents((Listener)this.onHurtInstance, (Plugin)this);
        System.out.println("[NGUtils] Loading MuteChat event");
        this.getServer().getPluginManager().registerEvents((Listener)new OnMuteChat(), (Plugin)this);
        System.out.println("[NGUtils] Loading Ban event");
        this.getServer().getPluginManager().registerEvents((Listener)new BanJoin(), (Plugin)this);
        System.out.println("[NGUtils] Loading PlayerPickCount event");
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerPickCount(), (Plugin)this);
        System.out.println("[NGUtils] Loading OnMessageSend event");
        this.getServer().getPluginManager().registerEvents((Listener)new OnMessageSend(), (Plugin)this);
        System.out.println("[NGUtils] Loading onPlayerQuit event");
        this.getServer().getPluginManager().registerEvents((Listener)new onPlayerQuit(), (Plugin)this);
        System.out.println("[NGUtils] Loading LogCrea event");
        this.getServer().getPluginManager().registerEvents((Listener)new LogCrea(), (Plugin)this);
        System.out.println("[NGUtils] Loading JoinGradeEvent event");
        this.getServer().getPluginManager().registerEvents((Listener)new JoinGradeEvent(), (Plugin)this);


        System.out.println("[NGUtils] Loading assaut command");
        this.getCommand("assaut").setExecutor((CommandExecutor)new Assaut());
        System.out.println("[NGUtils] Loading stopassaut command");
        this.getCommand("stopassaut").setExecutor((CommandExecutor)new StopAssaut());
        System.out.println("[NGUtils] Loading getgroup command");
        this.getCommand("getgroup").setExecutor((CommandExecutor)new GetGroup());
        System.out.println("[NGUtils] Loading redem command");
        this.getCommand("redem").setExecutor((CommandExecutor)new Redem());
        System.out.println("[NGUtils] Loading debug command");
        this.getCommand("debug").setExecutor((CommandExecutor)new IsDebug());
        System.out.println("[NGUtils] Loading spawn command");
        this.getCommand("spawn").setExecutor((CommandExecutor)new Spawn());
        System.out.println("[NGUtils] Loading warn command");
        this.getCommand("warn").setExecutor((CommandExecutor)new warn());
        System.out.println("[NGUtils] Loading Punisher command");
        this.getCommand("punisher").setExecutor((CommandExecutor)new SanctionHistory());
        System.out.println("[NGUtils] Loading Mute command");
        this.getCommand("mute").setExecutor((CommandExecutor)new Mute());
        System.out.println("[NGUtils] Loading UnMute command");
        this.getCommand("unmute").setExecutor((CommandExecutor)new UnMute());
        System.out.println("[NGUtils] Loading TempBan command");
        this.getCommand("tempban").setExecutor((CommandExecutor)new TempBan());
        System.out.println("[NGUtils] Loading Ban command");
        this.getCommand("ban").setExecutor((CommandExecutor)new Ban());
        System.out.println("[NGUtils] Loading UnBan command");
        this.getCommand("unban").setExecutor((CommandExecutor)new UnBan());
        System.out.println("[NGUtils] Loading ipview Command");
        this.getCommand("ipview").setExecutor((CommandExecutor)new IpView());
        System.out.println("[NGUtils] Loading listchest Command");
        this.getCommand("listchest").setExecutor((CommandExecutor)new ListChests());
        System.out.println("[NGUtils] Loading routineinv Command");
        this.getCommand("routineinv").setExecutor((CommandExecutor)new RoutineInv());
        System.out.println("[NGUtils] Loading foundertest Command");
        this.getCommand("foundertest").setExecutor((CommandExecutor)new foundertest());
        System.out.println("[NGUtils] Loading sanctiondel Command");
        this.getCommand("sanctiondel").setExecutor((CommandExecutor)new Sanctiondelete());
        System.out.println("[NGUtils] Loading createvent Command");
        this.getCommand("createvent").setExecutor((CommandExecutor)new CreateEvent());
        System.out.println("[NGUtils] Loading listevent Command");
        this.getCommand("listevent").setExecutor((CommandExecutor)new EventList());
        System.out.println("[NGUtils] Loading JoinEvent Command");
        this.getCommand("joinevent").setExecutor((CommandExecutor)new JoinEvent());
        System.out.println("[NGUtils] Loading leavevent Command");
        this.getCommand("leavevent").setExecutor((CommandExecutor)new LeaveEvent());
        System.out.println("[NGUtils] Loading CloseEvent Command");
        this.getCommand("closevent").setExecutor((CommandExecutor)new CloseEvent());
        System.out.println("[NGUtils] Loading OpenEvent Command");
        this.getCommand("openevent").setExecutor((CommandExecutor)new OpenEvent());
        System.out.println("[NGUtils] Loading GG Command");
        this.getCommand("gg").setExecutor((CommandExecutor)new gg());
        System.out.println("[NGUtils] Loading FML Command");
        this.getCommand("fml").setExecutor((CommandExecutor)new FML());
        System.out.println("[NGUtils] Loading DeleteEvent Command");
        this.getCommand("deletevent").setExecutor((CommandExecutor)new DeleteEvent());
        System.out.println("[NGUtils] Loading PlayerCount Command");
        this.getCommand("playercount").setExecutor((CommandExecutor)new playercount());
        System.out.println("[NGUtils] Loading HDV Command");
        this.getCommand("hdv").setExecutor((CommandExecutor)new Hdv());
        System.out.println("[NGUtils] Loading Catalog Command");
        this.getCommand("catalog").setExecutor((CommandExecutor)new Catalog());
        System.out.println("[NGUtils] Loading allplayer Command");
        this.getCommand("allplayer").setExecutor((CommandExecutor)new getAllPlayer());
        System.out.println("[NGUtils] Loading Spy Command");
        this.getCommand("spy").setExecutor((CommandExecutor)new Spy());
        System.out.println("[NGUtils] Loading LogCreaCmd Command");
        this.getCommand("logcrea").setExecutor((CommandExecutor)new LogCreaCmd());

        System.out.println("[NGUtils] Remove worldborder");
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)"nworldborder remove Assaut");
        this.getLogger().info("TabGrade active.");
        BanJoin.convoc.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID uuid = SanctionDB.getOfflineUUID(p.getName());
            if (!SanctionDB.isBanned(uuid)) continue;
            p.kickPlayer(ChatColor.RED + "Tu es banni !");
        }
        CountEvent countEvent = new CountEvent();
        countEvent.count();
        onClockGrade();
        System.out.println("[NGUtils] Enabled!");
    }

    public void onDisable() {
        System.out.println("[NGUtil] Unloading all component, Good bye!");
        this.getLogger().info("TabGrade desactive.");
        try {
            if (SanctionDB.getConnection() != null && !SanctionDB.getConnection().isClosed()) {
                SanctionDB.getConnection().close();
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            WorldborderManager.removeBorder(player);
        }
        eventdb.close();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
        playercountDB.disconnect();
    }

    public static Main getInstance() {
        return instance;
    }

    public static SanctionDB getSanctionDB() {
        return sanctionDB;
    }

    public static EventDB getEventdb() {
        return eventdb;
    }

    public static PlayercountDB getPlayercountDB() {
        return playercountDB;
    }

    static {
        isDebug = false;
    }
}

