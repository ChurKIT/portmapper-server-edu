package service.impl;

import service.ConnectService;

import java.io.*;
import java.net.Socket;

public class ConnectServiceImpl implements ConnectService {

    private static Socket clientSocket;
    private static BufferedReader reader;
    private String uuidToTargetServer = "28748480-e5ea-4479-ba13-9f346772644d";
    private String queryToTargetServer;
    private String request;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean isDone = false;

    @Override
    public void connect() {
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
    public void mapTo() {
        try {
//            System.out.println("Write UUID Target Server");
//            uuidToTargetServer = reader.readLine();
            out.write(uuidToTargetServer);
            out.newLine();
            out.flush();
        } catch (IOException e){
            throw new RuntimeException("ERROR: Undefined UUID");
        }
    }

    @Override
    public void query()
     {
         queryToTargetServer = "";
         if (!clientSocket.isClosed()){
             try {
                 System.out.println("Write query to Target Server");
                 String line = reader.readLine();
                 while (!line.equals("DONE")){
                     queryToTargetServer += line;
                     queryToTargetServer += "\r\n";
                     line = reader.readLine();
                 }
                 out.write(queryToTargetServer);
                 out.newLine();
                 out.flush();
             } catch (IOException e) {
                 throw new RuntimeException("ERROR: Undefined query");
             }
         } else {
             isDone = true;
         }
    }

    public void sendRequest(){

    }

    @Override
    public String readResponse() {
        try {
            String line;
            String response = "";
            while ((line = in.readLine()) != null){
                response += line;
                response += "\r\n";
            }
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
    public void close(){
        try {
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Couldn't close socket");
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }
}
