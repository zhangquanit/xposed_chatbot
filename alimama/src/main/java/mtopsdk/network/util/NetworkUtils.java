package mtopsdk.network.util;

import anet.channel.request.Request;
import java.io.Closeable;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;

public final class NetworkUtils {
    private NetworkUtils() {
    }

    public static boolean requiresRequestBody(String str) {
        return str.equals("POST") || str.equals(Request.Method.PUT) || str.equals("PATCH");
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }

    public static boolean checkContentEncodingGZip(Map<String, List<String>> map) {
        try {
            return "gzip".equalsIgnoreCase(HeaderHandlerUtil.getSingleHeaderFieldByKey(map, "Content-Encoding"));
        } catch (Exception unused) {
            return false;
        }
    }
}
