package com.taobao.weex.tracing;

import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stopwatch {
    private static final ThreadLocal<Stopwatch> sThreadLocal = new ThreadLocal<>();
    private List<ProcessEvent> splits = new ArrayList();
    private long startMillis;
    private long startNanos;

    public static class ProcessEvent {
        public double duration;
        public String fname;
        public long startMillis;
    }

    private static void prepare() {
        if (sThreadLocal.get() == null) {
            sThreadLocal.set(new Stopwatch());
        }
    }

    public static void tick() {
        if (WXTracing.isAvailable()) {
            try {
                prepare();
                if (sThreadLocal.get().startNanos != 0) {
                    WXLogUtils.w("Stopwatch", "Stopwatch is not reset");
                }
                sThreadLocal.get().startNanos = System.nanoTime();
                sThreadLocal.get().startMillis = System.currentTimeMillis();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public static void split(String str) {
        if (WXTracing.isAvailable()) {
            try {
                ProcessEvent processEvent = new ProcessEvent();
                long j = sThreadLocal.get().startMillis;
                double tackAndTick = tackAndTick();
                processEvent.fname = str;
                processEvent.duration = tackAndTick;
                processEvent.startMillis = j;
                sThreadLocal.get().splits.add(processEvent);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public static List<ProcessEvent> getProcessEvents() {
        if (!WXTracing.isAvailable()) {
            return Collections.emptyList();
        }
        tack();
        List<ProcessEvent> list = sThreadLocal.get().splits;
        sThreadLocal.get().splits = new ArrayList();
        return list;
    }

    public static double tack() {
        if (!WXTracing.isAvailable()) {
            return -1.0d;
        }
        try {
            long j = sThreadLocal.get().startNanos;
            if (j == 0) {
                WXLogUtils.w("Stopwatch", "Should call Stopwatch.tick() before Stopwatch.tack() called");
            }
            long nanoTime = System.nanoTime() - j;
            sThreadLocal.get().startNanos = 0;
            return nanosToMillis(nanoTime);
        } catch (Throwable th) {
            th.printStackTrace();
            return -1.0d;
        }
    }

    public static long lastTickStamp() {
        if (!WXTracing.isAvailable()) {
            return -1;
        }
        try {
            return sThreadLocal.get().startMillis;
        } catch (Throwable th) {
            th.printStackTrace();
            return -1;
        }
    }

    public static double tackAndTick() {
        double tack = tack();
        tick();
        return tack;
    }

    public static double nanosToMillis(long j) {
        double d = (double) j;
        Double.isNaN(d);
        return d / 1000000.0d;
    }

    public static double millisUntilNow(long j) {
        return nanosToMillis(System.nanoTime() - j);
    }
}
