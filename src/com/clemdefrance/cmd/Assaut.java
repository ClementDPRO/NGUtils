/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.massivecraft.factions.entity.Faction
 *  com.massivecraft.factions.entity.FactionColl
 *  com.massivecraft.factions.entity.FactionColls
 *  com.massivecraft.factions.entity.UPlayer
 *  net.minecraft.server.v1_6_R3.NBTTagCompound
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 *  org.bukkit.scoreboard.Scoreboard
 */
package com.clemdefrance.cmd;

import com.clemdefrance.Enum.ASSAULTRANK;
import com.clemdefrance.Enum.AssaultMembre;
import com.clemdefrance.Main;
import com.clemdefrance.cmd.WorldborderManager;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Assaut
implements CommandExecutor {
    private static Assaut instance;
    public static boolean islaunch;
    private int currentPhase = 1;
    private int phaseTime = 480;
    private int taskID = -1;
    private int scoreDefense = 0;
    private int scoreAttaque = 0;
    private Location currentLoc = null;
    public static Faction facA;
    public static Faction facB;
    private Player starter;
    private int x;
    private int z;
    public static List<AssaultMembre> assautPlayer;
    private List<String> forbidenFaction = new ArrayList<String>();

    public Assaut() {
        instance = this;
        this.forbidenFaction.add("Factionless");
        this.forbidenFaction.add("WarZone");
        this.forbidenFaction.add("SafeZone");
    }

    public static Assaut getInstance() {
        return instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p;
        if (!(sender instanceof Player)) {
            sender.sendMessage("Commande r\u00e9serv\u00e9e aux joueurs");
            return true;
        }
        Player player = (Player)sender;
        if (args.length == 1 && args[0].equalsIgnoreCase("pass")) {
            if (this.taskID == -1) {
                player.sendMessage(ChatColor.RED + "Aucun assaut n'est en cours.");
                return true;
            }
            if (!player.isOp()) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission.");
                return true;
            }
            player.sendMessage(ChatColor.GREEN + "Passage forc\u00e9 \u00e0 la phase suivante...");
            this.nextPhase();
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /assaut <faction> ou /assaut pass");
            return true;
        }
        if (this.taskID != -1) {
            sender.sendMessage(ChatColor.RED + "Un assaut est d\u00e9j\u00e0 en cours !");
            return true;
        }
        facA = ((FactionColl)FactionColls.get().get((Object)"default")).getByName(args[0]);
        if (facA == null) {
            sender.sendMessage(ChatColor.RED + "Faction cible inconnue.");
            return true;
        }
        UPlayer uPlayer = UPlayer.get((Object)player);
        facB = uPlayer.getFaction();
        if (facB == null || !facB.isNormal()) {
            sender.sendMessage(ChatColor.RED + "Vous devez \u00eatre dans une faction.");
            return true;
        }
        if (facA.getName().equals(facB.getName())) {
            sender.sendMessage(ChatColor.RED + "Impossible d'attaquer votre propre faction.");
            return true;
        }
        if (!sender.isOp() && this.forbidenFaction.contains(facA.getName())) {
            sender.sendMessage(ChatColor.RED + "Impossible d'attaquer " + facA.getName());
            return true;
        }
        this.currentPhase = 1;
        this.phaseTime = 480;
        this.scoreDefense = 0;
        this.scoreAttaque = 0;
        this.currentLoc = null;
        this.starter = player;
        assautPlayer.clear();
        islaunch = true;
        for (UPlayer pls : facA.getUPlayers()) {
            p = Bukkit.getPlayer((String)pls.getName());
            if (p == null || !p.isOnline()) continue;
            assautPlayer.add(new AssaultMembre(pls.getName(), ASSAULTRANK.DEFENSE));
        }
        for (UPlayer pls : facB.getUPlayers()) {
            p = Bukkit.getPlayer((String)pls.getName());
            if (p == null || !p.isOnline()) continue;
            assautPlayer.add(new AssaultMembre(pls.getName(), ASSAULTRANK.ATTAQUE));
        }
        SimpleDateFormat heure = new SimpleDateFormat("HH:mm");
        heure.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)("&6[*banner id=\"assault\"] &c&lD\u00e9but d'assaut \u00e0 &c" + heure.format(new Date()) + " &4" + facA.getName() + " &c\u2694 &a" + facB.getName())));
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)Main.getInstance(), new Runnable(){

            @Override
            public void run() {
                if (Assaut.this.currentLoc != null) {
                    int attInZone = 0;
                    int defInZone = 0;
                    List<Player> inBorder = Assaut.this.getPlayersInBorder(Assaut.this.currentLoc);
                    for (Player p : inBorder) {
                        for (AssaultMembre m : assautPlayer) {
                            if (!m.pseudo.equalsIgnoreCase(p.getName())) continue;
                            if (m.role == ASSAULTRANK.ATTAQUE) {
                                ++attInZone;
                                continue;
                            }
                            if (m.role != ASSAULTRANK.DEFENSE) continue;
                            ++defInZone;
                        }
                    }
                    if (attInZone > defInZone) {
                        Assaut.this.scoreAttaque++;
                    } else if (defInZone > attInZone) {
                        Assaut.this.scoreDefense++;
                    }
                }
                if (Assaut.this.phaseTime <= 0) {
                    Assaut.this.nextPhase();
                    return;
                }
                for (AssaultMembre m : assautPlayer) {
                    Player p = Bukkit.getPlayer((String)m.pseudo);
                    if (p == null || !p.isOnline()) continue;
                    Assaut.this.updateAssautScoreboard(p, facA.getName(), facB.getName(), Assaut.this.currentPhase, Assaut.this.phaseTime, Assaut.this.scoreDefense, Assaut.this.scoreAttaque);
                }
                Assaut.this.phaseTime--;
            }
        }, 20L, 20L);
        return true;
    }

    public void nextPhase() {
        ++this.currentPhase;
        if (this.currentPhase > 5) {
            this.stopAssaut("Fin du temps", facA.getName(), facB.getName());
            return;
        }
        this.phaseTime = 300;
        Bukkit.broadcastMessage((String)(ChatColor.GOLD + "\u27a1 Passage \u00e0 la phase " + this.currentPhase + " !"));
        for (AssaultMembre as : assautPlayer) {
            Player p = Bukkit.getPlayer((String)as.pseudo);
            if (p == null || !p.isOnline()) continue;
            WorldborderManager.removeBorder(p);
        }
        this.currentLoc = null;
        if (this.currentPhase == 2 || this.currentPhase == 4 || this.currentPhase == 5) {
            Player p;
            this.x = ThreadLocalRandom.current().nextInt(640, 937);
            this.z = ThreadLocalRandom.current().nextInt(-539, -279);
            World world = this.starter.getWorld();
            for (int tries = 0; world.getBlockAt(this.x, 4, this.z).getType() != Material.AIR && tries < 200; ++tries) {
                ++this.x;
                if (this.x <= 937) continue;
                this.x = 640;
            }
            this.currentLoc = new Location(world, (double)(this.x + 8), 4.0, (double)(this.z + 8));
            for (AssaultMembre m : assautPlayer) {
                p = Bukkit.getPlayer((String)m.pseudo);
                if (p == null) continue;
                p.sendMessage("[*banner id=\"assault\"]" + ChatColor.RED + " Une zone de cap est apparue !");
            }
            for (AssaultMembre as : assautPlayer) {
                p = Bukkit.getPlayer((String)as.pseudo);
                if (p == null || !p.isOnline()) continue;
                NBTTagCompound tag = WorldborderManager.createBorderNBT(this.x, 4, this.z, 16, 16, 1.0f);
                WorldborderManager.sendBorderPacket(p, tag);
            }
        }
    }

    public void stopAssaut(String raison, String fA, String fB) {
        islaunch = false;
        if (this.taskID != -1) {
            islaunch = false;
            Bukkit.getScheduler().cancelTask(this.taskID);
            this.taskID = -1;
            for (AssaultMembre as : assautPlayer) {
                Player p = Bukkit.getPlayer((String)as.pseudo);
                if (p == null || !p.isOnline()) continue;
                WorldborderManager.removeBorder(p);
            }
            String win = this.scoreAttaque > this.scoreDefense ? fB : fA;
            Bukkit.broadcastMessage((String)(ChatColor.RED + "Fin de l'assaut : Le gagnant est " + ChatColor.GREEN + win));
            for (AssaultMembre m : assautPlayer) {
                Player p = Bukkit.getPlayer((String)m.pseudo);
                if (p == null) continue;
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
        }
    }

    public void updateAssautScoreboard(Player player, String paysA, String paysB, int phase, int temps, int scoreA, int scoreB) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("assaut", "dummy");
        obj.setDisplayName(ChatColor.RED + "\u2694 ASSAUT \u2694");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        String tempsFormat = String.format("%02d:%02d", temps / 60, temps % 60);
        this.setScore(obj, "\u00a7cDef: \u00a7f" + paysA, 10);
        this.setScore(obj, "\u00a7aAtt: \u00a7f" + paysB, 9);
        this.setScore(obj, "\u00a78--------------", 8);
        this.setScore(obj, "\u00a7ePhase: \u00a76" + phase + "/5", 5);
        this.setScore(obj, "\u00a7eTemps: \u00a7f" + tempsFormat, 4);
        this.setScore(obj, "\u00a78--------------", 3);
        this.setScore(obj, "\u00a7cPoints Def: \u00a7f" + scoreA, 2);
        this.setScore(obj, "\u00a7aPoints Att: \u00a7f" + scoreB, 1);
        player.setScoreboard(board);
    }

    private void setScore(Objective obj, String text, int score) {
        String row = text.length() > 16 ? text.substring(0, 16) : text;
        obj.getScore(Bukkit.getOfflinePlayer((String)row)).setScore(score);
    }

    public List<Player> getPlayersInBorder(Location center) {
        ArrayList<Player> inside = new ArrayList<Player>();
        if (center == null) {
            return inside;
        }
        double half = 8.0;
        for (AssaultMembre m : assautPlayer) {
            Location pl;
            Player p = Bukkit.getPlayer((String)m.pseudo);
            if (p == null || !p.isOnline() || !p.getWorld().equals(center.getWorld()) || !(Math.abs((pl = p.getLocation()).getX() - center.getX()) <= half) || !(Math.abs(pl.getZ() - center.getZ()) <= half)) continue;
            inside.add(p);
        }
        return inside;
    }

    static {
        islaunch = false;
        assautPlayer = new ArrayList<AssaultMembre>();
    }
}

