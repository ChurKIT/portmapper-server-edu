package service;

import context.Context;
import socketThreadPair.SocketThreadPair;

import java.util.List;
import java.util.UUID;

public interface SocketService {

    Integer getPort(UUID uuid);

    void initMap();

    String getQueryFromClient(String request);

    String getUUIDFromClient(String request);

    void addSocketThreadPairToList(SocketThreadPair threadPair);

    List<Context> getAllContexts();

    List<Context> getAllActiveContexts();

}
