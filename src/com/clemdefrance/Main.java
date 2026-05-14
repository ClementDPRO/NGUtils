/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.clemdefrance;

import com.clemdefrance.DB.SanctionDB;
import com.clemdefrance.cmd.Assaut;
import com.clemdefrance.cmd.GetGroup;
import com.clemdefrance.cmd.IsDebug;
import com.clemdefrance.cmd.ListChests;
import com.clemdefrance.cmd.Redem;
import com.clemdefrance.cmd.Spawn;
import com.clemdefrance.cmd.StopAssaut;
import com.clemdefrance.cmd.sanction.Ban;
import com.clemdefrance.cmd.sanction.IpView;
import com.clemdefrance.cmd.sanction.Mute;
import com.clemdefrance.cmd.sanction.SanctionHistory;
import com.clemdefrance.cmd.sanction.TempBan;
import com.clemdefrance.cmd.sanction.UnBan;
import com.clemdefrance.cmd.sanction.UnMute;
import com.clemdefrance.cmd.sanction.warn;
import com.clemdefrance.event.BanJoin;
import com.clemdefrance.event.EnderPear;
import com.clemdefrance.event.JoinListener;
import com.clemdefrance.event.KB;
import com.clemdefrance.event.OnDrink;
import com.clemdefrance.event.OnHurt;
import com.clemdefrance.event.OnMuteCHa;
import com.clemdefrance.event.onChatConfirm;
import com.clemdefrance.event.onMentionChAT;
import java.sql.SQLException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin {
    private static Main instance;
    public static boolean isDebug;
    private OnHurt onHurtInstance;
    private static SanctionDB sanctionDB;

    public void onEnable() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        System.out.println("[NGUtils] Loading Instance!");
        instance = this;
        this.onHurtInstance = new OnHurt(this);
        sanctionDB = new SanctionDB(this);
        sanctionDB.connect();
        System.out.println("[NGUtils] Loading nationsgui Packet");
        System.out.println("[NGUtils] Loading onDrink event");
        this.getServer().getPluginManager().registerEvents((Listener)new OnDrink(), (Plugin)this);
        System.out.println("[NGUtils] Loading EnderPear event");
        this.getServer().getPluginManager().registerEvents((Listener)new EnderPear(), (Plugin)this);
        System.out.println("[NGUtils] Loading KB event");
        this.getServer().getPluginManager().registerEvents((Listener)new KB(), (Plugin)this);
        System.out.println("[NGUtils] Loading JoinListener event");
        this.getServer().getPluginManager().registerEvents((Listener)new JoinListener(), (Plugin)this);
        System.out.println("[NGUtils] Loading onChatConfirm event");
        this.getServer().getPluginManager().registerEvents((Listener)new onChatConfirm(), (Plugin)this);
        System.out.println("[NGUtils] Loading onMentionChAT event");
        this.getServer().getPluginManager().registerEvents((Listener)new onMentionChAT(), (Plugin)this);
        System.out.println("[NGUtils] Loading gradeListener event");
        this.getServer().getPluginManager().registerEvents((Listener)this.onHurtInstance, (Plugin)this);
        System.out.println("[NGUtils] Loading MuteChat event");
        this.getServer().getPluginManager().registerEvents((Listener)new OnMuteCHa(), (Plugin)this);
        System.out.println("[NGUtils] Loading Ban event");
        this.getServer().getPluginManager().registerEvents((Listener)new BanJoin(), (Plugin)this);
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
        this.getCommand("listchest").setExecutor((CommandExecutor)new ListChests());
        System.out.println("[NGUtils] Remove worldborder");
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)"nworldborder remove Assaut");
        this.getLogger().info("TabGrade active.");
        BanJoin.convoc.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID uuid = SanctionDB.getOfflineUUID(p.getName());
            if (!SanctionDB.isBanned(uuid)) continue;
            p.kickPlayer(ChatColor.RED + "Tu es banni !");
        }
        System.out.println("[NGUtils] Enabled!");
    }

    public void onDisable() {
        System.out.println("[NGUtil] Unloading all component, Good bye!");
        this.getLogger().info("TabGrade desactive.");
        try {
            SanctionDB.getConnection().close();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public static SanctionDB getSanctionDB() {
        return sanctionDB;
    }

    static {
        isDebug = false;
    }
}

