import org.apache.log4j.Logger;
import read.ReadClientThread;
import write.WriteClientThread;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class SocketTransmitter extends Thread {

    private static final Logger log = Logger.getLogger(SocketTransmitter.class);

    public ServerSocket serverSocket;
    public Socket toClient;
    public int listeningPort;

    public SocketTransmitter() {
        getProperties();
        //setDaemon(true);
        try {
            serverSocket = new ServerSocket(listeningPort, 10, InetAddress.getByName("localhost"));
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
            log.error("ОШИБКА:Файл свойств не найден или поврежден.");
            throw new RuntimeException();
        }
    }

    @Override
    public void run() {
        System.out.println("Server listening: " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());

        try {
            System.out.println("Waiting for connect...");
            toClient = serverSocket.accept();
            System.out.println("Connecting...");

            ReadClientThread read = new ReadClientThread(toClient);
            WriteClientThread write = new WriteClientThread(toClient);

            toClient.close();
            System.out.println("SocketTransmitter work end");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
