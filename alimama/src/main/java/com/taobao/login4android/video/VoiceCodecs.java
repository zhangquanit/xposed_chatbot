package com.taobao.login4android.video;

import com.alibaba.idst.nls.internal.codec.OupsCodec;

public class VoiceCodecs {
    private OupsCodec mOupsCodec;

    public VoiceCodecs() {
        this.mOupsCodec = null;
        this.mOupsCodec = new OupsCodec();
    }

    public boolean open(boolean z) {
        return this.mOupsCodec.open(z);
    }

    public void close() {
        this.mOupsCodec.close();
    }

    public int bufferFrame(short[] sArr, byte[] bArr) {
        return this.mOupsCodec.encodec(sArr, 0, 320, bArr);
    }

    public int bufferFrame(short[] sArr, byte[] bArr, int i) {
        return this.mOupsCodec.encodec(sArr, 0, i, bArr);
    }
}
