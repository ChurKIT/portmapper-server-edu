package response;

import java.io.*;
import java.net.Socket;

public class ResponseThread extends Thread {

    private Socket toClient;
    private Socket toTargetServer;
    private BufferedReader inFromTargetServer;
    private BufferedWriter outToClient;
    private volatile boolean isDone;

    public ResponseThread(Socket toClient, Socket toTargetServer){
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
        isDone = false;
    }

    public String responseFromTargetServer(){
        StringBuilder response = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(toTargetServer.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            toTargetServer.shutdownInput();
            return response.toString();
        } catch (IOException e){
            throw new RuntimeException("ERROR: Couldn't get a response from the target server");
        }
    }

    public void sentResponseToClient(String response){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(toClient.getOutputStream()));
            writer.write(response);
            writer.newLine();
            writer.flush();
            toClient.shutdownOutput();
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Couldn't send response to client");
        }
    }

    public boolean isDone(){
        return isDone;
    }

    @Override
    public void run() {
        String response = responseFromTargetServer();
        sentResponseToClient(response);
        isDone = true;
    }
}
