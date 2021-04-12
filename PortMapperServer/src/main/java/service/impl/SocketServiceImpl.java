package service.impl;

import org.apache.log4j.Logger;
import service.SocketService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SocketServiceImpl implements SocketService {

    private static final Logger log = Logger.getLogger(SocketServiceImpl.class);

    private Map<UUID, Integer> uuidToPortMap = new HashMap<>();

    public SocketServiceImpl() {
        initMap();
    }

    @Override
    public Integer getPort(UUID uuid){
        return uuidToPortMap.get(uuid);
    }

    @Override
    public void initMap(){
        uuidToPortMap.put(UUID.fromString("28748480-e5ea-4479-ba13-9f346772644d"), 8080);
        uuidToPortMap.put(UUID.fromString("28748480-e5ea-4479-ba13-9f346772644d"), 9090);
        uuidToPortMap.put(UUID.fromString("28748480-e5ea-4479-ba13-9f346772644d"), 7070);
        uuidToPortMap.put(UUID.fromString("28748480-e5ea-4479-ba13-9f346772644d"), 5464);
        uuidToPortMap.put(UUID.fromString("28748480-e5ea-4479-ba13-9f346772644d"), 4565);
    }

    @Override
    public synchronized Socket connectionFromClient(ServerSocket serverSocket) throws IOException {
        System.out.println("Waiting for connect...");
        Socket result = serverSocket.accept();
        System.out.println("Connect to " + result.getInetAddress());
        return result;
    }


    @Override
    public Socket connectToTargetServer(Integer port) {
        try {
            return new Socket("localhost", port);
        } catch (IOException e) {
            log.error("ERROR: Can't connect to target server on port " + port);
            throw new RuntimeException("ERROR: Can't connect to target server on port " + port);
        }
    }

    @Override
    public synchronized String getUUIDFromClient(Socket toClient) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
            return reader.readLine();
        } catch (IOException e){
            log.error("ERROR: Invalid UUID");
            throw new RuntimeException("ERROR: Invalid UUID");
        }
    }




}
