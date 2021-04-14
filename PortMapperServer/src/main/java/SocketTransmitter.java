import org.apache.log4j.Logger;
import request.RequestThread;
import service.impl.SocketServiceImpl;
import response.ResponseThread;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.UUID;

public class SocketTransmitter extends Thread {

    private static final Logger log = Logger.getLogger(SocketTransmitter.class);

    private SocketServiceImpl socketService;
    private ServerSocket serverSocket;
    private Socket toClient;
    private Socket toTargetServer;
    private int listeningPort;

    public SocketTransmitter() {
        getProperties();
        //setDaemon(true);
        try {
            serverSocket = new ServerSocket(listeningPort, 10, InetAddress.getByName("localhost"));
            socketService = new SocketServiceImpl();
            start();
        } catch (IOException t) {
            t.printStackTrace();
        }
    }

    private void getProperties(){
        try {
            FileInputStream fis = new FileInputStream("PortMapperServer/src/main/resources/portmapper.properties");
            Properties properties = new Properties();
            properties.load(fis);
            listeningPort = Integer.parseInt(properties.getProperty("listen.port"));
        } catch (IOException e) {
            log.error("ERROR: Property file not found or corrupted.");
            throw new RuntimeException();
        }
    }

    @Override
    public void run() {
        System.out.println("Server listening: " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());

        try {
            toClient = socketService.connectionFromClient(serverSocket);
            socketService.getRequest(toClient);
            String targetServerUUID = socketService.getUUIDFromClient();
            Integer targetServerPort = socketService.getPort(UUID.fromString(targetServerUUID));
            toTargetServer = socketService.connectToTargetServer(targetServerPort);

            RequestThread requestThread = new RequestThread(socketService);
            ResponseThread responseThread = new ResponseThread(toClient, toTargetServer);

            //todo wrap in a loop with check and exit on request
            HttpURLConnection connection = requestThread.requestToTargetServer(requestThread.requestFromClient());
            responseThread.sentResponseToClient(responseThread.responseFromTargetServer(connection));


            toClient.close();
            toTargetServer.close();
            System.out.println("SocketTransmitter work end");
        } catch (IOException e) {
            log.error("FATAL ERROR IN SocketTransmitter Thread");
            throw new RuntimeException("FATAL ERROR IN SocketTransmitter Thread");
        }

    }
}
