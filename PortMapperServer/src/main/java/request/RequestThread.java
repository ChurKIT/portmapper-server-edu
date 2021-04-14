package request;

import service.SocketService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class RequestThread extends Thread{

    private SocketService service;


    public RequestThread(SocketService socketService) throws IOException {
        this.service = socketService;
        start();
    }


    public String requestFromClient() {
        String request = service.getRequestFromClient();
        return request;
    }

    public HttpURLConnection requestToTargetServer(String request) {
        String url = "http://localhost:8080/" + request;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            return connection;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


    @Override
    public void run() {

    }
}
