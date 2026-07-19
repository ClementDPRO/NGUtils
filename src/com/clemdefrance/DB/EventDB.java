/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.clemdefrance.DB;

import com.clemdefrance.cmd.Event.EventData;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

public class EventDB {
    private Connection connection;
    private final File dbFile;

    public EventDB(JavaPlugin plugin) {
        this.dbFile = new File(plugin.getDataFolder(), "events.db");
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.dbFile.getAbsolutePath());
            this.createTable();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        try (Statement st = this.connection.createStatement();){
            st.executeUpdate("CREATE TABLE IF NOT EXISTS events (name TEXT PRIMARY KEY, x REAL NOT NULL, y REAL NOT NULL, z REAL NOT NULL, staff_x REAL NOT NULL, staff_y REAL NOT NULL, staff_z REAL NOT NULL,world TEXT NOT NULL);");
        }
    }

    public boolean createEvent(EventData event) {
        String query = "INSERT OR REPLACE INTO events(name, x, y, z, staff_x, staff_y, staff_z, world) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query);){
            ps.setString(1, event.getName().toLowerCase());
            ps.setDouble(2, event.getX());
            ps.setDouble(3, event.getY());
            ps.setDouble(4, event.getZ());
            ps.setDouble(5, event.getStaffX());
            ps.setDouble(6, event.getStaffY());
            ps.setDouble(7, event.getStaffZ());
            ps.setString(8, event.getWorld());
            ps.executeUpdate();
            boolean bl = true;
            return bl;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public EventData getEvent(String name) {
        String query = "SELECT * FROM events WHERE name = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);){
            ps.setString(1, name.toLowerCase());
            try (ResultSet rs = ps.executeQuery();){
                if (!rs.next()) return null;
                EventData eventData = new EventData(rs.getString("name"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getDouble("staff_x"), rs.getDouble("staff_y"), rs.getDouble("staff_z"), rs.getString("world"));
                return eventData;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EventData> getAllEvents() {
        ArrayList<EventData> list = new ArrayList<EventData>();
        String query = "SELECT * FROM events;";
        try (Statement st = this.connection.createStatement();
             ResultSet rs = st.executeQuery(query);){
            while (rs.next()) {
                list.add(new EventData(rs.getString("name"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getDouble("staff_x"), rs.getDouble("staff_y"), rs.getDouble("staff_z"), rs.getString("world")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteEvent(String name) {
        String query = "DELETE FROM events WHERE name = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);){
            ps.setString(1, name.toLowerCase());
            int rowsAffected = ps.executeUpdate();
            boolean bl = rowsAffected > 0;
            return bl;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

