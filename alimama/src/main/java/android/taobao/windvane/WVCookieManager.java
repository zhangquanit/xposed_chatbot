package android.taobao.windvane;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class WVCookieManager {
    private static final String TAG = "WVCookieManager";

    public static void onCreate(Context context) {
        CookieSyncManager.createInstance(context);
    }

    public static String getCookie(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return CookieManager.getInstance().getCookie(str);
    }

    public static void setCookie(String str, String str2) {
        if (!TextUtils.isEmpty(str) && str2 != null) {
            if (!CookieManager.getInstance().acceptCookie()) {
                CookieManager.getInstance().setAcceptCookie(true);
            }
            CookieManager.getInstance().setCookie(str, str2);
        }
    }
}
