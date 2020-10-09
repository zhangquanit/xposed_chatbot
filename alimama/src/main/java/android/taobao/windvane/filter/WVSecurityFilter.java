package android.taobao.windvane.filter;

import android.annotation.SuppressLint;
import android.taobao.windvane.service.WVWebViewClientFilter;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.taobao.windvane.webview.WVWrapWebResourceResponse;
import android.text.TextUtils;

import java.io.InputStream;
import java.util.Map;

public class WVSecurityFilter extends WVWebViewClientFilter {
    @SuppressLint({"NewApi", "DefaultLocale"})
    public WVWrapWebResourceResponse shouldInterceptRequest(IWVWebView iWVWebView, String str) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVSecurityFilter", "WVSecurityFilter shouldInterceptRequest url =" + str);
        }
        if (TextUtils.isEmpty(str) || str.length() <= 6 || !str.substring(0, 7).toLowerCase().startsWith("file://")) {
            return super.shouldInterceptRequest(iWVWebView, str);
        }
        return new WVWrapWebResourceResponse("", "utf-8", (InputStream) null, (Map<String, String>) null);
    }
}
