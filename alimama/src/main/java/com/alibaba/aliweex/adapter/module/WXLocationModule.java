package com.alibaba.aliweex.adapter.module;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.alibaba.aliweex.AliWXSDKInstance;
import com.alibaba.aliweex.bundle.WeexPageFragment;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

public class WXLocationModule extends WXModule {
    @JSMethod
    public void reload(Boolean bool) {
        Context context = this.mWXSDKInstance.getContext();
        if (context != null) {
            WeexPageFragment findWeexPageFragment = findWeexPageFragment();
            if (findWeexPageFragment != null) {
                findWeexPageFragment.reload();
            } else if (context instanceof ILocationModule) {
                ((ILocationModule) context).reload(bool.booleanValue());
            }
        }
    }

    @JSMethod
    public void replace(String str) {
        Context context;
        if (!TextUtils.isEmpty(str) && (context = this.mWXSDKInstance.getContext()) != null) {
            WeexPageFragment findWeexPageFragment = findWeexPageFragment();
            if (findWeexPageFragment != null) {
                replace(findWeexPageFragment, str);
            } else if (context instanceof ILocationModule) {
                ((ILocationModule) context).replace(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public WeexPageFragment findWeexPageFragment() {
        Fragment findFragmentByTag;
        Context context = this.mWXSDKInstance.getContext();
        String str = WeexPageFragment.FRAGMENT_TAG;
        if (this.mWXSDKInstance instanceof AliWXSDKInstance) {
            String fragmentTag = ((AliWXSDKInstance) this.mWXSDKInstance).getFragmentTag();
            if (!TextUtils.isEmpty(fragmentTag)) {
                str = fragmentTag;
            }
        }
        if (!(context instanceof FragmentActivity) || (findFragmentByTag = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(str)) == null || !(findFragmentByTag instanceof WeexPageFragment)) {
            return null;
        }
        return (WeexPageFragment) findFragmentByTag;
    }

    private void replace(WeexPageFragment weexPageFragment, String str) {
        String str2 = "";
        Uri parse = Uri.parse(str);
        String queryParameter = parse.getQueryParameter("_wx_tpl");
        if (!TextUtils.isEmpty(queryParameter)) {
            str2 = queryParameter;
        } else if ("true".equals(parse.getQueryParameter("wh_weex"))) {
            str2 = str;
        }
        if (!TextUtils.isEmpty(str2) && weexPageFragment != null) {
            weexPageFragment.replace(str, str2);
        }
    }
}
