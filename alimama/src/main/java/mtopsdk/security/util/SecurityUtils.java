package mtopsdk.security.util;

import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.security.MessageDigest;
import kotlin.UByte;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;

public class SecurityUtils {
    public static final int DEFAULT_WUA_FLAG = 0;
    public static final int GENERAL_WUA_FLAG = 4;
    public static final int MINI_WUA_FLAG = 8;
    private static final String TAG = "mtopsdk.SecurityUtils";

    public static String convertNull2Default(String str) {
        return str == null ? "" : str;
    }

    public static String getMd5(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            instance.update(str.getBytes("utf-8"));
            byte[] digest = instance.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                StringBuilder sb2 = new StringBuilder(Integer.toHexString(b & UByte.MAX_VALUE));
                while (sb2.length() < 2) {
                    sb2.insert(0, "0");
                }
                sb.append(sb2);
            }
            return sb.toString();
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[getMd5] compute md5 value failed for source str=" + str, (Throwable) e);
            return null;
        }
    }
}
