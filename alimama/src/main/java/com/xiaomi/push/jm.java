package com.xiaomi.push;

public abstract class jm {
    public int a() {
        return 0;
    }

    public abstract int a(byte[] bArr, int i, int i2);

    public void a(int i) {
    }

    /* renamed from: a  reason: collision with other method in class */
    public abstract void m531a(byte[] bArr, int i, int i2);

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m532a() {
        return null;
    }

    public int b() {
        return -1;
    }

    public int b(byte[] bArr, int i, int i2) {
        int i3 = 0;
        while (i3 < i2) {
            int a = a(bArr, i + i3, i2 - i3);
            if (a > 0) {
                i3 += a;
            } else {
                throw new jn("Cannot read. Remote side has closed. Tried to read " + i2 + " bytes, but only got " + i3 + " bytes.");
            }
        }
        return i3;
    }
}
