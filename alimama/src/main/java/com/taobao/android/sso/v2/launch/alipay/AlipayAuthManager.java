package com.taobao.android.sso.v2.launch.alipay;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.alipay.auth.mobile.AlipayAuthAPIFactory;
import com.alipay.auth.mobile.api.IAlipayAuthAPI;
import com.alipay.auth.mobile.api.IAlipayAuthMonitor;
import com.alipay.auth.mobile.exception.AlipayAuthIllegalArgumentException;
import java.util.Properties;

public class AlipayAuthManager {
    private static AlipayAuthManager alipayAuthManager;
    private IAlipayAuthAPI mAlipayAuthApi;

    public static AlipayAuthManager getInstance() {
        if (alipayAuthManager == null) {
            alipayAuthManager = new AlipayAuthManager();
        }
        return alipayAuthManager;
    }

    private AlipayAuthManager() {
        initAlipayApi();
    }

    private void initAlipayApi() {
        try {
            this.mAlipayAuthApi = AlipayAuthAPIFactory.createAlipayAuthApi(DataProviderFactory.getApplicationContext(), new IAlipayAuthMonitor() {
                public void monitorAliayAuth(String str, Properties properties) {
                    if (!TextUtils.isEmpty(str)) {
                        UserTrackAdapter.sendUT(str, properties);
                    }
                }
            });
        } catch (AlipayAuthIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public IAlipayAuthAPI getAlipayAuth() {
        if (this.mAlipayAuthApi == null) {
            initAlipayApi();
        }
        return this.mAlipayAuthApi;
    }
}
