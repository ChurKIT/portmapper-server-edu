package response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;

public class ResponseThread extends Thread {

    private volatile Socket toClient;
    private volatile Socket toTargetServer;

    public ResponseThread(Socket toClient, Socket toTargetServer){
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
    }

    public String responseFromTargetServer(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(toTargetServer.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
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

    public synchronized void sentResponseToClient(String response){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(toClient.getOutputStream()));
            writer.write(response);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Couldn't send response to client");
        }
    }

    @Override
    public void run() {
        String response = responseFromTargetServer();
        sentResponseToClient(response);
    }
}
