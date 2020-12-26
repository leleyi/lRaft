package les.org.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

public class Bus {


    public static void main(String[] args) {
        EventBus bus = new EventBus();
        bus.register(new Bus());
        bus.post("1");
        bus.post("2");
        bus.post("3");
        bus.post("4");
        bus.post("5");
        bus.post("6");
        bus.post("7");
        new Thread(() -> {
            bus.post(2);
        }, "aaaa").start();

    }

    @Subscribe
    public void test1(String string) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " String : " + string);
    }

    @Subscribe
    public void test2(Integer string) {
        System.out.println(Thread.currentThread().getName() + " int : " + string);
    }


}
