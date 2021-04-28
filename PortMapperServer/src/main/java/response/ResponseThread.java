package response;

import context.Context;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ResponseThread implements Runnable {

    private static final Logger log = Logger.getLogger(ResponseThread.class);

    private final Socket toClient;
    private final Socket toTargetServer;
    private boolean isDone;
    private Context context;

    public ResponseThread(Socket toClient, Socket toTargetServer, Context context){
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
        this.context = context;
        isDone = false;
    }

    public String responseFromTargetServer(){
        StringBuilder response = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(toTargetServer.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            toTargetServer.shutdownInput();
            context.countResponseBytes(response.toString().getBytes(StandardCharsets.UTF_8).length);
        } catch (IOException e){
            log.error("ERROR: Couldn't get a response from the target server");
//            throw new RuntimeException("ERROR: Couldn't get a response from the target server");
        }
            context.countResponseBytes(response.toString().getBytes().length);
            return response.toString();
    }

    public void sentResponseToClient(String response){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(toClient.getOutputStream()));
            writer.write(response);
            writer.newLine();
            writer.flush();
            toClient.shutdownOutput();
        } catch (IOException e) {
            log.error("ERROR: Couldn't send response to client");
//            throw new RuntimeException("ERROR: Couldn't send response to client");
        }
    }

    public boolean isDone(){
        return isDone;
    }


    @Override
    public void run() {
        String response = responseFromTargetServer();
        sentResponseToClient(response);
        isDone = true;
    }
}
