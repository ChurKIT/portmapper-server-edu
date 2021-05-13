package context;

public class Context {

    private String clientAddress;
    private boolean threadPairIsFinished;
    private int requestBytes = 0;
    private int responseBytes = 0;
    private long startSession;
    private long stopSession;
    private long workTime;

    public boolean isThreadPairIsFinished() {
        return threadPairIsFinished;
    }

    public void setThreadPairIsFinished(boolean threadPairIsFinished) {
        this.threadPairIsFinished = threadPairIsFinished;
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

    public void setStartSession() {

        this.startSession = System.currentTimeMillis();
    }

    public long getStopSession() {
        return stopSession;
    }

    public void setStopSession() {
        this.stopSession = System.currentTimeMillis();
        setWorkTime();
    }

    public long getWorkTime() {
        return workTime;
    }

    public void setWorkTime() {
        this.workTime = stopSession - startSession;
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

    @Override
    public String toString() {
        return "Context{" +
                "clientAddress='" + clientAddress + '\'' +
                ", threadPairIsFinished=" + threadPairIsFinished +
                ", requestBytes=" + requestBytes +
                ", responseBytes=" + responseBytes +
                ", startSession=" + startSession +
                ", stopSession=" + stopSession +
                ", workTime=" + workTime +
                '}';
    }
}
