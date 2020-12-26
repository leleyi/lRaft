package les.org.eventbus;

public class DiveInto {

    static ThreadLocal<MateData> tl = new ThreadLocal();

    public static void main(String[] args) {

        tl.set(new MateData());

        new Thread(() -> {

            MateData mateData = tl.get();
            System.out.println(mateData);
        }, "Thread1").start();

        MateData mateData = tl.get();
        System.out.println(mateData.name);
    }

    static class MateData {
        String name = "hello word";
    }

}
