package service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SocketService {

    private Map<UUID, Integer> uuidToPortMap = new HashMap<>();

    public SocketService() {
        initMap();
    }

    public Integer getPort(UUID uuid){
        return uuidToPortMap.get(uuid);
    }

    private void initMap(){
        uuidToPortMap.put(UUID.fromString("28748480-e5ea-4479-ba13-9f346772644d"), 8080);
        uuidToPortMap.put(UUID.fromString("28748480-e5ea-4479-ba13-9f346772644d"), 9090);
        uuidToPortMap.put(UUID.fromString("28748480-e5ea-4479-ba13-9f346772644d"), 7070);
        uuidToPortMap.put(UUID.fromString("28748480-e5ea-4479-ba13-9f346772644d"), 5464);
        uuidToPortMap.put(UUID.fromString("28748480-e5ea-4479-ba13-9f346772644d"), 4565);
    }

}
