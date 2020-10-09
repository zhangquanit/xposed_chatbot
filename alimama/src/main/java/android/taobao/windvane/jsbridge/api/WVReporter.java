package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.monitor.WVMonitorService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class WVReporter extends WVApiPlugin {
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("reportError".equals(str)) {
            reportError(wVCallBackContext, str2);
            return true;
        } else if (!"reportDomLoad".equals(str)) {
            return false;
        } else {
            reportDomLoad(wVCallBackContext, str2);
            return true;
        }
    }

    public synchronized void reportError(WVCallBackContext wVCallBackContext, String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String url = wVCallBackContext.getWebview().getUrl();
            if (WVMonitorService.getErrorMonitor() != null) {
                WVMonitorService.getErrorMonitor().didOccurJSError(url, jSONObject.optString("msg"), jSONObject.optString("file"), jSONObject.optString("line"));
            }
            wVCallBackContext.success();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void reportDomLoad(WVCallBackContext wVCallBackContext, String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String url = wVCallBackContext.getWebview().getUrl();
            long optLong = jSONObject.optLong("time", 0);
            long optLong2 = jSONObject.optLong("firstByte", 0);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                if (next.startsWith("self_")) {
                    Long valueOf = Long.valueOf(jSONObject.optLong(next));
                    if (WVMonitorService.getPerformanceMonitor() != null) {
                        WVMonitorService.getPerformanceMonitor().didPageOccurSelfDefinedEvent(url, next.substring(5), valueOf.longValue());
                    }
                }
            }
            if (WVMonitorService.getPerformanceMonitor() != null) {
                WVMonitorService.getPerformanceMonitor().didPageDomLoadAtTime(url, optLong);
                WVMonitorService.getPerformanceMonitor().didPageReceiveFirstByteAtTime(url, optLong2);
            }
            wVCallBackContext.success();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
