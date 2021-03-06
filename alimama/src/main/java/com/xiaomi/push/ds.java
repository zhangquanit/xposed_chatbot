package com.xiaomi.push;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import com.alibaba.wireless.security.SecExceptionCode;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.ag;

public class ds {
    private static volatile ds a;

    /* renamed from: a  reason: collision with other field name */
    private Context f240a;

    private ds(Context context) {
        this.f240a = context;
    }

    private int a(int i) {
        return Math.max(60, i);
    }

    public static ds a(Context context) {
        if (a == null) {
            synchronized (ds.class) {
                if (a == null) {
                    a = new ds(context);
                }
            }
        }
        return a;
    }

    private boolean a() {
        if (Build.VERSION.SDK_INT >= 14) {
            try {
                ((Application) (this.f240a instanceof Application ? this.f240a : this.f240a.getApplicationContext())).registerActivityLifecycleCallbacks(new dj(this.f240a, String.valueOf(System.currentTimeMillis() / 1000)));
                return true;
            } catch (Exception e) {
                b.a((Throwable) e);
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void b() {
        ai a2 = ai.a(this.f240a);
        ag a3 = ag.a(this.f240a);
        SharedPreferences sharedPreferences = this.f240a.getSharedPreferences("mipush_extra", 0);
        long currentTimeMillis = System.currentTimeMillis();
        long j = sharedPreferences.getLong("first_try_ts", currentTimeMillis);
        if (j == currentTimeMillis) {
            sharedPreferences.edit().putLong("first_try_ts", currentTimeMillis).commit();
        }
        if (Math.abs(currentTimeMillis - j) >= 172800000) {
            boolean a4 = a3.a(hl.ScreenSizeCollectionSwitch.a(), true);
            boolean a5 = a3.a(hl.AndroidVnCollectionSwitch.a(), true);
            boolean a6 = a3.a(hl.AndroidVcCollectionSwitch.a(), true);
            boolean a7 = a3.a(hl.AndroidIdCollectionSwitch.a(), true);
            boolean a8 = a3.a(hl.OperatorSwitch.a(), true);
            if (a4 || a5 || a6 || a7 || a8) {
                int a9 = a(a3.a(hl.DeviceInfoCollectionFrequency.a(), 1209600));
                dz dzVar = r6;
                dz dzVar2 = new dz(this.f240a, a9, a4, a5, a6, a7, a8);
                a2.a(dzVar, a9, 30);
            }
            boolean a10 = a3.a(hl.MacCollectionSwitch.a(), false);
            boolean a11 = a3.a(hl.IMSICollectionSwitch.a(), false);
            boolean a12 = a3.a(hl.IccidCollectionSwitch.a(), false);
            boolean a13 = a3.a(hl.DeviceIdSwitch.a(), false);
            if (a10 || a11 || a12 || a13) {
                int a14 = a(a3.a(hl.DeviceBaseInfoCollectionFrequency.a(), 1209600));
                a2.a(new dy(this.f240a, a14, a10, a11, a12, a13), a14, 30);
            }
            if (Build.VERSION.SDK_INT < 21 && a3.a(hl.AppActiveListCollectionSwitch.a(), false)) {
                int a15 = a(a3.a(hl.AppActiveListCollectionFrequency.a(), (int) SecExceptionCode.SEC_ERROR_UMID_VALID));
                a2.a(new du(this.f240a, a15), a15, 30);
            }
            if (a3.a(hl.StorageCollectionSwitch.a(), true)) {
                int a16 = a(a3.a(hl.StorageCollectionFrequency.a(), 86400));
                a2.a(new ea(this.f240a, a16), a16, 30);
            }
            if (a3.a(hl.TopAppCollectionSwitch.a(), false)) {
                int a17 = a(a3.a(hl.TopAppCollectionFrequency.a(), 300));
                a2.a(new eb(this.f240a, a17), a17, 30);
            }
            if (a3.a(hl.BroadcastActionCollectionSwitch.a(), true)) {
                int a18 = a(a3.a(hl.BroadcastActionCollectionFrequency.a(), (int) SecExceptionCode.SEC_ERROR_UMID_VALID));
                a2.a(new dw(this.f240a, a18), a18, 30);
            }
            if (a3.a(hl.ActivityTSSwitch.a(), false)) {
                a();
            }
            if (a3.a(hl.UploadSwitch.a(), true)) {
                a2.a(new ec(this.f240a), a(a3.a(hl.UploadFrequency.a(), 86400)), 60);
            }
            if (a3.a(hl.BatteryCollectionSwitch.a(), false)) {
                int a19 = a(a3.a(hl.BatteryCollectionFrequency.a(), 3600));
                a2.a(new dv(this.f240a, a19), a19, 30);
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m178a() {
        ai.a(this.f240a).a((Runnable) new dt(this), 30);
    }
}
