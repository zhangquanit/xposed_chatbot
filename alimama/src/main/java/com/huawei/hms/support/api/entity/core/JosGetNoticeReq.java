package com.huawei.hms.support.api.entity.core;

import com.huawei.hms.core.aidl.a.a;

public class JosGetNoticeReq extends JosBaseReq {
    public static final int NOTICE_TYPE_CONN = 0;
    public static final int NOTICE_TYPE_SIGN = 1;
    @a
    private int noticeType;

    private static <T> T get(T t) {
        return t;
    }

    public int getNoticeType() {
        return ((Integer) get(Integer.valueOf(this.noticeType))).intValue();
    }

    public void setNoticeType(int i) {
        this.noticeType = i;
    }
}
