/*
 * Decompiled with CFR 0.151.
 */
package com.clemdefrance.DB;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayercountDB {
    private Connection connection;
    private final File databaseFile;

    public PlayercountDB(File dataFolder) {
        this.databaseFile = new File(dataFolder, "Playercount.db");
    }

    public void connect() {
        try {
            if (!this.databaseFile.getParentFile().exists()) {
                this.databaseFile.getParentFile().mkdirs();
            }
            if (!this.databaseFile.exists()) {
                this.databaseFile.createNewFile();
            }
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.databaseFile.getAbsolutePath());
            this.createtable();
        }
        catch (ClassNotFoundException e) {
            System.out.println("[NGUtils] Error until load PlayercountDB: SQlite not found " + e);
        }
        catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean createtable() {
        String sql = "CREATE TABLE IF NOT EXISTS playercount ( pick INTEGER, date TEXT);";
        try {
            Statement stmt = this.connection.createStatement();
            stmt.execute(sql);
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean add(int pik, String timestamp) {
        String sql = "INSERT INTO playercount(pick, date) VALUES(?, ?)";
        try (PreparedStatement pstmt = this.connection.prepareStatement(sql);){
            pstmt.setInt(1, pik);
            pstmt.setString(2, timestamp);
            pstmt.executeUpdate();
            boolean bl = true;
            return bl;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int read(String timestamp) {
        String sql = "SELECT pick FROM playercount WHERE date = ?";
        try (PreparedStatement pstmt = this.connection.prepareStatement(sql);){
            pstmt.setString(1, timestamp);
            try (ResultSet rs = pstmt.executeQuery();){
                if (!rs.next()) return -1;
                int n = rs.getInt("pick");
                return n;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean remove(String timestamp) {
        String sql = "DELETE FROM playercount WHERE date = ?";
        try (PreparedStatement pstmt = this.connection.prepareStatement(sql);){
            pstmt.setString(1, timestamp);
            pstmt.executeUpdate();
            boolean bl = true;
            return bl;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

