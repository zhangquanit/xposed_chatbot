package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.BindingXJSFunctionRegister;
import com.alibaba.android.bindingx.core.BindingXPropertyInterceptor;
import com.alibaba.android.bindingx.core.IEventHandler;
import com.alibaba.android.bindingx.core.IHandlerCleanable;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractEventHandler implements IEventHandler {
    protected String mAnchorInstanceId;
    private Cache<String, Expression> mCachedExpressionMap = new Cache<>(16);
    protected BindingXCore.JavaScriptCallback mCallback;
    protected Context mContext;
    protected volatile ExpressionPair mExitExpressionPair;
    protected volatile Map<String, List<ExpressionHolder>> mExpressionHoldersMap;
    protected Object[] mExtensionParams;
    protected IHandlerCleanable mHandlerCleaner;
    protected String mInstanceId;
    protected volatile Map<String, ExpressionPair> mInterceptorsMap;
    protected Map<String, Object> mOriginParams;
    protected PlatformManager mPlatformManager;
    protected final Map<String, Object> mScope = new HashMap(64);
    protected String mToken;

    /* access modifiers changed from: protected */
    public abstract void onExit(@NonNull Map<String, Object> map);

    /* access modifiers changed from: protected */
    public abstract void onUserIntercept(String str, @NonNull Map<String, Object> map);

    public void setGlobalConfig(@Nullable Map<String, Object> map) {
    }

    public AbstractEventHandler(Context context, PlatformManager platformManager, Object... objArr) {
        this.mContext = context;
        this.mPlatformManager = platformManager;
        this.mInstanceId = (objArr == null || objArr.length <= 0 || !(objArr[0] instanceof String)) ? null : objArr[0];
    }

    public void setOriginalParams(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            this.mOriginParams = Collections.emptyMap();
        } else {
            this.mOriginParams = map;
        }
    }

    public void setAnchorInstanceId(String str) {
        this.mAnchorInstanceId = str;
    }

    public void onBindExpression(@NonNull String str, @Nullable Map<String, Object> map, @Nullable ExpressionPair expressionPair, @NonNull List<Map<String, Object>> list, @Nullable BindingXCore.JavaScriptCallback javaScriptCallback) {
        clearExpressions();
        transformArgs(str, list);
        this.mCallback = javaScriptCallback;
        this.mExitExpressionPair = expressionPair;
        if (!this.mScope.isEmpty()) {
            this.mScope.clear();
        }
        applyFunctionsToScope();
    }

    @CallSuper
    public void onDestroy() {
        this.mCachedExpressionMap.clear();
        BindingXPropertyInterceptor.getInstance().clearCallbacks();
    }

    private void applyFunctionsToScope() {
        Map<String, JSFunctionInterface> jSFunctions = BindingXJSFunctionRegister.getInstance().getJSFunctions();
        if (jSFunctions != null && !jSFunctions.isEmpty()) {
            this.mScope.putAll(jSFunctions);
        }
    }

    private void transformArgs(@NonNull String str, @NonNull List<Map<String, Object>> list) {
        Map<String, Object> map;
        if (this.mExpressionHoldersMap == null) {
            this.mExpressionHoldersMap = new HashMap();
        }
        for (Map next : list) {
            String stringValue = Utils.getStringValue(next, "element");
            String stringValue2 = Utils.getStringValue(next, BindingXConstants.KEY_INSTANCE_ID);
            String stringValue3 = Utils.getStringValue(next, "property");
            ExpressionPair expressionPair = Utils.getExpressionPair(next, "expression");
            Object obj = next.get(BindingXConstants.KEY_CONFIG);
            if (obj != null && (obj instanceof Map)) {
                try {
                    map = Utils.toMap(new JSONObject((Map) obj));
                } catch (Exception e) {
                    LogProxy.e("parse config failed", e);
                }
                if (!TextUtils.isEmpty(stringValue) || !TextUtils.isEmpty(stringValue3) || expressionPair == null) {
                    LogProxy.e("skip illegal binding args[" + stringValue + "," + stringValue3 + "," + expressionPair + Operators.ARRAY_END_STR);
                } else {
                    ExpressionHolder expressionHolder = new ExpressionHolder(stringValue, stringValue2, expressionPair, stringValue3, str, map);
                    List list2 = this.mExpressionHoldersMap.get(stringValue);
                    if (list2 == null) {
                        ArrayList arrayList = new ArrayList(4);
                        this.mExpressionHoldersMap.put(stringValue, arrayList);
                        arrayList.add(expressionHolder);
                    } else if (!list2.contains(expressionHolder)) {
                        list2.add(expressionHolder);
                    }
                }
            }
            map = null;
            if (!TextUtils.isEmpty(stringValue) && !TextUtils.isEmpty(stringValue3)) {
            }
            LogProxy.e("skip illegal binding args[" + stringValue + "," + stringValue3 + "," + expressionPair + Operators.ARRAY_END_STR);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0022  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean evaluateExitExpression(com.alibaba.android.bindingx.core.internal.ExpressionPair r3, @androidx.annotation.NonNull java.util.Map<java.lang.String, java.lang.Object> r4) {
        /*
            r2 = this;
            boolean r0 = com.alibaba.android.bindingx.core.internal.ExpressionPair.isValid(r3)
            r1 = 0
            if (r0 == 0) goto L_0x001f
            com.alibaba.android.bindingx.core.internal.Expression r3 = com.alibaba.android.bindingx.core.internal.Expression.createFrom(r3)
            if (r3 != 0) goto L_0x000e
            return r1
        L_0x000e:
            java.lang.Object r3 = r3.execute(r4)     // Catch:{ Exception -> 0x0019 }
            java.lang.Boolean r3 = (java.lang.Boolean) r3     // Catch:{ Exception -> 0x0019 }
            boolean r3 = r3.booleanValue()     // Catch:{ Exception -> 0x0019 }
            goto L_0x0020
        L_0x0019:
            r3 = move-exception
            java.lang.String r0 = "evaluateExitExpression failed. "
            com.alibaba.android.bindingx.core.LogProxy.e(r0, r3)
        L_0x001f:
            r3 = 0
        L_0x0020:
            if (r3 == 0) goto L_0x0034
            r2.clearExpressions()
            r2.onExit(r4)     // Catch:{ Exception -> 0x0029 }
            goto L_0x002f
        L_0x0029:
            r4 = move-exception
            java.lang.String r0 = "execute exit expression failed: "
            com.alibaba.android.bindingx.core.LogProxy.e(r0, r4)
        L_0x002f:
            java.lang.String r4 = "exit = true,consume finished"
            com.alibaba.android.bindingx.core.LogProxy.d(r4)
        L_0x0034:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.bindingx.core.internal.AbstractEventHandler.evaluateExitExpression(com.alibaba.android.bindingx.core.internal.ExpressionPair, java.util.Map):boolean");
    }

    public void setInterceptors(@Nullable Map<String, ExpressionPair> map) {
        this.mInterceptorsMap = map;
    }

    public void performInterceptIfNeeded(@NonNull String str, @NonNull ExpressionPair expressionPair, @NonNull Map<String, Object> map) {
        Expression createFrom;
        boolean z;
        if (ExpressionPair.isValid(expressionPair) && (createFrom = Expression.createFrom(expressionPair)) != null) {
            try {
                z = ((Boolean) createFrom.execute(map)).booleanValue();
            } catch (Exception e) {
                LogProxy.e("evaluate interceptor [" + str + "] expression failed. ", e);
                z = false;
            }
            if (z) {
                onUserIntercept(str, map);
            }
        }
    }

    private void tryInterceptAllIfNeeded(@NonNull Map<String, Object> map) {
        if (this.mInterceptorsMap != null && !this.mInterceptorsMap.isEmpty()) {
            for (Map.Entry next : this.mInterceptorsMap.entrySet()) {
                String str = (String) next.getKey();
                ExpressionPair expressionPair = (ExpressionPair) next.getValue();
                if (!TextUtils.isEmpty(str) && expressionPair != null) {
                    performInterceptIfNeeded(str, expressionPair, map);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void consumeExpression(@Nullable Map<String, List<ExpressionHolder>> map, @NonNull Map<String, Object> map2, @NonNull String str) throws IllegalArgumentException, JSONException {
        Map<String, Object> map3 = map2;
        String str2 = str;
        tryInterceptAllIfNeeded(map3);
        if (map == null) {
            LogProxy.e("expression args is null");
        } else if (map.isEmpty()) {
            LogProxy.e("no expression need consumed");
        } else {
            int i = 2;
            if (LogProxy.sEnableLog) {
                LogProxy.d(String.format(Locale.getDefault(), "consume expression with %d tasks. event type is %s", new Object[]{Integer.valueOf(map.size()), str2}));
            }
            LinkedList linkedList = new LinkedList();
            for (List<ExpressionHolder> it : map.values()) {
                for (ExpressionHolder expressionHolder : it) {
                    if (!str2.equals(expressionHolder.eventType)) {
                        LogProxy.d("skip expression with wrong event type.[expected:" + str2 + ",found:" + expressionHolder.eventType + Operators.ARRAY_END_STR);
                    } else {
                        linkedList.clear();
                        if (this.mExtensionParams != null && this.mExtensionParams.length > 0) {
                            Collections.addAll(linkedList, this.mExtensionParams);
                        }
                        String str3 = TextUtils.isEmpty(expressionHolder.targetInstanceId) ? this.mInstanceId : expressionHolder.targetInstanceId;
                        if (!TextUtils.isEmpty(str3)) {
                            linkedList.add(str3);
                        }
                        ExpressionPair expressionPair = expressionHolder.expressionPair;
                        if (ExpressionPair.isValid(expressionPair)) {
                            Expression expression = (Expression) this.mCachedExpressionMap.get(expressionPair.transformed);
                            if (expression == null) {
                                expression = Expression.createFrom(expressionPair);
                                if (expression != null) {
                                    if (!TextUtils.isEmpty(expressionPair.transformed)) {
                                        this.mCachedExpressionMap.put(expressionPair.transformed, expression);
                                    }
                                }
                            }
                            Object execute = expression.execute(map3);
                            if (execute == null) {
                                LogProxy.e("failed to execute expression,expression result is null");
                            } else if ((!(execute instanceof Double) || !Double.isNaN(((Double) execute).doubleValue())) && (!(execute instanceof Float) || !Float.isNaN(((Float) execute).floatValue()))) {
                                View findViewBy = this.mPlatformManager.getViewFinder().findViewBy(expressionHolder.targetRef, linkedList.toArray());
                                BindingXPropertyInterceptor instance = BindingXPropertyInterceptor.getInstance();
                                String str4 = expressionHolder.prop;
                                PlatformManager.IDeviceResolutionTranslator resolutionTranslator = this.mPlatformManager.getResolutionTranslator();
                                Map<String, Object> map4 = expressionHolder.config;
                                Object[] objArr = new Object[i];
                                objArr[0] = expressionHolder.targetRef;
                                objArr[1] = str3;
                                instance.performIntercept(findViewBy, str4, execute, resolutionTranslator, map4, objArr);
                                if (findViewBy == null) {
                                    LogProxy.e("failed to execute expression,target view not found.[ref:" + expressionHolder.targetRef + Operators.ARRAY_END_STR);
                                    i = 2;
                                } else {
                                    i = 2;
                                    this.mPlatformManager.getViewUpdater().synchronouslyUpdateViewOnUIThread(findViewBy, expressionHolder.prop, execute, this.mPlatformManager.getResolutionTranslator(), expressionHolder.config, expressionHolder.targetRef, str3);
                                }
                            } else {
                                LogProxy.e("failed to execute expression,expression result is NaN");
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void clearExpressions() {
        LogProxy.d("all expression are cleared");
        if (this.mExpressionHoldersMap != null) {
            this.mExpressionHoldersMap.clear();
            this.mExpressionHoldersMap = null;
        }
        this.mExitExpressionPair = null;
    }

    public void setToken(String str) {
        this.mToken = str;
    }

    public void setExtensionParams(Object[] objArr) {
        this.mExtensionParams = objArr;
    }

    public void setHandlerCleaner(IHandlerCleanable iHandlerCleanable) {
        this.mHandlerCleaner = iHandlerCleanable;
    }

    static class Cache<K, V> extends LinkedHashMap<K, V> {
        private int maxSize;

        Cache(int i) {
            super(4, 0.75f, true);
            this.maxSize = Math.max(i, 4);
        }

        /* access modifiers changed from: protected */
        public boolean removeEldestEntry(Map.Entry entry) {
            return size() > this.maxSize;
        }
    }
}
