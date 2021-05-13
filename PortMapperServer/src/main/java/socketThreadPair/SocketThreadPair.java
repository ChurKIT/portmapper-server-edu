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

    private final Socket toClient;
    private final Socket toTargetServer;
    private Context context;

    public SocketThreadPair(Socket toClient, Socket toTargetServer) {
        this.toClient = toClient;
        this.toTargetServer = toTargetServer;
        this.context = new Context();
    }

    public Context getContext() {
        return context;
    }

    private void close() {
        try {
            toClient.close();
            toTargetServer.close();
            context.setThreadPairIsFinished(true);
        } catch (IOException e) {
            log.error("ERROR : Error when closing connection");
        }
    }

    private void requestSubmit(){
        RequestThread request = new RequestThread(toClient, toTargetServer, context);
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
            response.sentResponseToClient("Timeout expired");
            log.error("ERROR on " + toClient.getInetAddress() + " : The timeout for a response from the target server has expired ");
            close();
        }
    }

    @Override
    public void run() {
        context.setClientAddress(toClient.getInetAddress().toString());
        context.setStartSession();
            requestSubmit();
            responseSubmit();
        context.setStopSession();
        close();
    }
}
