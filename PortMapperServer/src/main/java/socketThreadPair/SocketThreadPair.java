package socketThreadPair;

import request.RequestThread;
import response.ResponseThread;

import java.net.Socket;

public class SocketThreadPair extends Thread{

    private RequestThread request;
    private ResponseThread response;
    //private ThreadContext context

    public SocketThreadPair(Socket toClient, Socket toTargetServer) {
        this.request = new RequestThread(toClient, toTargetServer);
        this.response = new ResponseThread(toClient, toTargetServer);
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

    // interface ThreadListener
    @Override
    public void run() {
        request.start();
        while (true){
            if(request.isDone()){
                break;
            }
        }
        response.start();
        while (true){
            if(response.isDone()){
                break;
            }
        }
    }
}
