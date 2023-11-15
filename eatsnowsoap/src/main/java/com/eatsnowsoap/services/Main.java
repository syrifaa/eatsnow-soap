package com.eatsnowsoap.services;
import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        try {
            Endpoint.publish("http://0.0.0.0:8020/ws/review", new EatsnowService());
            System.out.println("Service is running at http://0.0.0.0:8020/ws/review");
        } catch (Exception e) {
            System.out.println("Something Wrong");
        }
    }
}