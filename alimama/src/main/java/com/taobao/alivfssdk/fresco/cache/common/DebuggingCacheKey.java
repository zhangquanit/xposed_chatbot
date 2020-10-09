package com.taobao.alivfssdk.fresco.cache.common;

import androidx.annotation.Nullable;

public class DebuggingCacheKey extends SimpleCacheKey {
    private final Object mCallerContext;

    public DebuggingCacheKey(String str, @Nullable Object obj) {
        super(str);
        this.mCallerContext = obj;
    }

    @Nullable
    public Object getCallerContext() {
        return this.mCallerContext;
    }
}
