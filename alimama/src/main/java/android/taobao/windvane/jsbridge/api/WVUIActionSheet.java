package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.view.PopupWindowController;
import android.text.TextUtils;
import android.view.View;

import com.taobao.weex.adapter.IWXUserTrackAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WVUIActionSheet extends WVApiPlugin {
    private static final String TAG = "WVUIActionSheet";
    /* access modifiers changed from: private */
    public String _index;
    /* access modifiers changed from: private */
    public WVCallBackContext mCallback = null;
    /* access modifiers changed from: private */
    public PopupWindowController mPopupWindowController;
    private View.OnClickListener popupClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            WVResult wVResult = new WVResult();
            wVResult.addData("type", (String) view.getTag());
            wVResult.addData("_index", WVUIActionSheet.this._index);
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVUIActionSheet", "ActionSheet: click: 8.5.0");
            }
            WVUIActionSheet.this.mPopupWindowController.hide();
            wVResult.setSuccess();
            WVUIActionSheet.this.mCallback.success(wVResult);
            WVUIActionSheet.this.mCallback.fireEvent("wv.actionsheet", wVResult.toJsonString());
        }
    };

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (!"show".equals(str)) {
            return false;
        }
        show(wVCallBackContext, str2);
        return true;
    }

    public synchronized void show(WVCallBackContext wVCallBackContext, String str) {
        String[] strArr;
        String str2;
        String[] strArr2 = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                String optString = jSONObject.optString("title");
                this._index = jSONObject.optString("_index");
                JSONArray optJSONArray = jSONObject.optJSONArray("buttons");
                if (optJSONArray != null) {
                    if (optJSONArray.length() > 8) {
                        TaoLog.w("WVUIActionSheet", "WVUIDialog: ActionSheet is too long, limit 8");
                        WVResult wVResult = new WVResult();
                        wVResult.setResult("HY_PARAM_ERR");
                        wVResult.addData("msg", "ActionSheet is too long. limit 8");
                        wVCallBackContext.error(wVResult);
                        return;
                    }
                    strArr2 = new String[optJSONArray.length()];
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        strArr2[i] = optJSONArray.optString(i);
                    }
                }
                strArr = strArr2;
                str2 = optString;
            } catch (JSONException unused) {
                TaoLog.e("WVUIActionSheet", "WVUIDialog: param parse to JSON error, param=" + str);
                WVResult wVResult2 = new WVResult();
                wVResult2.setResult("HY_PARAM_ERR");
                wVCallBackContext.error(wVResult2);
                return;
            }
        } else {
            str2 = null;
            strArr = null;
        }
        this.mCallback = wVCallBackContext;
        try {
            this.mPopupWindowController = new PopupWindowController(this.mContext, this.mWebView.getView(), str2, strArr, this.popupClickListener);
            this.mPopupWindowController.show();
            TaoLog.d("WVUIActionSheet", "ActionSheet: show");
        } catch (Exception e) {
            TaoLog.w("WVUIActionSheet", e.getMessage());
            WVResult wVResult3 = new WVResult();
            wVResult3.addData(IWXUserTrackAdapter.MONITOR_ERROR_MSG, e.getMessage());
            wVCallBackContext.error(wVResult3);
        }
    }

    public void onDestroy() {
        this.mCallback = null;
    }
}
