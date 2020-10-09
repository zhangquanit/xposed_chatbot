package android.taobao.windvane.jsbridge.api;

import android.net.Uri;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weaver.prefetch.GetPrefetchCallback;
import com.taobao.weaver.prefetch.PrefetchDataResponse;
import com.taobao.weaver.prefetch.WMLPrefetch;
import com.uc.webview.export.extension.UCCore;

import java.util.Map;

public class WVPrefetch extends WVApiPlugin {
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("getData".equals(str)) {
            getData(str2, wVCallBackContext);
            return true;
        } else if (!"requestData".equals(str)) {
            return false;
        } else {
            requestData(str2, wVCallBackContext);
            return true;
        }
    }

    public void getData(String str, final WVCallBackContext wVCallBackContext) {
        try {
            JSONObject parseObject = JSONObject.parseObject(str);
            IWVWebView webview = wVCallBackContext.getWebview();
            if (webview == null) {
                WVResult wVResult = new WVResult();
                wVResult.addData("msg", "NO_WEBVIEW");
                wVCallBackContext.error(wVResult);
                return;
            }
            String string = parseObject.getString("externalKey");
            String string2 = parseObject.getString("url");
            if (TextUtils.isEmpty(string2)) {
                string2 = webview.getUrl();
            }
            String matchingUrl = getMatchingUrl(string2);
            if (!TextUtils.isEmpty(string)) {
                matchingUrl = matchingUrl + "#" + string;
            }
            TaoLog.d("WVPrefetch", "getData: " + matchingUrl);
            WMLPrefetch.getInstance().getData(matchingUrl, new GetPrefetchCallback() {
                public void onComplete(PrefetchDataResponse prefetchDataResponse) {
                    try {
                        if (!TextUtils.isEmpty(prefetchDataResponse.jsonData)) {
                            wVCallBackContext.success(prefetchDataResponse.jsonData);
                            return;
                        }
                    } catch (Throwable unused) {
                    }
                    if (prefetchDataResponse.data instanceof JSONObject) {
                        wVCallBackContext.success(((JSONObject) prefetchDataResponse.data).toJSONString());
                    } else {
                        wVCallBackContext.success(new JSONObject((Map<String, Object>) prefetchDataResponse.data).toJSONString());
                    }
                }

                public void onError(PrefetchDataResponse prefetchDataResponse) {
                    WVResult wVResult = new WVResult();
                    wVResult.addData("msg", prefetchDataResponse.performanceData.getStatus().getMsg());
                    wVResult.addData("code", prefetchDataResponse.performanceData.getStatus().getCode());
                    wVCallBackContext.error(wVResult);
                }
            });
        } catch (Throwable th) {
            th.printStackTrace();
            WVResult wVResult2 = new WVResult();
            wVResult2.addData("msg", UCCore.EVENT_EXCEPTION);
            wVResult2.addData("code", "-1");
            wVCallBackContext.error(wVResult2);
        }
    }

    public void requestData(String str, WVCallBackContext wVCallBackContext) {
        try {
            JSONObject parseObject = JSONObject.parseObject(str);
            String string = parseObject.getString("url");
            if (TextUtils.isEmpty(string)) {
                wVCallBackContext.error(WVResult.RET_PARAM_ERR);
                return;
            }
            parseObject.put("userAgent", (Object) this.mWebView.getUserAgentString());
            TaoLog.d("WVPrefetch", "requestData: " + string + " with params: " + parseObject.toJSONString());
            WMLPrefetch.getInstance().prefetchData(string, parseObject);
        } catch (Throwable th) {
            th.printStackTrace();
            WVResult wVResult = new WVResult();
            wVResult.addData("msg", UCCore.EVENT_EXCEPTION);
            wVResult.addData("code", "-1");
            wVCallBackContext.error(wVResult);
        }
    }

    private String getMatchingUrl(String str) {
        Uri parse = Uri.parse(str);
        return parse.getHost() + parse.getPath();
    }
}
