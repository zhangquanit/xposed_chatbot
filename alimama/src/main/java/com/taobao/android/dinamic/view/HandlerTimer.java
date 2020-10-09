package com.taobao.android.dinamic.view;

import android.os.Handler;
import android.os.Looper;

public class HandlerTimer implements Runnable {
    private Handler handler;
    protected long interval;
    protected TimerStatus status;
    private Runnable task;

    public void runOver() {
    }

    public HandlerTimer(Runnable runnable) {
        this(1000, runnable);
    }

    public HandlerTimer(long j, Runnable runnable) {
        this(j, runnable, new Handler(Looper.getMainLooper()));
    }

    public HandlerTimer(long j, Runnable runnable, Handler handler2) {
        this.interval = j;
        this.task = runnable;
        this.handler = handler2;
        this.status = TimerStatus.Waiting;
    }

    public final void run() {
        if (this.status != TimerStatus.Waiting && this.status != TimerStatus.Paused && this.status != TimerStatus.Stopped) {
            this.task.run();
            runOver();
            this.handler.removeCallbacks(this);
            this.handler.postDelayed(this, this.interval);
        }
    }

    public void start() {
        this.handler.removeCallbacks(this);
        this.status = TimerStatus.Running;
        this.handler.postDelayed(this, this.interval);
    }

    public void start(long j) {
        this.handler.removeCallbacks(this);
        this.status = TimerStatus.Running;
        this.handler.postDelayed(this, j);
    }

    public void pause() {
        this.status = TimerStatus.Paused;
        this.handler.removeCallbacks(this);
    }

    public void restart() {
        this.handler.removeCallbacks(this);
        this.status = TimerStatus.Running;
        this.handler.postDelayed(this, this.interval);
    }

    public void stop() {
        this.status = TimerStatus.Stopped;
        this.handler.removeCallbacksAndMessages((Object) null);
    }

    public void cancel() {
        this.status = TimerStatus.Stopped;
        this.handler.removeCallbacks(this);
    }

    public enum TimerStatus {
        Waiting(0, "待启动"),
        Running(1, "运行中"),
        Paused(-1, "暂停"),
        Stopped(-2, "停止");
        
        private int code;
        private String desc;

        private TimerStatus(int i, String str) {
            this.code = i;
            this.desc = str;
        }

        public int getCode() {
            return this.code;
        }

        public void setCode(int i) {
            this.code = i;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String str) {
            this.desc = str;
        }
    }
}
