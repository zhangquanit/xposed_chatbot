package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.miui.pushads.sdk.f;
import com.xiaomi.miui.pushads.sdk.k;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

class cl {
    public static int a(String str, String str2, ch chVar) {
        LinkedList linkedList = new LinkedList();
        linkedList.add(new BasicNameValuePair("logValue", chVar.f178a));
        linkedList.add(new BasicNameValuePair("appId", str));
        linkedList.add(new BasicNameValuePair("showType", chVar.a + ""));
        linkedList.add(new BasicNameValuePair("s", cm.a(linkedList, str2)));
        try {
            HttpPost httpPost = new HttpPost("http://new.api.ad.xiaomi.com/logNotificationAdActions");
            httpPost.setEntity(new UrlEncodedFormEntity(linkedList, "UTF-8"));
            return 200 == new DefaultHttpClient().execute(httpPost).getStatusLine().getStatusCode() ? 0 : 1;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 1;
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
            return 1;
        } catch (IOException e3) {
            e3.printStackTrace();
            return 1;
        }
    }

    public static boolean a(Context context) {
        return k.a.NONE != f.a(context);
    }
}
