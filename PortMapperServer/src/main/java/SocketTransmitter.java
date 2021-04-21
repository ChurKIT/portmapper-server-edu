import socketThreadPair.SocketThreadPair;
import org.apache.log4j.Logger;
import service.impl.SocketServiceImpl;

import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.UUID;

public class SocketTransmitter extends Thread {

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
        while (true) {
            toClient = connectionFromClient(serverSocket);
            String targetServerUUID = getUUIDFromClient(toClient);
            Integer targetServerPort = socketService.getPort(UUID.fromString(targetServerUUID));
            toTargetServer = connectToTargetServer(targetServerPort);

            SocketThreadPair socketThreadPair = new SocketThreadPair(toClient, toTargetServer);
            socketService.addSocketThreadPairToList(socketThreadPair);
            socketThreadPair.start();
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
        try {
            BufferedReader reader = new BufferedReader( new InputStreamReader(toClient.getInputStream()));
            String uuid = reader.readLine();
            //reader.close();
            return uuid;
        } catch (IOException e) {
            log.error("ERROR: Invalid uuid");
            throw new RuntimeException("ERROR: Invalid uuid");
        }
    }

    public Socket connectToTargetServer(Integer port) {
        try {
            Socket socket = new Socket("localhost", port);
            return socket;
        } catch (IOException e){
            throw new RuntimeException("ERROR: Couldn't to connect to server");
        }


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
