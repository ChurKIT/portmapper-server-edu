package request;

import context.Context;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestThread implements Runnable{

    private static final Logger log = Logger.getLogger(RequestThread.class);

    private final Socket toClient;
    private final Socket toTargetServer;
    private Context context;


    public RequestThread(Socket toClient, Socket toTargetServer, Context context) {
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
        this.context = context;
    }


    public String queryFromClient() {
        String query = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
            String line = reader.readLine();
            while (!line.equals("")){
                query += line;
                query += "\r\n";
                line = reader.readLine();
            }
            query += "\r\n";
            toClient.shutdownInput();
            context.countRequestBytes(query.getBytes(StandardCharsets.UTF_8).length);
            context.setRequest(query);
        } catch (IOException e) {
            log.error("ERROR: Invalid request from Client");
        }
                return query;
    }

    public void queryToTargetServer(String query) {
        context.countRequestBytes(query.getBytes().length);
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(toTargetServer.getOutputStream()));
            writer.write(query);
            writer.newLine();
            writer.flush();
            toTargetServer.shutdownOutput();
        } catch (IOException e) {
            log.error("ERROR: Invalid GET request to Target Server");
        }
    }

    @Override
    public void run() {
        String request = queryFromClient();
        queryToTargetServer(request);
    }

}
