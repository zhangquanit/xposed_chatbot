package android.taobao.windvane.config;

import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVConstants;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class WVServerConfig {
    public static String ALLOW_ACCESS_DOMAIN_PATTERN = "";
    public static boolean CACHE = true;
    public static String DOMAIN_PATTERN = WVConstants.DEFAULT_DOMAIN_PATTERN;
    public static String FORBIDDEN_DOMAIN_PATTERN = "";
    public static boolean LOG = false;
    public static boolean STATISTICS = false;
    public static String SUPPORT_DOWNLOAD_DOMAIN_PATTERN = "";
    public static String THIRD_PARTY_DOMAIN_PATTERN = WVConstants.THIRD_PARTY_DOMAIN_PATTERN;
    public static boolean URL_FILTER = true;
    public static Pattern allowAccessDomain = null;
    public static Pattern domainPat = null;
    public static Pattern forbiddenDomain = null;
    public static Pattern supportDownloadDomain = null;
    public static Pattern thirdPartyDomain = null;
    public static String v = "0";

    static {
        try {
            updateGlobalConfig(ConfigStorage.getStringVal("WVURLCacheDefault", ConfigStorage.KEY_DATA));
        } catch (Exception unused) {
        }
    }

    public static boolean isTrustedUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (domainPat == null) {
            if (TextUtils.isEmpty(DOMAIN_PATTERN)) {
                DOMAIN_PATTERN = WVConstants.DEFAULT_DOMAIN_PATTERN;
            }
            try {
                domainPat = Pattern.compile(DOMAIN_PATTERN, 2);
                TaoLog.d("WVServerConfig", "compile pattern domainPat rule, " + DOMAIN_PATTERN);
            } catch (PatternSyntaxException e) {
                TaoLog.e("WVServerConfig", " PatternSyntaxException pattern:" + e.getMessage());
            }
        }
        try {
            if (domainPat != null) {
                return domainPat.matcher(str).matches();
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public static boolean isThirdPartyUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (thirdPartyDomain == null) {
            if (TextUtils.isEmpty(THIRD_PARTY_DOMAIN_PATTERN)) {
                THIRD_PARTY_DOMAIN_PATTERN = WVConstants.THIRD_PARTY_DOMAIN_PATTERN;
            }
            try {
                thirdPartyDomain = Pattern.compile(THIRD_PARTY_DOMAIN_PATTERN, 2);
                TaoLog.d("WVServerConfig", "compile pattern thirdPartyDomain rule, " + THIRD_PARTY_DOMAIN_PATTERN);
            } catch (PatternSyntaxException e) {
                TaoLog.e("WVServerConfig", " PatternSyntaxException pattern:" + e.getMessage());
            }
        }
        try {
            if (thirdPartyDomain != null) {
                return thirdPartyDomain.matcher(str).matches();
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public static boolean isBlackUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (forbiddenDomain == null) {
            if (TextUtils.isEmpty(FORBIDDEN_DOMAIN_PATTERN)) {
                FORBIDDEN_DOMAIN_PATTERN = "";
            }
            try {
                forbiddenDomain = Pattern.compile(FORBIDDEN_DOMAIN_PATTERN, 2);
                TaoLog.d("WVServerConfig", "compile pattern black rule, " + FORBIDDEN_DOMAIN_PATTERN);
            } catch (PatternSyntaxException e) {
                TaoLog.e("WVServerConfig", " PatternSyntaxException pattern:" + e.getMessage());
            }
        }
        try {
            if (!TextUtils.isEmpty(str)) {
                return forbiddenDomain.matcher(str).matches();
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public static boolean isSupportDownload(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (supportDownloadDomain == null) {
            if (TextUtils.isEmpty(SUPPORT_DOWNLOAD_DOMAIN_PATTERN)) {
                SUPPORT_DOWNLOAD_DOMAIN_PATTERN = "";
            }
            try {
                supportDownloadDomain = Pattern.compile(SUPPORT_DOWNLOAD_DOMAIN_PATTERN, 2);
                TaoLog.d("WVServerConfig", "compile pattern supportDownloadDomain rule, " + SUPPORT_DOWNLOAD_DOMAIN_PATTERN);
            } catch (PatternSyntaxException e) {
                TaoLog.e("WVServerConfig", " PatternSyntaxException pattern:" + e.getMessage());
            }
        }
        try {
            if (!TextUtils.isEmpty(str)) {
                return supportDownloadDomain.matcher(str).matches();
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public static boolean isAllowAccess(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (allowAccessDomain == null) {
            if (TextUtils.isEmpty(ALLOW_ACCESS_DOMAIN_PATTERN)) {
                ALLOW_ACCESS_DOMAIN_PATTERN = "";
            }
            try {
                allowAccessDomain = Pattern.compile(ALLOW_ACCESS_DOMAIN_PATTERN, 2);
                TaoLog.d("WVServerConfig", "compile pattern allowAccessDomain rule, " + ALLOW_ACCESS_DOMAIN_PATTERN);
            } catch (PatternSyntaxException e) {
                TaoLog.e("WVServerConfig", " PatternSyntaxException pattern:" + e.getMessage());
            }
        }
        try {
            if (!TextUtils.isEmpty(str)) {
                return allowAccessDomain.matcher(str).matches();
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public static boolean updateGlobalConfig(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ApiResponse apiResponse = new ApiResponse();
        JSONObject jSONObject = apiResponse.parseJsonResult(str).success ? apiResponse.data : null;
        if (jSONObject == null) {
            return false;
        }
        CACHE = jSONObject.optInt("lock", 0) == 0;
        LOG = jSONObject.optInt("log") == 1;
        if (jSONObject.optInt("statistics") == 1) {
            z = true;
        }
        STATISTICS = z;
        DOMAIN_PATTERN = jSONObject.optString("alidomain");
        domainPat = null;
        return true;
    }
}
