package com.eatsnowsoap.services;

import com.sun.net.httpserver.HttpExchange;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.sql.*;
import java.text.SimpleDateFormat;



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

}