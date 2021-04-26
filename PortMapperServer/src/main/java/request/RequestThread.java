package request;

import context.Context;
import listener.ThreadListener;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestThread implements Runnable{

    private static final Logger log = Logger.getLogger(RequestThread.class);

    private Socket toClient;
    private Socket toTargetServer;
    private boolean isDone;
    private boolean clientExit;
    private ThreadListener listener;
    private Context context;


    public RequestThread(Socket toClient, Socket toTargetServer, Context context) {
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
        this.context = context;
        isDone = false;
        clientExit = false;
    }


    public String queryFromClient() {
        String query = "";
        try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
                query = reader.readLine();
                toClient.shutdownInput();
                context.countRequestBytes(query.getBytes(StandardCharsets.UTF_8).length);
        } catch (IOException e) {
            log.error("ERROR: Invalid request from Client");
        }
                return query;
    }

    public void queryToTargetServer(String query) {
        String request = "GET /" + query + " HTTP/1.1\r\n" +
                    "Host: " + toTargetServer.getInetAddress().getHostName() + "\r\n\r\n";
        context.countRequestBytes(request.getBytes().length);
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(toTargetServer.getOutputStream()));
            writer.write(request);
            writer.flush();
            toTargetServer.shutdownOutput();
        } catch (IOException e) {
            log.error("ERROR: Invalid GET request to Target Server");
        }
    }

    public boolean isDone() {
        return isDone;
    }

    public void setListener(ThreadListener listener){
        this.listener = listener;
    }

    @Override
    public void run() {
        String request = queryFromClient();
        queryToTargetServer(request);
        listener.accept();
        isDone = true;
    }
}
