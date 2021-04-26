package socketThreadPair;

import context.Context;
import org.apache.log4j.Logger;
import poolThreads.PoolThreads;
import request.RequestThread;
import response.ResponseThread;

import java.io.IOException;
import java.net.Socket;

public class SocketThreadPair implements Runnable{

    private static final Logger log = Logger.getLogger(SocketThreadPair.class);

    private RequestThread request;
    private ResponseThread response;
    private Socket toClient;
    private Context context;

    public SocketThreadPair(Socket toClient, Socket toTargetServer) {
        this.toClient = toClient;
        this.context = new Context();
        this.request = new RequestThread(toClient, toTargetServer, context);
        this.response = new ResponseThread(toClient, toTargetServer, context);
    }

    public RequestThread getRequest() {
        return request;
    }

    public void setRequest(RequestThread request) {
        this.request = request;
    }

    public ResponseThread getResponse() {
        return response;
    }

    public void setResponse(ResponseThread response) {
        this.response = response;
    }

    private void close() {
        response.sentResponseToClient("No request has been received from you within 20 seconds"); //стоит ли так делать?
        try {
            toClient.close();
        } catch (IOException e) {
            log.error("ERROR : Error when closing connection");
        }
    }

    @Override
    public void run() {
        context.setStartSession();
//        while (!toClient.isClosed()) {
            PoolThreads.INSTANCE.executor.submit(request);
//            try {
//                toClient.setSoTimeout(20000);
//            } catch (SocketException e) {
//                log.error("ERROR on " + toClient.getInetAddress() +
//                        ": An error occurred while waiting for a request from the client." );
//                this.close();
//                break;
//            }
            //todo добавить таймер ожидания запроса от клиента(если запроса нет закрывать тредпару и соединение)
            PoolThreads.INSTANCE.executor.submit(response);
            //todo добавить таймер ожидания ответа от сервера(если сервера нет закрывать тредпару и соединение)
//        }
        context.setStopSession();
        System.out.println(context.toString());
    }
}
