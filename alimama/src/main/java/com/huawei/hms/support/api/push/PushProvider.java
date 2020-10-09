package com.huawei.hms.support.api.push;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.huawei.hms.support.log.a;
import java.io.File;
import java.io.FileNotFoundException;

public class PushProvider extends ContentProvider {
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public boolean onCreate() {
        return false;
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    public String getType(Uri uri) {
        if (uri.toString().endsWith(".xml")) {
            return "xml";
        }
        return null;
    }

    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        a.b("PushSelfShowLog", "use sdk PushProvider openFile");
        if ("xml".equals(getType(uri))) {
            if (Build.VERSION.SDK_INT >= 24) {
                StringBuilder sb = new StringBuilder();
                Context context = getContext();
                context.getClass();
                sb.append(context.createDeviceProtectedStorageContext().getDataDir());
                sb.append("/shared_prefs/push_notify_flag.xml");
                File file = new File(sb.toString());
                if (file.exists()) {
                    return ParcelFileDescriptor.open(file, 268435456);
                }
                File file2 = new File(getContext().getDataDir() + "/shared_prefs/push_notify_flag.xml");
                if (file2.exists()) {
                    return ParcelFileDescriptor.open(file2, 268435456);
                }
            } else {
                String str2 = getContext().getFilesDir() + "";
                if (!TextUtils.isEmpty(str2)) {
                    File file3 = new File(str2.substring(0, str2.length() - 6) + "/shared_prefs/push_notify_flag.xml");
                    if (file3.exists()) {
                        return ParcelFileDescriptor.open(file3, 268435456);
                    }
                }
            }
            throw new FileNotFoundException(uri.getPath());
        }
        a.c("PushSelfShowLog", "Incorrect file uri");
        throw new FileNotFoundException(uri.getPath());
    }
}
