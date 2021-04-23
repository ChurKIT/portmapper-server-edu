import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public enum PoolThreads {

    INSTANCE;

    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(6);
}
