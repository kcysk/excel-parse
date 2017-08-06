package net.zdsoft;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author shenke
 * @since 17-8-6 下午1:32
 */
public class ExecutorServerTest {

    @Test
    public void testCancel() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (int i=0 ; i< 10; i++) {
            final int t = i;
            Future future = executorService.submit((Runnable) () -> System.out.printf(t+""));
            future.cancel(Boolean.TRUE);
        }
        Future future = executorService.submit(()-> System.out.printf("111"));
        Thread.sleep(200);
        System.out.println(((ThreadPoolExecutor)executorService).getQueue().size());
    }
}
