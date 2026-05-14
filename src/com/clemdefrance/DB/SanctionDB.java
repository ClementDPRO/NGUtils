/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.clemdefrance.DB;

import com.clemdefrance.cmd.sanction.BanInfo;
import com.clemdefrance.sanction.Sanction;
import com.clemdefrance.sanction.Type;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.bukkit.plugin.java.JavaPlugin;

public class SanctionDB {
    private static Connection connection;
    private final File dbFile;

    public SanctionDB(JavaPlugin plugin) {
        this.dbFile = new File(plugin.getDataFolder(), "sanctions.db");
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + this.dbFile.getAbsolutePath());
            this.createTable();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        Statement st = connection.createStatement();
        st.executeUpdate("CREATE TABLE IF NOT EXISTS sanctions (id INTEGER PRIMARY KEY AUTOINCREMENT,uuid TEXT NOT NULL,type TEXT NOT NULL,date TEXT NOT NULL,time INTEGER,reason TEXT NOT NULL,moderator TEXT NOT NULL);");
        st.close();
    }

    public static Connection getConnection() {
        return connection;
    }

    public static String formatDuration(long seconds) {
        if (seconds <= 0L) {
            return "Permanent";
        }
        if (seconds < 60L) {
            return seconds + " s";
        }
        if (seconds < 3600L) {
            return seconds / 60L + " m";
        }
        if (seconds < 86400L) {
            return seconds / 3600L + " h";
        }
        if (seconds < 2592000L) {
            return seconds / 86400L + " j";
        }
        if (seconds < 31536000L) {
            return seconds / 2592000L + " mo";
        }
        return seconds / 31536000L + " y";
    }

    public static boolean unmute(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE sanctions SET time = -1 WHERE id = (SELECT id FROM sanctions WHERE uuid = ? AND type = 'MUTE' ORDER BY id DESC LIMIT 1);");
            ps.setString(1, uuid.toString());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int addSanction(UUID uuid, Type type, String reason, String moderator, long timeSeconds) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO sanctions (uuid, type, date, time, reason, moderator) VALUES (?, ?, ?, ?, ?, ?);", 1);
            String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            ps.setString(1, uuid.toString());
            ps.setString(2, type.name());
            ps.setString(3, date);
            ps.setLong(4, timeSeconds);
            ps.setString(5, reason);
            ps.setString(6, moderator);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            int id = -1;
            if (keys.next()) {
                id = keys.getInt(1);
            }
            keys.close();
            ps.close();
            return id;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static List<Sanction> getSanctions(UUID uuid) {
        ArrayList<Sanction> list = new ArrayList<Sanction>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM sanctions WHERE uuid = ? ORDER BY id DESC;");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Sanction s = new Sanction(rs.getInt("id"), uuid, Type.valueOf(rs.getString("type")), rs.getString("reason"), rs.getString("moderator"), rs.getString("date"), rs.getLong("time"));
                list.add(s);
            }
            rs.close();
            ps.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean isMuted(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM sanctions WHERE uuid = ? AND type = 'MUTE' ORDER BY id DESC LIMIT 1;");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long time = rs.getLong("time");
                if (time == -1L) {
                    rs.close();
                    ps.close();
                    return false;
                }
                if (time == 0L) {
                    rs.close();
                    ps.close();
                    return true;
                }
                String dateStr = rs.getString("date");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date startDate = sdf.parse(dateStr);
                long startMillis = startDate.getTime();
                long endMillis = startMillis + time * 1000L;
                long now = System.currentTimeMillis();
                rs.close();
                ps.close();
                return now < endMillis;
            }
            rs.close();
            ps.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Sanction getSanctionById(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM sanctions WHERE id = ?;");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Sanction s = new Sanction(id, UUID.fromString(rs.getString("uuid")), Type.valueOf(rs.getString("type")), rs.getString("reason"), rs.getString("moderator"), rs.getString("date"), rs.getLong("time"));
                rs.close();
                ps.close();
                return s;
            }
            rs.close();
            ps.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteSanction(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM sanctions WHERE id = ?;");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getLastSanctionId() {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT id FROM sanctions ORDER BY id DESC LIMIT 1;");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                rs.close();
                ps.close();
                return id;
            }
            rs.close();
            ps.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static UUID getOfflineUUID(String name) {
        try {
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes("UTF-8"));
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean isBanned(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM sanctions WHERE uuid = ? AND type = 'BAN' ORDER BY id DESC LIMIT 1;");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long time = rs.getLong("time");
                if (time == -1L) {
                    return false;
                }
                if (time == 0L) {
                    return true;
                }
                String dateStr = rs.getString("date");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date startDate = sdf.parse(dateStr);
                long endMillis = startDate.getTime() + time * 1000L;
                return System.currentTimeMillis() < endMillis;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean unban(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE sanctions SET time = -1 WHERE id = (SELECT id FROM sanctions WHERE uuid = ? AND type = 'BAN' ORDER BY id DESC LIMIT 1);");
            ps.setString(1, uuid.toString());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static BanInfo getBanInfo(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM sanctions WHERE uuid = ? AND type = 'BAN' ORDER BY id DESC LIMIT 1;");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long time = rs.getLong("time");
                String reason = rs.getString("reason");
                String dateStr = rs.getString("date");
                if (time == -1L) {
                    return new BanInfo(false, null, 0L, null);
                }
                if (time == 0L) {
                    return new BanInfo(true, reason, -1L, dateStr);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date startDate = sdf.parse(dateStr);
                long remaining = startDate.getTime() + time * 1000L - System.currentTimeMillis();
                if (remaining <= 0L) {
                    return new BanInfo(false, null, 0L, null);
                }
                return new BanInfo(true, reason, remaining / 1000L, dateStr);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new BanInfo(false, null, 0L, null);
    }
}

