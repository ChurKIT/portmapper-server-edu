package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public interface SocketService {

    public Integer getPort(UUID uuid);

    public void initMap();

    public Socket connectionFromClient(ServerSocket serverSocket) throws IOException;

    public Socket connectToTargetServer(Integer port);

    public String getUUIDFromClient(Socket toClient) throws IOException;

}
