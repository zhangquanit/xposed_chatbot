package com.taobao.weex.common;

import com.taobao.weex.WXEnvironment;
import java.util.Map;

public class WXJSExceptionInfo {
    private String mBundleUrl;
    private WXErrorCode mErrCode;
    private String mException;
    private Map<String, String> mExtParams;
    private String mFunction;
    private String mInstanceId;
    private String mJsFrameworkVersion = WXEnvironment.JS_LIB_SDK_VERSION;
    private String mWeexVersion = WXEnvironment.WXSDK_VERSION;
    public long time;

    public WXJSExceptionInfo(String str, String str2, WXErrorCode wXErrorCode, String str3, String str4, Map<String, String> map) {
        this.mInstanceId = str;
        this.mBundleUrl = str2;
        this.mErrCode = wXErrorCode;
        this.mFunction = str3;
        this.mException = str4;
        this.mExtParams = map;
        this.time = System.currentTimeMillis();
    }

    public String getInstanceId() {
        return this.mInstanceId;
    }

    public void setInstanceId(String str) {
        this.mInstanceId = str;
    }

    public String getBundleUrl() {
        return this.mBundleUrl;
    }

    public void setBundleUrl(String str) {
        this.mBundleUrl = str;
    }

    public WXErrorCode getErrCode() {
        return this.mErrCode;
    }

    public void setErrCode(WXErrorCode wXErrorCode) {
        this.mErrCode = wXErrorCode;
    }

    public String getFunction() {
        return this.mFunction;
    }

    public void setFunction(String str) {
        this.mFunction = str;
    }

    public String getException() {
        return this.mException;
    }

    public void setException(String str) {
        this.mException = str;
    }

    public Map<String, String> getExtParams() {
        return this.mExtParams;
    }

    public void setExtParams(Map<String, String> map) {
        this.mExtParams = map;
    }

    public String getWeexVersion() {
        return this.mWeexVersion;
    }

    public String getJsFrameworkVersion() {
        return this.mJsFrameworkVersion;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" errCode:");
        sb.append(this.mErrCode == null ? "unSetErrorCode" : this.mErrCode.getErrorCode());
        sb.append(",function:");
        sb.append(this.mFunction == null ? "unSetFuncName" : this.mFunction);
        sb.append(",exception:");
        sb.append(this.mException == null ? "unSetException" : this.mException);
        return sb.toString();
    }
}
