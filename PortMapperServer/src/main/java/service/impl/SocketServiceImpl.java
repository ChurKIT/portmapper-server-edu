package service.impl;

import context.Context;
import org.apache.log4j.Logger;
import service.SocketService;
import socketThreadPair.SocketThreadPair;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SocketServiceImpl implements SocketService {

    private volatile static SocketServiceImpl instance;

    private SocketServiceImpl() {
        initMap();
    }

    public static SocketServiceImpl getInstance() {
        if (instance == null){
            synchronized (SocketServiceImpl.class) {
                if (instance == null) {
                    instance = new SocketServiceImpl();
                }
            }
        }
        return instance;
    }

    private static final Logger log = Logger.getLogger(SocketServiceImpl.class);

    private Map<UUID, Integer> uuidToPortMap = new HashMap<>();

    private volatile List<SocketThreadPair> threadPairs = new ArrayList<>();

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

    @Override
    public List<Context> getAllContexts() {
        List<Context> result = new ArrayList<>();
        for (SocketThreadPair socketThreadPair : threadPairs){
            result.add(socketThreadPair.getContext());
        }
        return result;
    }

    @Override
    public List<Context> getAllActiveContexts() {
        List<Context> result = new ArrayList<>();
        for (SocketThreadPair socketThreadPair : threadPairs){
            Context context = socketThreadPair.getContext();
            if (!context.isThreadPairIsFinished()) {
                result.add(context);
            }
        }
        return result;
    }
}
