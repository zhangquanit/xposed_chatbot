package com.xiaomi.push;

import java.io.ByteArrayOutputStream;

public class iu extends ByteArrayOutputStream {
    public iu() {
    }

    public iu(int i) {
        super(i);
    }

    public int a() {
        return this.count;
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m495a() {
        return this.buf;
    }
}
