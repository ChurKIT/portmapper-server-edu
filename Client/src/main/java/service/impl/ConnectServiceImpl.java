package service.impl;

import service.ConnectService;

import java.io.*;
import java.net.Socket;

public class ConnectServiceImpl implements ConnectService {

    private static Socket clientSocket;
    private static BufferedReader reader;
    private static PrintWriter out;
    private static BufferedReader in;


    @Override
    public boolean connect() {
        try {
            clientSocket = new Socket("localhost", 6666);
            reader = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return true;
        } catch (IOException e){
            throw new RuntimeException("ERROR: Couldn't to connect to server");
        }
    }

    @Override
    public boolean mapTo() {
        try {
            System.out.println("Write UUID Target Server");
            String uuid = reader.readLine();
            out.write(uuid + "/");
            return true;
        } catch (IOException e){
            throw new RuntimeException("ERROR: Undefined UUID");
        }
    }

    @Override
    public void query()
     {
         try {
             System.out.println("Write query to Target Server");
             String query = reader.readLine();
             out.write(query);
         } catch (IOException e){
             throw new RuntimeException("ERROR: Undefined query");
         }
    }

    @Override
    public String readResponse() {
        try {
            String response = in.readLine();
            return response;
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Couldn't get response");
        }
    }
}
