package service.impl;

import service.ConnectService;

import java.io.*;
import java.net.Socket;

public class ConnectServiceImpl implements ConnectService {

    private static Socket clientSocket;
    private static BufferedReader reader;
    private String uuidToTargetServer;
    private String queryToTargetServer;
    private String request;
    private BufferedReader in;
    private BufferedWriter out;

    @Override
    public synchronized void connect() {
        try {
            clientSocket = new Socket("localhost", 6666);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e){
            throw new RuntimeException("ERROR: Couldn't to connect to server");
        }
    }

    @Override
    public synchronized void mapTo() {
        try {
            System.out.println("Write UUID Target Server");
            uuidToTargetServer = reader.readLine();
            out.write(uuidToTargetServer);
            out.newLine();
            out.flush();
        } catch (IOException e){
            throw new RuntimeException("ERROR: Undefined UUID");
        }
    }

    @Override
    public synchronized void query()
     {
         try {
             System.out.println("Write query to Target Server");
             queryToTargetServer = reader.readLine();
             out.write(queryToTargetServer);
             out.newLine();
             out.flush();
         } catch (IOException e){
             throw new RuntimeException("ERROR: Undefined query");
         }
    }

    @Override
    public synchronized String readResponse() {
        try {
            String response = in.readLine();
            return response;
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Couldn't get response");
        }
    }

//    @Override
//    public synchronized void sendRequest(){
//        request = uuidToTargetServer + ":" + queryToTargetServer;
//        try {
//            out.write(request);
//            out.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public synchronized void close(){
        try {
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Couldn't close socket");
        }
    }
}
