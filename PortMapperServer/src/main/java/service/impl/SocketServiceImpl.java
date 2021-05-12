package service.impl;

import org.apache.log4j.Logger;
import service.SocketService;
import socketThreadPair.SocketThreadPair;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SocketServiceImpl implements SocketService {

    private static final Logger log = Logger.getLogger(SocketServiceImpl.class);

    private Map<UUID, Integer> uuidToPortMap = new HashMap<>();

    private List<SocketThreadPair> threadPairs = new ArrayList<>();

    public SocketServiceImpl() {
        initMap();
    }

    @Override
    public synchronized Integer getPort(UUID uuid){
        return uuidToPortMap.get(uuid);
    }

    @Override
    public void initMap(){
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("PortMapperServer/src/main/resources/portmapper.properties");
            properties.load(fis);
            uuidToPortMap = properties.entrySet().stream()
                    .filter((entry) -> {
                        try {
                            Integer.parseInt(String.valueOf(entry.getKey()));
                            return true;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toMap(
                        entry -> UUID.fromString(String.valueOf(entry.getValue())),
                        entry -> Integer.parseInt(String.valueOf(entry.getKey()))));
        } catch (IOException e){
            log.error("ERROR : Invalid initialization port:uuid map");
            throw new RuntimeException(e);
        }
//        uuidToPortMap.put(UUID.fromString("28748480-e5ea-4479-ba13-9f346772644d"), 8080);
//        uuidToPortMap.put(UUID.fromString("0c2a0553-dd70-4a38-85f9-115b9830df30"), 9090);
//        uuidToPortMap.put(UUID.fromString("f1fa4e69-2313-4513-a96a-55caf206f86e"), 7070);
//        uuidToPortMap.put(UUID.fromString("fcacd0b9-e4c1-40c7-9c8d-67bf356d5134"), 5464);
//        uuidToPortMap.put(UUID.fromString("06dcc836-6c3f-4b28-ba2a-4259100d8d79"), 4565);
    }


    @Override
    public synchronized String getUUIDFromClient(String request) {
        String[] splitRequest = request.split(":");
        return splitRequest[0];
    }

    @Override
    public synchronized void addSocketThreadPairToList(SocketThreadPair threadPair) {
        threadPairs.add(threadPair);
    }

    @Override
    public synchronized String getQueryFromClient(String request) {
        String[] splitRequest = request.split(":");
        return splitRequest[1];
    }




}
