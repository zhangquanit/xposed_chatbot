package com.xiaomi.push;

import com.xiaomi.push.bw;

public class bs extends bw.d {
    protected String a = "MessageDeleteJob";

    public bs(String str, String str2, String[] strArr, String str3) {
        super(str, str2, strArr);
        this.a = str3;
    }

    public static bs a(String str) {
        return new bs(str, "status = ?", new String[]{String.valueOf(2)}, "a job build to delete uploaded job");
    }
}
