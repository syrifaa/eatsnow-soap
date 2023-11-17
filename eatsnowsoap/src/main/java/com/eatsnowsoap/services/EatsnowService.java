package com.eatsnowsoap.services;

import com.sun.net.httpserver.HttpExchange;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.sql.*;
import java.text.SimpleDateFormat;

import com.eatsnowsoap.core.Database;

@WebService
public class EatsnowService {
    @Resource
    private WebServiceContext wsContext;

    public void log(String message){
        MessageContext messageContext = wsContext.getMessageContext();
        HttpExchange httpExchange = (HttpExchange) messageContext.get("com.sun.xml.ws.http.exchange");
        String ipAddress = httpExchange.getRemoteAddress().getAddress().getHostAddress();
        String endpoint = httpExchange.getRequestURI().toString();
        String APIKey = httpExchange.getRequestHeaders().getFirst("X-API-KEY");
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp date = new Timestamp(System.currentTimeMillis());
        String timestamp = (String) timeFormat.format(date);

        String logMessage = "API Key:" + APIKey + "| message: " + message;
        
        Log log = new Log();
        log.addLog(logMessage, ipAddress, endpoint, timestamp);
    }

    public boolean isKeyValid(){
        MessageContext messageContext = wsContext.getMessageContext();
        HttpExchange httpExchange = (HttpExchange) messageContext.get("com.sun.xml.ws.http.exchange");

        String APIKey = httpExchange.getRequestHeaders().getFirst("X-API-KEY");
        String[] APIValid = {"eatsnow-rest-service", "eatsnow-app-service"};
        if (APIKey.equals(APIValid[0]) || APIKey.equals(APIValid[1])) {
            return true;
        } else {
            return false;
        }
    }

    @WebMethod
    public String addReview(String content, Float rating, String email, String name_user, String profile_img, Integer restaurant_id) {
        if (!isKeyValid()) {
            String message = "API Key tidak valid";
            return message;
        }
        Database db = new Database();
        Connection connection = db.getConnection();
        try {
            if (connection != null) {
                String query = "INSERT INTO review (content, rating, email, name_user, profile_img, id_restaurant) VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement preparedQueryStatement = connection.prepareStatement(query)) {
                    preparedQueryStatement.setString(1, content);
                    preparedQueryStatement.setFloat(2, rating);
                    preparedQueryStatement.setString(3, email);
                    preparedQueryStatement.setString(4, name_user);
                    preparedQueryStatement.setString(5, profile_img);
                    preparedQueryStatement.setInt(6, restaurant_id);

                    preparedQueryStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String message = "Kesalahan saat menambahkan review : " + e.getMessage();
            log(message);
            return message;
        } finally {
            db.closeConnection(connection);
        }
        String message = "Berhasil menambahkan review";
        log(message);
        return message;
    }

    @WebMethod
    public String getReview(Integer id_restaurant) {
        if (!isKeyValid()) {
            String message = "API Key tidak valid";
            return message;
        }
        String message;
        Database db = new Database();
        Connection connection = db.getConnection();
        try {
            if (connection != null) {
                String query = "SELECT * FROM review WHERE id_restaurant = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, id_restaurant);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    Boolean hasil = false;
                    message = "{\"hasil\": [";

                    while (resultSet.next()) {
                        message += "{\"content\": \"" + resultSet.getString("content") + "\", \"rating\": " + resultSet.getString("rating") + ", \"email\": \"" + resultSet.getString("email") + "\", \"name_user\": \"" + resultSet.getString("name_user") + "\", \"profile_img\": \"" + resultSet.getString("profile_img") + "\", \"id_restaurant\": " + resultSet.getString("id_restaurant") + "},";
                        hasil = true;
                    }
                    message = message.substring(0, message.length() - 1);
                    message += "]}";

                    if (!hasil) {
                        log("Berhasil mengambil review, data hasil kosong");
                        message = "{\"hasil\": []}";
                    } else {
                        log("Berhasil mengambil review");
                    }
                    return message;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message = "Kesalahan saat mengambil review : " + e.getMessage();
            log(message);
            return message;
        }
        db.closeConnection(connection);
        return "Database tidak dapat diakses";
    }

    @WebMethod
    public String getUserReview(String email){
        if (!isKeyValid()) {
            String message = "API Key tidak valid";
            return message;
        }
        String message;
        Database db = new Database();
        Connection connection = db.getConnection();
        try {
            if (connection != null) {
                String query = "SELECT * FROM review WHERE email = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    Boolean hasil = false;
                    message = "{\"hasil\": [";

                    while (resultSet.next()) {
                        message += "{\"content\": \"" + resultSet.getString("content") + "\", \"rating\": " + resultSet.getString("rating") + ", \"email\": \"" + resultSet.getString("email") + "\", \"name_user\": \"" + resultSet.getString("name_user") + "\", \"profile_img\": \"" + resultSet.getString("profile_img") + "\", \"id_restaurant\": " + resultSet.getString("id_restaurant") + "},";
                        hasil = true;
                    }
                    message = message.substring(0, message.length() - 1);
                    message += "]}";

                    if (!hasil) {
                        log("Berhasil mengambil review, data hasil kosong");
                        message = "{\"hasil\": []}";
                    } else {
                        log("Berhasil mengambil review");
                    }
                    return message;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message = "Kesalahan saat mengambil review : " + e.getMessage();
            log(message);
            return message;
        }
        db.closeConnection(connection);
        return "Database tidak dapat diakses";

    }

    @WebMethod
    public String updateReview(Integer review_id, String content, Float rating){
        if(!isKeyValid()){
            String message = "API Key tidak valid";
            return message;
        }
        Database db = new Database();
        Connection connection = db.getConnection();
        try {
            if (connection != null) {
                String query = "UPDATE review SET content = ?, rating = ? WHERE review_id = ?";
                // String query = "INSERT INTO review (content, rating, email, name_user, profile_img, id_restaurant) VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement preparedQueryStatement = connection.prepareStatement(query)) {
                    preparedQueryStatement.setString(1, content);
                    preparedQueryStatement.setFloat(2, rating);
                    preparedQueryStatement.setInt(3, review_id);

                    preparedQueryStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String message = "Kesalahan saat menambahkan review : " + e.getMessage();
            log(message);
            return message;
        } finally {
            db.closeConnection(connection);
        }
        String message = "Berhasil mengubah review dengan id" + review_id;
        log(message);
        return message;
    }

}