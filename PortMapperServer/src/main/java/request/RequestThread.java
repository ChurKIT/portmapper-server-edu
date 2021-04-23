package request;

import java.io.*;
import java.net.Socket;

public class RequestThread extends Thread{

    private Socket toClient;
    private Socket toTargetServer;
    private BufferedReader inFromClient;
    private BufferedWriter outToTargetServer;
    private volatile boolean isDone;
    private volatile boolean clientExit;


    public RequestThread(Socket toClient, Socket toTargetServer) {
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
        isDone = false;
        clientExit = false;
    }


    public String queryFromClient() {
        try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
                String query = reader.readLine();
                toClient.shutdownInput();
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
            toTargetServer.shutdownOutput();
        } catch (IOException e) {
            //log.error("ERROR: Invalid request");
            throw new RuntimeException("ERROR: Invalid GET request to Target Server");
        }
    }

    @Override
    public void run() {

        String request = queryFromClient();
        queryToTargetServer(request);
        isDone = true;
    }


    public boolean isDone() {
        return isDone;
    }
}
