package service;

import socketThreadPair.SocketThreadPair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public interface SocketService {

    Integer getPort(UUID uuid);

    void initMap();

    String getQueryFromClient(String request);

    String getUUIDFromClient(String request);

    void addSocketThreadPairToList(SocketThreadPair threadPair);

}
