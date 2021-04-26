import org.apache.log4j.Logger;
import poolThreads.PoolThreads;
import service.impl.SocketServiceImpl;
import socketThreadPair.SocketThreadPair;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.UUID;

public class SocketTransmitter {

    private static final Logger log = Logger.getLogger(SocketTransmitter.class);

    private SocketServiceImpl socketService;
    private ServerSocket serverSocket;
    private Socket toClient;
    private Socket toTargetServer;
    private volatile int listeningPort;

    public SocketTransmitter() {
        getProperties();
        try {
            serverSocket = new ServerSocket(listeningPort, 10, InetAddress.getByName("localhost"));
            socketService = new SocketServiceImpl();
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

    public void run() {
        while (true) {
            System.out.println("Server listening: " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
            toClient = connectionFromClient(serverSocket);
            try {
                String targetServerUUID = getUUIDFromClient(toClient);
                Integer targetServerPort = socketService.getPort(UUID.fromString(targetServerUUID));
                toTargetServer = connectToTargetServer(targetServerPort);
            } catch (NullPointerException e ){
                log.error("ERROR on " + toClient.getInetAddress() +  ": Nothing entered when prompted for UUID");
                continue; //затычка
            } catch (IllegalArgumentException e){
                log.error("ERROR on " + toClient.getInetAddress() + ": Invalid UUID");
                continue; //затычка
            }

            SocketThreadPair socketThreadPair = new SocketThreadPair(toClient, toTargetServer);
            socketService.addSocketThreadPairToList(socketThreadPair);
            PoolThreads.INSTANCE.executor.submit(socketThreadPair);
        }
    }

    public Socket connectionFromClient(ServerSocket serverSocket) {
        System.out.println("Waiting for connect...");
        Socket result = null;
        try {
            result = serverSocket.accept();
        } catch (IOException e) {
            log.error("ERROR : Failed to create client connection");
        }
        System.out.println("Connect to " + result.getInetAddress());
        return result;
    }

    public String getUUIDFromClient(Socket toClient){
        String uuid = null;
        try {
            BufferedReader reader = new BufferedReader( new InputStreamReader(toClient.getInputStream()));
            uuid = reader.readLine();
        } catch (IOException e) {
            log.error("ERROR on " + toClient.getInetAddress() + ": Invalid uuid");
        }
        return uuid;
    }

    public Socket connectToTargetServer(Integer port) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", port);
        } catch (IOException e){
            log.error("ERROR on " + toClient.getInetAddress() + ": Unable to connect to server with this port");
        }
            return socket;


//        try {
//            toTargetServer = (HttpURLConnection) new URL("http://localhost:" + port).openConnection();
//            toTargetServer.setRequestMethod("GET");
//            toTargetServer.setUseCaches(false);
//            return toTargetServer;
//        } catch (IOException e){
//            throw new RuntimeException("ERROR: Couldn't to connect to server");
//        }
//        String url = "http://localhost:" + port + "/";
//        HttpURLConnection connection = null;
//        try {
//            connection = (HttpURLConnection) new URL(url).openConnection();
//            return connection;
//        } catch (IOException e) {
//            log.error("ERROR: Can't connect to target server on port " + port);
//            throw new RuntimeException("ERROR: Can't connect to target server on port " + port);
//        }
    }
}
