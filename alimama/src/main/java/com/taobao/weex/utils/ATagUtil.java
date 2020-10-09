package com.taobao.weex.utils;

import android.net.Uri;
import android.view.View;
import com.alibaba.fastjson.JSONArray;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.URIAdapter;

public class ATagUtil {
    public static void onClick(View view, String str, String str2) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance != null) {
            String uri = sDKInstance.rewriteUri(Uri.parse(str2), URIAdapter.LINK).toString();
            JSONArray jSONArray = new JSONArray();
            jSONArray.add(uri);
            WXSDKManager.getInstance().getWXBridgeManager().callModuleMethod(str, "event", "openURL", jSONArray);
        }
    }
}
