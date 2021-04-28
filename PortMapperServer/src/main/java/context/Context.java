package context;

public class Context {

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

    @Override
    public String toString() {
        return "Context{" +
                "threadPairIsFinished=" + threadPairIsFinished +
                ", requestBytes=" + requestBytes +
                ", responseBytes=" + responseBytes +
                ", startSession=" + startSession +
                ", stopSession=" + stopSession +
                ", workTime=" + workTime +
                '}';
    }
}
