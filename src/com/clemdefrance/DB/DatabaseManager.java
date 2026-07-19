/*
 * Decompiled with CFR 0.151.
 */
package com.clemdefrance.DB;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() {
        this.setupDatabase();
    }

    public void setupDatabase() {
        try {
            File dbFile = new File("ngutils/NGUtils/whitelist.db");
            if (dbFile.getParentFile() != null && !dbFile.getParentFile().exists()) {
                dbFile.getParentFile().mkdirs();
                System.out.println("[NGUtil] Dossiers cr\u00e9\u00e9s avec succ\u00e8s.");
            }
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getPath());
            Statement statement = this.connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS whitelist (name TEXT PRIMARY KEY)");
            statement.close();
            System.out.println("[NGUtil] Connexion \u00e0 whitelist.db r\u00e9ussie.");
        }
        catch (Exception e) {
            System.err.println("[NGUtil] Erreur lors de la cr\u00e9ation de la base de donn\u00e9es !");
            e.printStackTrace();
        }
    }

    public void addNAME(String name) {
        String sql = "INSERT OR IGNORE INTO whitelist(name) VALUES(?)";
        try (PreparedStatement pstmt = this.connection.prepareStatement(sql);){
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(String name) {
        String sql = "DELETE FROM whitelist WHERE name = ?";
        try (PreparedStatement pstmt = this.connection.prepareStatement(sql);){
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getName() {
        ArrayList<String> names = new ArrayList<String>();
        String sql = "SELECT name FROM whitelist";
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);){
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }
}

