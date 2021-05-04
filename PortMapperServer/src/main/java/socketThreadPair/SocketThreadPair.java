package socketThreadPair;

import context.Context;
import org.apache.log4j.Logger;
import poolThreads.PoolThreads;
import request.RequestThread;
import response.ResponseThread;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SocketThreadPair implements Runnable{

    private static final Logger log = Logger.getLogger(SocketThreadPair.class);

//    private RequestThread request;
//    private ResponseThread response;
    private final Socket toClient;
    private final Socket toTargetServer;
    private Context context;
//    private Queue<RequestThread> requestQueue = new PriorityQueue<>();

    public SocketThreadPair(Socket toClient, Socket toTargetServer) {
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
        this.context = new Context();
//        this.response = new ResponseThread(toClient, toTargetServer, context);
    }

//    public RequestThread getRequest() {
//        return request;
//    }
//
//    public void setRequest(RequestThread request) {
//        this.request = request;
//    }
//
//    public ResponseThread getResponse() {
//        return response;
//    }
//
//    public void setResponse(ResponseThread response) {
//        this.response = response;
//    }

    private void close() {
        //response.sentResponseToClient("No request has been received from you within 20 seconds"); //стоит ли так делать?
        try {
            toClient.close();
        } catch (IOException e) {
            log.error("ERROR : Error when closing connection");
        }
    }

    private void requestSubmit(){
        RequestThread request = new RequestThread(toClient, toTargetServer, context);
//        requestQueue.add(request);
        Future<?> requestFuture = PoolThreads.INSTANCE.executor.submit(request);
        try {
            requestFuture.get(60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            log.error("ERROR on " + toClient.getInetAddress() + " : The timeout for a request from the client has expired ");
            close();
        }
    }

    private void responseSubmit(){
        ResponseThread response = new ResponseThread(toClient, toTargetServer, context);
        Future<?> responseFuture = PoolThreads.INSTANCE.executor.submit(response);
        try {
            responseFuture.get(60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            log.error("ERROR on " + toClient.getInetAddress() + " : The timeout for a response from the target server has expired ");
            close();
        }
    }

    @Override
    public void run() {
        context.setStartSession();
        //todo solve infinite loop problem
//        while (toClient.isConnected()) {
            requestSubmit();
            responseSubmit();
//        }
        context.setStopSession();
        System.out.println(context.toString());
    }
}
