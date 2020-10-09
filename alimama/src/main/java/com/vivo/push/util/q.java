package com.vivo.push.util;

import android.text.TextUtils;
import com.vivo.push.model.InsideNotificationItem;
import com.vivo.push.model.UPSNotificationMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: MessageConvertUtil */
public final class q {
    public static UPSNotificationMessage a(InsideNotificationItem insideNotificationItem) {
        UPSNotificationMessage uPSNotificationMessage = new UPSNotificationMessage();
        uPSNotificationMessage.setTargetType(insideNotificationItem.getTargetType());
        uPSNotificationMessage.setTragetContext(insideNotificationItem.getTragetContent());
        uPSNotificationMessage.setTitle(insideNotificationItem.getTitle());
        uPSNotificationMessage.setContent(insideNotificationItem.getContent());
        uPSNotificationMessage.setNotifyType(insideNotificationItem.getNotifyType());
        uPSNotificationMessage.setPurePicUrl(insideNotificationItem.getPurePicUrl());
        uPSNotificationMessage.setIconUrl(insideNotificationItem.getIconUrl());
        uPSNotificationMessage.setCoverUrl(insideNotificationItem.getCoverUrl());
        uPSNotificationMessage.setSkipContent(insideNotificationItem.getSkipContent());
        uPSNotificationMessage.setSkipType(insideNotificationItem.getSkipType());
        uPSNotificationMessage.setShowTime(insideNotificationItem.isShowTime());
        uPSNotificationMessage.setMsgId(insideNotificationItem.getMsgId());
        uPSNotificationMessage.setParams(insideNotificationItem.getParams());
        return uPSNotificationMessage;
    }

    public static String b(InsideNotificationItem insideNotificationItem) {
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(insideNotificationItem.getTargetType());
        jSONArray.put(insideNotificationItem.getTragetContent());
        jSONArray.put(insideNotificationItem.getTitle());
        jSONArray.put(insideNotificationItem.getContent());
        jSONArray.put(insideNotificationItem.getNotifyType());
        jSONArray.put(insideNotificationItem.getPurePicUrl());
        jSONArray.put(insideNotificationItem.getIconUrl());
        jSONArray.put(insideNotificationItem.getCoverUrl());
        jSONArray.put(insideNotificationItem.getSkipContent());
        jSONArray.put(insideNotificationItem.getSkipType());
        jSONArray.put(insideNotificationItem.isShowTime());
        if (insideNotificationItem.getParams() != null) {
            jSONArray.put(new JSONObject(insideNotificationItem.getParams()));
        } else {
            jSONArray.put("{}");
        }
        jSONArray.put(insideNotificationItem.getAppType());
        jSONArray.put(insideNotificationItem.getReactPackage());
        jSONArray.put(insideNotificationItem.isShowBigPicOnMobileNet());
        jSONArray.put(insideNotificationItem.getSuitReactVersion());
        jSONArray.put(insideNotificationItem.getMessageType());
        return jSONArray.toString();
    }

    public static InsideNotificationItem a(String str) {
        InsideNotificationItem insideNotificationItem = new InsideNotificationItem();
        try {
            if (TextUtils.isEmpty(str)) {
                p.a("MessageConvertUtil", "notify msg pack to obj is null");
                return null;
            }
            JSONArray jSONArray = new JSONArray(str);
            insideNotificationItem.setTargetType(jSONArray.getInt(0));
            insideNotificationItem.setTragetContext(jSONArray.getString(1));
            insideNotificationItem.setTitle(jSONArray.getString(2));
            insideNotificationItem.setContent(jSONArray.getString(3));
            insideNotificationItem.setNotifyType(jSONArray.getInt(4));
            insideNotificationItem.setPurePicUrl(jSONArray.getString(5));
            insideNotificationItem.setIconUrl(jSONArray.getString(6));
            insideNotificationItem.setCoverUrl(jSONArray.getString(7));
            insideNotificationItem.setSkipContent(jSONArray.getString(8));
            insideNotificationItem.setSkipType(jSONArray.getInt(9));
            insideNotificationItem.setShowTime(jSONArray.getBoolean(10));
            if (jSONArray.length() > 11) {
                insideNotificationItem.setParams(m.a(new JSONObject(jSONArray.getString(11))));
            }
            if (jSONArray.length() > 15) {
                insideNotificationItem.setAppType(jSONArray.getInt(12));
                insideNotificationItem.setReactPackage(jSONArray.getString(13));
                insideNotificationItem.setIsShowBigPicOnMobileNet(jSONArray.getBoolean(14));
                insideNotificationItem.setSuitReactVersion(jSONArray.getString(15));
            }
            if (jSONArray.length() > 16) {
                insideNotificationItem.setMessageType(jSONArray.getInt(16));
            }
            return insideNotificationItem;
        } catch (JSONException e) {
            p.a("MessageConvertUtil", "notify msg pack to obj error", e);
        }
    }
}
