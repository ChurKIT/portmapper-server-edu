package request;

import java.io.*;
import java.net.Socket;

public class RequestThread extends Thread{

    private Socket toClient;
    private Socket toTargetServer;
    private BufferedReader reader;
    private BufferedWriter writer;

    public RequestThread(Socket toClient, Socket toTargetServer) throws IOException {
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
        reader = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(toTargetServer.getOutputStream()));
        start();
    }


    public String requestFromClient(){
        return null;
    }

    public boolean requestToTargetServer(String request){
        return false;
    }


    @Override
    public void run() {

    }
}
