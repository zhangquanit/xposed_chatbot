package com.taobao.android.dinamicx.thread;

import com.taobao.android.dinamicx.template.download.DXPriorityRunnable;

public class DXDownLoadRunnable extends DXPriorityRunnable {
    public DXDownLoadRunnable(int i, Runnable runnable) {
        super(i, runnable);
    }
}
