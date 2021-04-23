package context;

import java.util.Date;

public class Context {

    private boolean threadPairIsFinished;
    private int requestBytes;
    private int responseBytes;
    private Date startSession;
    private Date stopSession;

    public boolean isThreadPairIsFinished() {
        return threadPairIsFinished;
    }

    public void setThreadPairIsFinished(boolean threadPairIsFinished) {
        this.threadPairIsFinished = threadPairIsFinished;
    }

    public int getRequestBytes() {
        return requestBytes;
    }

    public void setRequestBytes(int requestBytes) {
        this.requestBytes = requestBytes;
    }

    public int getResponseBytes() {
        return responseBytes;
    }

    public void setResponseBytes(int responseBytes) {
        this.responseBytes = responseBytes;
    }

    public Date getStartSession() {
        return startSession;
    }

    public void setStartSession(Date startSession) {
        this.startSession = startSession;
    }

    public Date getStopSession() {
        return stopSession;
    }

    public void setStopSession(Date stopSession) {
        this.stopSession = stopSession;
    }
}
