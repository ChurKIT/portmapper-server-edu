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
        initStreams();
    }

    private void initStreams(){
        try {
            inFromTargetServer = new BufferedReader(new InputStreamReader(toTargetServer.getInputStream()));
            outToClient = new BufferedWriter(new OutputStreamWriter(toClient.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String responseFromTargetServer(){
        try {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = inFromTargetServer.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (IOException e){
            throw new RuntimeException("ERROR: Invalid response from target server");
        }
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null){
//                response.append(line);
//            }
//            return response.toString();
//        } catch (IOException e){
//            throw new RuntimeException("ERROR: Couldn't get a response from the target server");
//        }
    }

    public void sentResponseToClient(String response){
        try {
            outToClient.write(response);
            outToClient.newLine();
            outToClient.flush();
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Couldn't send response to client");
        }
    }

    private void closeStreams(){
        try {
            inFromTargetServer.close();
            outToClient.close();
        } catch (IOException e) {
            e.printStackTrace();
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
