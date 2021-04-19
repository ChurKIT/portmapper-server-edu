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

    private volatile String request;

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
        uuidToPortMap.put(UUID.fromString("0c2a0553-dd70-4a38-85f9-115b9830df30"), 9090);
        uuidToPortMap.put(UUID.fromString("f1fa4e69-2313-4513-a96a-55caf206f86e"), 7070);
        uuidToPortMap.put(UUID.fromString("fcacd0b9-e4c1-40c7-9c8d-67bf356d5134"), 5464);
        uuidToPortMap.put(UUID.fromString("06dcc836-6c3f-4b28-ba2a-4259100d8d79"), 4565);
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
    public synchronized String getUUIDFromClient() {
        return getUUID(request);
    }

    @Override
    public synchronized String getRequestFromClient() {
        return getQuery(request);
    }

    public synchronized void getRequest(Socket toClient){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
            request = reader.readLine();
        } catch (IOException e) {
            log.error("ERROR: Invalid request");
            throw new RuntimeException("ERROR: Invalid request");
        }
    }

    private synchronized String getUUID (String request){
        String[] splitRequest = request.split("/");
        return splitRequest[0];
    }

    private synchronized String getQuery (String request){
        String[] splitRequest = request.split("/");
        return splitRequest[1];
    }


}
