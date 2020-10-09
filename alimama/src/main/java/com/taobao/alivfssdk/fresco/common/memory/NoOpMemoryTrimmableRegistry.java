package com.taobao.alivfssdk.fresco.common.memory;

public class NoOpMemoryTrimmableRegistry implements MemoryTrimmableRegistry {
    private static NoOpMemoryTrimmableRegistry sInstance;

    public void registerMemoryTrimmable(MemoryTrimmable memoryTrimmable) {
    }

    public void unregisterMemoryTrimmable(MemoryTrimmable memoryTrimmable) {
    }

    public static synchronized NoOpMemoryTrimmableRegistry getInstance() {
        NoOpMemoryTrimmableRegistry noOpMemoryTrimmableRegistry;
        synchronized (NoOpMemoryTrimmableRegistry.class) {
            if (sInstance == null) {
                sInstance = new NoOpMemoryTrimmableRegistry();
            }
            noOpMemoryTrimmableRegistry = sInstance;
        }
        return noOpMemoryTrimmableRegistry;
    }
}
