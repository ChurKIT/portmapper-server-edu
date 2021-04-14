package response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;

public class ResponseThread extends Thread {

    private Socket toClient;
    private Socket toTargetServer;

    public ResponseThread(Socket toClient, Socket toTargetServer) throws IOException {
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
        start();
    }


    public String responseFromTargetServer(HttpURLConnection connection){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (IOException e){
            throw new RuntimeException("ERROR: Couldn't get a response from the target server" + toTargetServer.getInetAddress() + ":" + toTargetServer.getPort());
        }
    }

    public boolean sentResponseToClient(String response){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(toClient.getOutputStream()));
            writer.write(response);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Couldn't send response to client");
        }
    }

    @Override
    public void run() {

    }
}
