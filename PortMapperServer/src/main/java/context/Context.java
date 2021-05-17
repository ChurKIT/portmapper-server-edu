package context;

import java.util.concurrent.atomic.AtomicInteger;

public class Context {

    public static transient AtomicInteger id_generator = new AtomicInteger(0);

    private int id;
    private String clientAddress;
    private boolean threadPairIsFinished;
    private int requestBytes = 0;
    private int responseBytes = 0;
    private long startSession;
    private long stopSession;
    private int workTime;
    private Message message;

    public Context() {
        this.id = id_generator.addAndGet(1);
    }

    public boolean isThreadPairIsFinished() {
        return threadPairIsFinished;
    }

    public void setThreadPairIsFinished(boolean threadPairIsFinished) {
        this.threadPairIsFinished = threadPairIsFinished;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequest() {
        return message.getRequest();
    }

    public void setRequest(String request) {
       message.setRequest(request);
    }

    public String getResponse() {
        return message.getResponse();
    }

    public void setResponse(String response) {
        message.setResponse(response);
    }

    public int getId() {
        return id;
    }

    public int getRequestBytes() {
        return requestBytes;
    }

    public int getResponseBytes() {
        return responseBytes;
    }

    public long getStartSession() {
        return startSession;
    }

    public void initStartSession() {

        this.startSession = System.currentTimeMillis();
    }

    public long getStopSession() {
        return stopSession;
    }

    public void initStopSession() {
        this.stopSession = System.currentTimeMillis();
        initWorkTime();
    }

    public int getWorkTime() {
        return workTime;
    }

    public void initWorkTime() {
        this.workTime = (int) (stopSession - startSession);
    }

    public void countRequestBytes(int bytes){
        requestBytes += bytes;

    }

    public void countResponseBytes(int bytes){
        responseBytes += bytes;

    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setRequestBytes(int requestBytes) {
        this.requestBytes = requestBytes;
    }

    public void setResponseBytes(int responseBytes) {
        this.responseBytes = responseBytes;
    }

    public void setStartSession(long startSession) {
        this.startSession = startSession;
    }

    public void setStopSession(long stopSession) {
        this.stopSession = stopSession;
    }

    public void initWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    @Override
    public String toString() {
        return "Context{" +
//                "clientAddress='" + clientAddress + '\'' +
//                ", threadPairIsFinished=" + threadPairIsFinished +
                ", requestBytes=" + requestBytes +
                ", responseBytes=" + responseBytes +
                ", startSession=" + startSession +
                ", stopSession=" + stopSession +
                ", workTime=" + workTime +
                '}';
    }
}
