package request;

import java.io.*;
import java.net.Socket;

public class RequestThread extends Thread{

    private Socket toClient;
    private Socket toTargetServer;
    private BufferedReader inFromClient;
    private BufferedWriter outToTargetServer;
    private volatile boolean isDone;


    public RequestThread(Socket toClient, Socket toTargetServer) {
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
        initStreams();
        isDone = false;
    }

    private void initStreams(){
        try {
            this.inFromClient = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
            this.outToTargetServer = new BufferedWriter(new OutputStreamWriter(toTargetServer.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String queryFromClient() {
        try {
            String query = inFromClient.readLine();
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
            outToTargetServer.write(request);
            outToTargetServer.newLine();
            outToTargetServer.flush();
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
