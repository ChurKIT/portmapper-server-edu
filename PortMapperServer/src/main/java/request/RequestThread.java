package request;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;

public class RequestThread extends Thread{

    private Socket toClient;
    private Socket toTargetServer;
    private volatile boolean isDone;


    public RequestThread(Socket toClient, Socket toTargetServer) {
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
        isDone = false;
    }

    public String queryFromClient() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
            String query = reader.readLine();
            return query;
        } catch (IOException e) {
            //log.error("ERROR: Invalid request");
            throw new RuntimeException("ERROR: Invalid request");
        }
    }

    public void queryToTargetServer(String query) {
        String request = "GET /" + query + " HTTP/1.1\r\n" +
                    "Host: " + toTargetServer.getInetAddress().getHostName() + "\r\n\r\n";

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(toTargetServer.getOutputStream()));
            writer.write(request);
            writer.flush();
            //writer.close();
        } catch (IOException e) {
            //log.error("ERROR: Invalid request");
            throw new RuntimeException("ERROR: Invalid GET request to Target Server");
        }
    }

    @Override
    public void run() {
        String query = queryFromClient();
        queryToTargetServer(query);
        isDone = true;
    }


    public boolean isDone() {
        return isDone;
    }
}
