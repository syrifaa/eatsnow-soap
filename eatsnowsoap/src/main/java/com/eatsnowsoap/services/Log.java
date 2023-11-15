package com.eatsnowsoap.services;

import com.eatsnowsoap.core.Database;

import java.sql.Connection;

public class Log extends Database {
    private Connection conn;

    public Log(){
        super();
    }

    public String addLog(String desc, String ip, String endpoint, String timestamp){
        this.conn = super.getConnection();

        try {
            if (this.conn != null) {
                String query = "INSERT INTO logging (req_desc, ip, endpoint, timestamp) VALUES (?, ?, ?, ?)";

                try (java.sql.PreparedStatement preparedStatement = this.conn.prepareStatement(query)) {
                    preparedStatement.setString(1, desc);
                    preparedStatement.setString(2, ip);
                    preparedStatement.setString(3, endpoint);
                    preparedStatement.setString(4, timestamp);

                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Gagal menambahkan log";
        } finally {
            super.closeConnection(this.conn);
        }

        return "Berhasil menambahkan log";
    }
}