package ru.otus.gc;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GCDemo {
    private static List<Long> list1 = new ArrayList<>();
    private static List<Long> list2 = new ArrayList<>();

    private static long gcTimePerMinute;
    private static long gcYoungCountPerMinute;
    private static long gcFullCountPerMinute;
    private static int minuteCount;

    private static long gcTimeTotal;
    private static long gcYoungCountTotal;
    private static long gcFullCountTotal;

    private static final Object lock = new Object();

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            switchOnMonitoring();
            runLogger();
            runMemoryLeak();
        } finally {
            System.out.println(list1.size());
            System.out.println(list2.size());
            System.out.println("Total time (ms): " + (System.currentTimeMillis() - startTime) +
                    "\nGC time total (ms): " + gcTimeTotal +
                    "\nGC Young amount total: " + gcYoungCountTotal +
                    "\nGC Full amount total: " + gcFullCountTotal);
        }
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcAction = info.getGcAction();
                    long duration = info.getGcInfo().getDuration();

                    synchronized (lock) {
                        gcTimePerMinute += duration;
                        gcTimeTotal += duration;
                        if (gcAction.contains("minor")) {
                            gcYoungCountPerMinute++;
                            gcYoungCountTotal++;
                        } else if (gcAction.contains("major")) {
                            gcFullCountPerMinute++;
                            gcFullCountTotal++;
                        }
                    }
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    private static void runLogger() {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    TimeUnit.MINUTES.sleep(1);

                    synchronized (lock) {
                        System.out.println("Minute: " + ++minuteCount +
                                "\nGC time (ms): " + gcTimePerMinute +
                                "\nGC Young amount: " + gcYoungCountPerMinute +
                                "\nGC Full amount: " + gcFullCountPerMinute);
                        gcTimePerMinute = 0;
                        gcYoungCountPerMinute = 0;
                        gcFullCountPerMinute = 0;
                    }
                }
            } catch (InterruptedException ignored) {
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private static void runMemoryLeak() throws InterruptedException {
        long i = 0;
        while (i < Long.MAX_VALUE) {
            if (i % 50 == 0) TimeUnit.NANOSECONDS.sleep(1);
            list1.add(Long.valueOf(i));
            list2.add(Long.valueOf(i));
            i++;
            if (i % 200000 == 0) {
                list2.clear();
            }
        }
    }
}