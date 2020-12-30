package les.org.eventbus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IExecutor {
    public static void main(String[] args) {

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("3");
    }
}
