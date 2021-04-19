package socketThreadPair;

import request.RequestThread;
import response.ResponseThread;

import java.net.HttpURLConnection;
import java.net.Socket;

public class SocketThreadPair extends Thread{

    private RequestThread request;
    private ResponseThread response;

    public SocketThreadPair(Socket toClient, Socket toTargetServer) {
        this.request = new RequestThread(toClient, toTargetServer);
        this.response = new ResponseThread(toClient, toTargetServer);
        this.start();
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

    @Override
    public void run() {
        request.start();
        //todo remove this stub
        while (!request.isDone()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        response.start();

    }
}
