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
    public String addReview(String content, Float rating, Integer id_user, String name_user, String profile_img, Integer restaurant_id) {
        if (!isKeyValid()) {
            String message = "API Key tidak valid";
            return message;
        }
        Database db = new Database();
        Connection connection = db.getConnection();
        try {
            if (connection != null) {
                String query = "INSERT INTO review (content, rating, id_user, name_user, profile_img, id_restaurant) VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement preparedQueryStatement = connection.prepareStatement(query)) {
                    preparedQueryStatement.setString(1, content);
                    preparedQueryStatement.setFloat(2, rating);
                    preparedQueryStatement.setInt(3, id_user);
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
}