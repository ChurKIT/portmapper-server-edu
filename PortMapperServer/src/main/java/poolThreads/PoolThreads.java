package poolThreads;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public enum PoolThreads {

    INSTANCE;

    public ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);


}
