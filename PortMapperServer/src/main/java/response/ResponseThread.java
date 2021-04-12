package response;

import java.io.*;
import java.net.Socket;

public class ResponseThread extends Thread {

    private Socket toClient;
    private Socket toTargetServer;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ResponseThread(Socket toClient, Socket toTargetServer) throws IOException {
        this.toTargetServer = toTargetServer;
        this.toClient = toClient;
        writer = new BufferedWriter(new OutputStreamWriter(toClient.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(toTargetServer.getInputStream()));
        start();
    }


    public String responseFromTargetServer(){
        return null;
    }

    public boolean sentResponseToClient(String response){
        return false;
    }

    @Override
    public void run() {

    }
}
