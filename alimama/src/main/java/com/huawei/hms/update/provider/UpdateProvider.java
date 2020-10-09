package com.huawei.hms.update.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.taobao.weex.ui.component.WXComponent;
import java.io.File;
import java.io.FileNotFoundException;
import mtopsdk.mtop.upload.domain.UploadConstants;

public class UpdateProvider extends ContentProvider {
    public static final String AUTHORITIES_SUFFIX = ".hms.update.provider";
    private static final String[] a = {"_display_name", "_size"};
    private static a b = new a();

    public boolean onCreate() {
        return true;
    }

    public static Uri getUriForFile(Context context, String str, File file) {
        b.a(context);
        return b.a(file, str);
    }

    public static File getLocalFile(Context context, String str) {
        b.a(context);
        return b.a(str);
    }

    private static int a(String str) {
        if (UploadQueueMgr.MSGTYPE_REALTIME.equals(str)) {
            return 268435456;
        }
        if (WXComponent.PROP_FS_WRAP_CONTENT.equals(str) || "wt".equals(str)) {
            return 738197504;
        }
        if ("wa".equals(str)) {
            return 704643072;
        }
        if ("rw".equals(str)) {
            return 939524096;
        }
        if ("rwt".equals(str)) {
            return 1006632960;
        }
        throw new IllegalArgumentException("Invalid mode: " + str);
    }

    private static String[] a(String[] strArr, int i) {
        String[] strArr2 = new String[i];
        System.arraycopy(strArr, 0, strArr2, 0, i);
        return strArr2;
    }

    private static Object[] a(Object[] objArr, int i) {
        Object[] objArr2 = new Object[i];
        System.arraycopy(objArr, 0, objArr2, 0, i);
        return objArr2;
    }

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (providerInfo.exported) {
            throw new SecurityException("Provider must not be exported");
        } else if (providerInfo.grantUriPermissions) {
            b.a(context);
        } else {
            throw new SecurityException("Provider must grant uri permissions");
        }
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("No external inserts");
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new UnsupportedOperationException("No external updates");
    }

    public String getType(Uri uri) {
        String name = b.a(uri).getName();
        int lastIndexOf = name.lastIndexOf(46);
        if (lastIndexOf < 0) {
            return UploadConstants.FILE_CONTENT_TYPE;
        }
        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(name.substring(lastIndexOf + 1));
        return !TextUtils.isEmpty(mimeTypeFromExtension) ? mimeTypeFromExtension : UploadConstants.FILE_CONTENT_TYPE;
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        int i;
        File a2 = b.a(uri);
        if (strArr == null) {
            strArr = a;
        }
        String[] strArr3 = new String[strArr.length];
        Object[] objArr = new Object[strArr.length];
        int i2 = 0;
        for (String str3 : strArr) {
            if ("_display_name".equals(str3)) {
                strArr3[i2] = "_display_name";
                i = i2 + 1;
                objArr[i2] = a2.getName();
            } else if ("_size".equals(str3)) {
                strArr3[i2] = "_size";
                i = i2 + 1;
                objArr[i2] = Long.valueOf(a2.length());
            }
            i2 = i;
        }
        String[] a3 = a(strArr3, i2);
        Object[] a4 = a(objArr, i2);
        MatrixCursor matrixCursor = new MatrixCursor(a3, 1);
        matrixCursor.addRow(a4);
        return matrixCursor;
    }

    public int delete(Uri uri, String str, String[] strArr) {
        return b.a(uri).delete() ? 1 : 0;
    }

    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        return ParcelFileDescriptor.open(b.a(uri), a(str));
    }
}
