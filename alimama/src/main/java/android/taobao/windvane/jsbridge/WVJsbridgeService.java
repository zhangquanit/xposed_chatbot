package android.taobao.windvane.jsbridge;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WVJsbridgeService {
    private static List<WVAsyncAuthCheck> mAyncPreprocessor = new CopyOnWriteArrayList();
    private static List<WVJSAPIPageAuth> mPagePreprocessors = new CopyOnWriteArrayList();
    private static List<WVJSAPIAuthCheck> mPreprocessor = new CopyOnWriteArrayList();

    public static void unregisterPreprocessor(WVAsyncAuthCheck wVAsyncAuthCheck) {
        mAyncPreprocessor.remove(wVAsyncAuthCheck);
    }

    public static List<WVAsyncAuthCheck> getJSBridgeayncPreprocessors() {
        return mAyncPreprocessor;
    }

    public static void registerJsbridgePreprocessor(WVAsyncAuthCheck wVAsyncAuthCheck) {
        mAyncPreprocessor.add(wVAsyncAuthCheck);
    }

    public static void unregisterPreprocessor(WVJSAPIAuthCheck wVJSAPIAuthCheck) {
        mPreprocessor.remove(wVJSAPIAuthCheck);
    }

    public static List<WVJSAPIAuthCheck> getJSBridgePreprocessors() {
        return mPreprocessor;
    }

    public static void registerJsbridgePreprocessor(WVJSAPIAuthCheck wVJSAPIAuthCheck) {
        mPreprocessor.add(wVJSAPIAuthCheck);
    }

    public static void registerJsBridgePagePreprocessors(WVJSAPIPageAuth wVJSAPIPageAuth) {
        mPagePreprocessors.add(wVJSAPIPageAuth);
    }

    public static List<WVJSAPIPageAuth> getPagePreprocessors() {
        return mPagePreprocessors;
    }

    public static void unregisterPagePreprocessors(WVJSAPIPageAuth wVJSAPIPageAuth) {
        mPagePreprocessors.remove(wVJSAPIPageAuth);
    }
}
