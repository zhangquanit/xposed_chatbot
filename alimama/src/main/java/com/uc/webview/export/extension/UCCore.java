package com.uc.webview.export.extension;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.alipay.sdk.util.e;
import com.uc.webview.export.WebResourceResponse;
import com.uc.webview.export.WebView;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.annotations.Interface;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.cd.a;
import com.uc.webview.export.internal.interfaces.INetLogger;
import com.uc.webview.export.internal.interfaces.INetwork;
import com.uc.webview.export.internal.interfaces.INetworkDecider;
import com.uc.webview.export.internal.interfaces.INetworkDelegate;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.interfaces.InvokeObject;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.setup.af;
import com.uc.webview.export.internal.setup.ak;
import com.uc.webview.export.internal.setup.bw;
import com.uc.webview.export.internal.setup.l;
import com.uc.webview.export.internal.setup.o;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import com.uc.webview.export.internal.utility.d;
import com.uc.webview.export.internal.utility.i;
import com.uc.webview.export.internal.utility.k;
import com.uc.webview.export.media.MediaPlayerFactory;
import com.uc.webview.export.utility.SetupTask;
import com.uc.webview.export.utility.download.UpdateTask;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Api
/* compiled from: U4Source */
public class UCCore {
    public static final int AC_FLAG_DEFAULT = -1;
    public static final int AC_FLAG_HAC = 1;
    public static final int AC_FLAG_SWAC = 0;
    public static final String ADAPTER_BUILD_TIMING = "adapter_build_timing";
    public static final String ADAPTER_BUILD_VERSOPM = "adapter_build_version";
    public static final String BUSINESS_INIT_BY_NEW_CORE_DEX_DIR = "bit_by_new_dex_dir";
    public static final String BUSINESS_INIT_BY_NEW_CORE_ZIP_FILE = "bit_by_new_zip_file";
    public static final String BUSINESS_INIT_BY_OLD_CORE_DEX_DIR = "bit_by_old_dex_dir";
    public static final String CD_DISABLE_UCDNS = "disable_ucdns";
    public static final String CD_ENABLE_NET_THREAD_REDUCE = "dec_thread";
    public static final String CD_ENABLE_TRAFFIC_STAT = "traffic_stat";
    public static final int COMPATIBLE_POLICY_ALL = 7;
    public static final int COMPATIBLE_POLICY_ARMV5 = 1;
    public static final int COMPATIBLE_POLICY_ARMV7 = 2;
    public static final int COMPATIBLE_POLICY_X86 = 4;
    public static final int CORE_EVENT_CLEAR_DNS_CACHE = 0;
    public static final int CORE_EVENT_CLEAR_HTTP_CACHE = 3;
    public static final int CORE_EVENT_CONSOLE_CALLBACK = 6;
    public static final int CORE_EVENT_DELETE_SERVICEWORKER_CACHE = 4;
    public static final int CORE_EVENT_DELETE_SERVICEWORKER_CACHE_SYNC = 9;
    public static final int CORE_EVENT_DISPATCH_WEBGL_EVENT = 18;
    public static final int CORE_EVENT_GET_HTTP_CACHE_SIZE = 1;
    public static final int CORE_EVENT_HTTP2_HOST_LIST = 8;
    public static final int CORE_EVENT_INIT_WPK = 13;
    public static final int CORE_EVENT_MAX_REQUEST_LIMITATION = 7;
    public static final int CORE_EVENT_ON_ACTIVITY_RECREATE = 11;
    public static final int CORE_EVENT_PUSH_DNS_RESULT = 5;
    public static final int CORE_EVENT_SERVICEWORKER_PUSHMESSAGE = 2;
    public static final int CORE_EVENT_SET_MAX_CACHESIZE = 10;
    public static final int CORE_EVENT_SET_WPK_CALLBACK = 14;
    public static final int CORE_EVENT_SET_WPK_COMMON_CUSTOM_FILELDS = 15;
    public static final int CORE_EVENT_SET_WPK_CONFIGS = 16;
    public static final int DELETE_CORE_POLICY_ALL = 63;
    public static final int DELETE_CORE_POLICY_FILE_VERIFY_FAILED = 16;
    public static final int DELETE_CORE_POLICY_LOAD_SO_ERROR = 2;
    public static final int DELETE_CORE_POLICY_MULTI_CRASH = 4;
    public static final int DELETE_CORE_POLICY_NONE = 0;
    public static final int DELETE_CORE_POLICY_OTHER = 32;
    public static final int DELETE_CORE_POLICY_SO_HASH_MISMATCH = 8;
    public static final int DELETE_CORE_POLICY_SO_SIZE_MISMATCH = 1;
    public static final int DEX2OAT_POLICY_DEFAULT = 0;
    public static final int DEX2OAT_POLICY_DELAY = 1;
    public static final String ENABLE_WEBVIEW_LISTENER_STANDARDIZATION_OPTION = "enable_webview_listener_standardization";
    public static final String EVENT_COST = "cost";
    public static final String EVENT_DELAY_SEARCH_CORE_FILE = "e_delay_search_core_file";
    public static final String EVENT_DELETE_FILE_FINISH = "be_del_fi";
    public static final String EVENT_DIE = "die";
    public static final String EVENT_DOWNLOAD_EXCEPTION = "downloadException";
    public static final String EVENT_DOWNLOAD_FILE_DELETE = "downloadFileDelete";
    public static final String EVENT_EXCEPTION = "exception";
    public static final String EVENT_GONE = "gone";
    public static final String EVENT_INIT_CORE_EXCEPTON = "be_init_exception";
    public static final String EVENT_INIT_CORE_SUCCESS = "be_init_success";
    public static final String EVENT_ODEX_DONE = "e_odex_done";
    public static final String EVENT_PAUSE = "pause";
    public static final String EVENT_PROGRESS = "progress";
    public static final String EVENT_REPAIR = "repair";
    public static final String EVENT_RESUME = "resume";
    public static final String EVENT_START = "start";
    public static final String EVENT_STAT = "stat";
    public static final String EVENT_STOP = "stop";
    public static final String EVENT_SUCCESS = "success";
    public static final String EVENT_UPDATE_PROGRESS = "updateProgress";
    public static final String EVENT_UPDATE_SHARE_CORE = "shareCoreEvt";
    public static final String GLOBAL_OPTION_IS_BROWSER_RUNTIME = "go_is_bw_rt";
    public static final int GPU_PROCESS_FULL = 2;
    public static final int GPU_PROCESS_IN_PROCESS = 1;
    public static final int GPU_PROCESS_NONE = 0;
    public static final String LEGACY_EVENT_INIT = "init";
    public static final String LEGACY_EVENT_LOAD = "load";
    public static final String LEGACY_EVENT_SETUP = "setup";
    public static final String LEGACY_EVENT_SWITCH = "switch";
    public static final String LOAD_POLICY_SPECIFIED_ONLY = "SPECIFIED_ONLY";
    public static final String LOAD_POLICY_SPECIFIED_OR_UCMOBILE = "SPECIFIED_OR_UCMOBILE";
    public static final String LOAD_POLICY_UCMOBILE_ONLY = "UCMOBILE_ONLY";
    public static final String LOAD_POLICY_UCMOBILE_OR_SPECIFIED = "UCMOBILE_OR_SPECIFIED";
    public static final String OPTION_APP_STARTUP_OPPORTUNITY = "app_st_o";
    public static final String OPTION_APP_STARTUP_TIME = "app_st_t";
    public static final String OPTION_BUSINESS_DECOMPRESS_ROOT_PATH = "bo_dec_r_p";
    public static final String OPTION_BUSINESS_INIT_TYPE = "bo_init_type";
    public static final String OPTION_CHECK_DECOMPRESS_FINISH = "chkDecFinish";
    public static final String OPTION_CHECK_MULTI_CORE = "chkMultiCore";
    public static final String OPTION_COMPATIBLE_POLICY = "COMPATIBLE_POLICY";
    public static final String OPTION_CONNECTION_CONNECT_TIMEOUT = "conn_to";
    public static final String OPTION_CONNECTION_READ_TIMEOUT = "read_to";
    public static final String OPTION_CONTEXT = "CONTEXT";
    public static final String OPTION_CONTINUE_ODEX_ON_DECOMPRESSED = "bo_continue_odex";
    public static final String OPTION_CORE_VERSION_EXCLUDE = "core_ver_excludes";
    public static final String OPTION_CURRENT_IS_UC_CORE = "o_st_cisuc";
    public static final String OPTION_DECOMPRESS_AND_ODEX_CALLBACK = "bo_dec_odex_cb";
    public static final String OPTION_DECOMPRESS_AND_ODEX_TASK_WAIT_MILIS = "bo_dec_odex_wm";
    public static final String OPTION_DECOMPRESS_CALLBACK = "bo_dec_cl";
    public static final String OPTION_DECOMPRESS_ROOT_DIR = "bo_dec_root_dir";
    public static final String OPTION_DEC_FILE = "o_dec_file";
    public static final String OPTION_DELETE_AFTER_EXTRACT = "bo_del_aft_extract";
    public static final String OPTION_DELETE_CORE_POLICY = "delete_core";
    public static final String OPTION_DELETE_OLD_DEX_DIR = "bo_dex_old_dex_dir";
    public static final String OPTION_DEX2OAT_POLICY = "DEX2OAT_POLICY";
    public static final String OPTION_DEX_FILE_PATH = "dexFilePath";
    public static final String OPTION_DISTINGUISH_JS_ERROR = "distinguish_js_error";
    public static final String OPTION_DOWNLOAD_CHECKER = "dlChecker";
    public static final String OPTION_DWN_RETRY_MAX_WAIT_MILIS = "dwnRetryMaxWait";
    public static final String OPTION_DWN_RETRY_WAIT_MILIS = "dwnRetryWait";
    public static final String OPTION_ENABLE_LOAD_CLASS = "bo_enable_load_class";
    public static final String OPTION_EXACT_LAST_MODIFIED_CHECK = "exact_mod";
    public static final String OPTION_EXACT_OLD_KERNEL_CHECK = "exact_old";
    public static final String OPTION_FIRST_USE_SYSTEM_WEBVIEW = "first_use_sw";
    public static final String OPTION_FORBID_GEN_REPAIR_DIR = "forbid_repair";
    public static final String OPTION_FORCE_USE_BUSINESS_DECOMPRESS_ROOT_PATH = "bo_f_u_dec_r_p";
    public static final String OPTION_GPU_PROCESS_DISABLE_WATCHDOG_BEFORE_LOAD_URL = "ucm_multi_gpu_process_disable_watchdog_before_launcher_start";
    public static final String OPTION_GPU_PROCESS_MODE = "gpu_process_mode";
    public static final String OPTION_GPU_WARM_UP_TIME = "ucm_gpu_warm_up_time";
    public static final String OPTION_GRANT_ALL_BUILDS = "grant_all_builds";
    public static final String OPTION_HARDWARE_ACCELERATED = "AC";
    public static final String OPTION_HAS_UPDATE_SOURCE = "o_st_hupds";
    @Deprecated
    public static final String OPTION_INIT_IN_SETUP_THREAD = "init_setup_thread";
    public static final String OPTION_INJECT_LIBRARY_PATH_CALLBACK = "e_in_lp_cb";
    public static final String OPTION_LOAD_KERNEL_TYPE = "load";
    public static final String OPTION_LOAD_POLICY = "loadPolicy";
    public static final String OPTION_LOAD_SHARE_CORE_HOST = "load_share_core_host";
    public static final String OPTION_LOCAL_DIR = "o_local_dir";
    public static final String OPTION_LOG_CONFIG = "log_conf";
    public static final String OPTION_MULTI_CORE_TYPE = "MULTI_CORE_TYPE";
    public static final String OPTION_MULTI_PROCESS_DISABLE_FALLBACK_TO_SINGLE_PROCESS = "ucm_multi_process_disable_fallback_to_single_process";
    public static final String OPTION_MULTI_PROCESS_STARTUP_TIMEOUT = "ucm_multi_process_startup_timeout";
    public static final String OPTION_MULTI_UNKNOWN_CRASH_DISABLE = "disable_multi_unknown_crash";
    public static final String OPTION_NEW_UCM_ZIP_FILE = "bo_new_ucm_zf";
    public static final String OPTION_NEW_UCM_ZIP_TYPE = "bo_new_ucm_z_type";
    public static final String OPTION_NOT_SWITCH_UCCORE = "oNotSwUCCore";
    public static final String OPTION_NOT_USE_7Z_CORE = "not_use_7z_core";
    public static final String OPTION_OLD_DEX_DIR_PATH = "bo_old_dex_dp";
    public static final String OPTION_ONLY_STAT_DEVICES_HAS_CORE_SHARE = "o_st_dhcs";
    public static final String OPTION_PRECREATE_WEBVIEW = "precreate_webview";
    public static final String OPTION_PRECREATE_WEBVIEW_URL = "precreate_webview_url";
    public static final String OPTION_PRIVATE_DATA_DIRECTORY_SUFFIX = "PRIVATE_DATA_DIRECTORY_SUFFIX";
    public static final String OPTION_PROMISE_SPECIAL_VERSION_CORE_INIT = "bo_prom_sp_v_c_i";
    public static final String OPTION_PROVIDED_KEYS = "provided_keys";
    public static final String OPTION_RES_FILE_PATH = "resFilePath";
    public static final String OPTION_ROOT_TASK_KEY = "root_task_key";
    public static final String OPTION_SDK_INTERNATIONAL_ENV = "sdk_international_env";
    public static final String OPTION_SDK_VERSION_EXCLUDE = "sdk_ver_excludes";
    public static final String OPTION_SETUP_CREATE_THREAD = "setup_create_thread";
    public static final String OPTION_SETUP_THREAD_PRIORITY = "setup_priority";
    public static final String OPTION_SET_ODEX_ROOT_PATH = "set_odex_path";
    public static final String OPTION_SHARE_CORE_SETUP_TASK_FLAG = "scst_flag";
    public static final String OPTION_SKIP_OLD_KERNEL = "skip_old_extra_kernel";
    public static final String OPTION_SKIP_PRECONDITIONS_IO_CHECK = "bo_skip_io_dc";
    public static final String OPTION_SO_FILE_PATH = "soFilePath";
    public static final String OPTION_SPEEDUP_DEXOPT_POLICY = "speedup_dexopt";
    public static final String OPTION_STARTUP_POLICY = "startup_policy";
    public static final String OPTION_START_INIT_UC_CORE = "bo_s_i_uc_core";
    public static final String OPTION_THICK_INTEGRATION = "THICK_INTEGRATION";
    public static final String OPTION_UCMOBILE_INIT = "bo_ucm_init";
    public static final String OPTION_UCM_CFG_FILE = "ucmCfgFile";
    public static final String OPTION_UCM_KRL_DIR = "ucmKrlDir";
    public static final String OPTION_UCM_LIB_DIR = "ucmLibDir";
    public static final String OPTION_UCM_PATCH_DIR = "ucmPatDir";
    public static final String OPTION_UCM_UPD_URL = "ucmUpdUrl";
    public static final String OPTION_UCM_ZIP_DIR = "ucmZipDir";
    public static final String OPTION_UCM_ZIP_FILE = "ucmZipFile";
    public static final String OPTION_UC_PLAYER_ROOT = "ucPlayerRoot";
    public static final String OPTION_UC_PROXY_ADBLOCK = "proxy_adblock";
    public static final String OPTION_UPDATE_PROCESS_LOCK = "upd_pro_lk";
    public static final String OPTION_UPD_SETUP_TASK_WAIT_MILIS = "updWait";
    public static final String OPTION_USE_SDK_SETUP = "sdk_setup";
    public static final String OPTION_USE_SYSTEM_WEBVIEW = "SYSTEM_WEBVIEW";
    public static final String OPTION_USE_UC_PLAYER = "ucPlayer";
    public static final String OPTION_VERIFY_POLICY = "VERIFY_POLICY";
    public static final String OPTION_VIDEO_HARDWARE_ACCELERATED = "VIDEO_AC";
    public static final String OPTION_VMSIZE_SAVING = "vmsize_saving";
    public static final String OPTION_WAP_DENY = "wap_deny";
    public static final String OPTION_WEBVIEW_MULTI_PROCESS = "webview_multi_process";
    public static final String OPTION_WEBVIEW_MULTI_PROCESS_ENABLE_SECCOMP = "ucm_multi_process_enable_seccomp";
    public static final String OPTION_WEBVIEW_MULTI_PROCESS_ENABLE_SERVICE_SPEEDUP = "webview_multi_process_enable_service_speedup";
    public static final String OPTION_WEBVIEW_MULTI_PROCESS_FALLBACK_TIMEOUT = "webview_multi_process_fallback_timeout";
    public static final String OPTION_WEBVIEW_POLICY = "WEBVIEW_POLICY";
    public static final String OPTION_WEBVIEW_POLICY_WAIT_MILLIS = "wait_fallback_sys";
    public static final String OPTION_ZIP_FILE_TYPE = "o_zio_file_type";
    public static final int PRE_INIT_EVENT_DECOMPRESS = 0;
    public static final int PRE_INIT_EVENT_ICU = 9;
    public static final int PRE_INIT_EVENT_INIT_WEBVIEW_PROVIDER = 6;
    public static final int PRE_INIT_EVENT_LOAD_CORE_CLASS = 3;
    public static final int PRE_INIT_EVENT_LOAD_IO = 1;
    public static final int PRE_INIT_EVENT_LOAD_JAR = 4;
    public static final int PRE_INIT_EVENT_LOAD_SDK_CLASS = 2;
    public static final int PRE_INIT_EVENT_LOAD_SO = 5;
    public static final int PRE_INIT_EVENT_PAK = 8;
    public static final int PRE_INIT_EVENT_PRE_PROCESS = 10;
    public static final int PRE_INIT_EVENT_START_CORE_ENGINE = 7;
    public static final String PROCESS_PRIVATE_DATA_DIR_SUFFIX_OPTION = "process_private_data_dir_suffix";
    public static final int SPEEDUP_DEXOPT_POLICY_ALL = 2047;
    public static final int SPEEDUP_DEXOPT_POLICY_ART = 1920;
    public static final int SPEEDUP_DEXOPT_POLICY_DAVIK = 127;
    public static final int SPEEDUP_DEXOPT_POLICY_NONE = 0;
    public static final String STARTUP_ELAPSE_BEETWEEN_UC_INIT_AND_APP = "st_el_b_uc_and_app";
    public static final int STARTUP_POLICY_ALL = 16;
    public static final int STARTUP_POLICY_CREATE_WEBVIEW_PARALLEL = 16;
    public static final int STARTUP_POLICY_DEFAULT = 16;
    public static final int STARTUP_POLICY_DISABLE_QUICK_PATH = 128;
    public static final int STARTUP_POLICY_NONE = 0;
    public static final int VERIFY_POLICY_ALL = 1073741871;
    public static final int VERIFY_POLICY_ALL_FULL_HASH = -1073741697;
    public static final int VERIFY_POLICY_ASYNC = Integer.MIN_VALUE;
    public static final int VERIFY_POLICY_BROWSER_IF = 2;
    public static final int VERIFY_POLICY_CORE_IMPL = 4;
    public static final int VERIFY_POLICY_NONE = 0;
    public static final int VERIFY_POLICY_PAK = 32;
    public static final int VERIFY_POLICY_PAK_FULL_HASH = 64;
    public static final int VERIFY_POLICY_PAK_QUICK = 536870912;
    public static final int VERIFY_POLICY_QUICK = 1073741824;
    public static final int VERIFY_POLICY_SDK_SHELL = 1;
    public static final int VERIFY_POLICY_SO = 8;
    public static final int VERIFY_POLICY_SO_FULL_HASH = 16;
    public static final int VERIFY_POLICY_SO_QUICK = 268435456;
    public static final int VERIFY_POLICY_WITH_MD5 = 1048576;
    public static final int VERIFY_POLICY_WITH_SHA1 = 2097152;
    public static final int VERIFY_POLICY_WITH_SHA256 = 4194304;
    public static final int VIDEO_AC_FLAG_DEFAULT = -1;
    public static final int VIDEO_AC_FLAG_HAC = 1;
    public static final int VIDEO_AC_FLAG_SWAC = 0;
    public static final int WEBVIEW_MULTI_PROCESS_ISOLATE = 2;
    public static final int WEBVIEW_MULTI_PROCESS_NONE = 0;
    public static final int WEBVIEW_MULTI_PROCESS_NORMAL = 1;
    public static final int WEBVIEW_POLICY_WAIT_UNTIL_EXCEPTION = 3;
    public static final int WEBVIEW_POLICY_WAIT_UNTIL_FALLBACK_SYSTEM = 2;
    public static final int WEBVIEW_POLICY_WAIT_UNTIL_LOADED = 1;
    private static AtomicBoolean a = new AtomicBoolean(false);

    @Interface
    /* compiled from: U4Source */
    public interface Callable<V, T> {
        V call(T t) throws Exception;
    }

    @Deprecated
    public static void uploadCrashLogs() {
    }

    public static void warmUpGpuProcess() {
        af.f();
    }

    public static boolean setForbidHomoDisabler(Context context, boolean z) {
        try {
            File file = new File(new File(context.getApplicationInfo().dataDir), "forbid_homodisabler");
            if (z) {
                if (!file.exists()) {
                    file.createNewFile();
                }
                return file.exists();
            }
            if (file.exists()) {
                file.delete();
            }
            if (!file.exists()) {
                return true;
            }
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static SetupTask setup(String str, Object obj) {
        Object[] objArr = new Object[1];
        objArr[0] = obj instanceof Context ? obj : null;
        af.a(9, objArr);
        return (SetupTask) o.a().setup(str, obj);
    }

    public static void update(Context context, String str, java.util.concurrent.Callable<Boolean> callable) throws UCSetupException {
        o.a().a(str, callable);
    }

    public static void updateUCCore(Context context, String str, java.util.concurrent.Callable<Boolean> callable, Map<String, ValueCallback> map, Map<String, Object> map2) throws UCSetupException {
        i.b(new a(context, map, callable, str, map2));
    }

    public static void updateUCPlayer(Context context, String str, java.util.concurrent.Callable<Boolean> callable) throws UCSetupException {
        updateUCPlayer(context, str, callable, (Map<String, ValueCallback>) null);
    }

    public static void updateUCPlayer(Context context, String str, java.util.concurrent.Callable<Boolean> callable, Map<String, ValueCallback> map) throws UCSetupException {
        if (!k.d(context)) {
            Log.e("ucmedia.UCCore", "ignore updateUCPlayer in process " + k.c(context));
        } else if (a.getAndSet(true)) {
            Log.e("ucmedia.UCCore", "want to updateUCPlayer again - " + str);
        } else {
            i.b(new f(context, str, callable, map));
        }
    }

    public static void setLocationManager(ILocationManager iLocationManager) {
        if (SDKFactory.d != null) {
            SDKFactory.d.setLocationManagerUC(iLocationManager);
        }
    }

    public static void onLowMemory() {
        if (SDKFactory.d != null) {
            try {
                SDKFactory.d.onLowMemory();
            } catch (Throwable unused) {
            }
        }
    }

    public static void onTrimMemory(int i) {
        if (SDKFactory.d != null) {
            try {
                SDKFactory.d.onTrimMemory(i);
            } catch (Throwable unused) {
            }
        }
    }

    public static void setNotAvailableUCListener(NotAvailableUCListener notAvailableUCListener) {
        SDKFactory.a = notAvailableUCListener;
    }

    public static String getCoreInfo() throws RuntimeException {
        return SDKFactory.c();
    }

    public static void setPrintLog(boolean z) {
        com.uc.webview.export.internal.utility.Log.setPrintLog(z, new Object[]{Boolean.valueOf(z), true, null, "[all]", "[all]"});
    }

    public static void setDrawableResource(String str, Drawable drawable) {
        if (SDKFactory.d != null) {
            SDKFactory.d.getWebResources().setDrawable(str, drawable);
        }
    }

    public static Pair<Long, Long> getTraffic() {
        return SDKFactory.h();
    }

    public static WebResourceResponse getResponseByUrl(String str) {
        return SDKFactory.b(str);
    }

    public static void setThirdNetwork(INetwork iNetwork, INetworkDecider iNetworkDecider) {
        if (WebView.getCoreType() != 2 && SDKFactory.d != null) {
            SDKFactory.d.setThirdNetwork(iNetwork, iNetworkDecider);
        }
    }

    public static void setNetworkDelegate(INetworkDelegate iNetworkDelegate) {
        if (SDKFactory.d != null) {
            com.uc.webview.export.internal.utility.Log.e("network delegate", "UCCore U4 setNetworkDelegate");
            try {
                SDKFactory.d.setNetworkDelegate(iNetworkDelegate);
            } catch (Exception e) {
                com.uc.webview.export.internal.utility.Log.e("network delegate", "setNetworkDelegate", e);
            }
        }
    }

    public static void setNetLogger(INetLogger iNetLogger) {
        SDKFactory.d.setNetLogger(iNetLogger);
    }

    public static boolean extractWebCoreLibraryIfNeeded(Context context, String str, String str2, boolean z) throws UCSetupException {
        b.a(20);
        boolean decompressIfNeeded = UCCyclone.decompressIfNeeded(context, true, new File(str), new File(str2), (FilenameFilter) null, z);
        b.a(21);
        b.a(19, decompressIfNeeded ? "1" : "0");
        return decompressIfNeeded;
    }

    public static boolean extractWebCoreLibraryIfNeeded(Context context, String str, String str2, String str3, boolean z) throws UCSetupException {
        b.a(20);
        boolean decompressIfNeeded = UCCyclone.decompressIfNeeded(context, str2, new File(str), new File(str3), (FilenameFilter) null, z);
        b.a(21);
        b.a(19, decompressIfNeeded ? "1" : "0");
        return decompressIfNeeded;
    }

    public static void setInitCallback(InitCallback initCallback) {
        SDKFactory.i = initCallback;
    }

    public static void setStatDataCallback(ValueCallback<String> valueCallback) {
        SDKFactory.m = valueCallback;
    }

    public static void setStatDataCheckCallback(ValueCallback<String> valueCallback) {
        SDKFactory.n = valueCallback;
    }

    public static void setSetupExceptionListener(ValueCallback<UCSetupException> valueCallback) {
        SDKFactory.o = valueCallback;
    }

    public static void setParam(String str) {
        a.a(str);
    }

    public static String getParam(String str) {
        return a.b(str);
    }

    public static Object notifyCoreEvent(int i, Object obj) {
        if (WebView.getCoreType() == 1 && SDKFactory.d != null) {
            com.uc.webview.export.internal.utility.Log.d("notifyCoreEvent", "notifyCoreEvent");
            try {
                return ReflectionUtil.invoke((Object) SDKFactory.d, "notifyCoreEvent", new Class[]{InvokeObject.class}, new Object[]{new com.uc.webview.export.internal.uc.a(i, obj)});
            } catch (Throwable th) {
                com.uc.webview.export.internal.utility.Log.e("notifyCoreEvent", "notifyCoreEvent", th);
                return null;
            }
        } else if (WebView.getCoreType() != 3 || SDKFactory.d == null) {
            return null;
        } else {
            try {
                return SDKFactory.d.notifyCoreEvent(i, obj);
            } catch (Exception e) {
                com.uc.webview.export.internal.utility.Log.e("notifyCoreEvent", "notifyCoreEvent error=", e);
                return null;
            }
        }
    }

    public static Object notifyCoreEvent(int i, Object obj, ValueCallback<Object> valueCallback) {
        if (WebView.getCoreType() != 3 || SDKFactory.d == null) {
            return null;
        }
        try {
            return SDKFactory.d.notifyCoreEvent(i, obj, valueCallback);
        } catch (Exception e) {
            com.uc.webview.export.internal.utility.Log.e("notifyCoreEvent", "notifyCoreEvent error=", e);
            return null;
        }
    }

    public static void startDownload() {
        o a2 = o.a();
        if (a2.a != null && (a2.a instanceof bw)) {
            ((bw) a2.a).a.startDownload();
        }
    }

    public static String getExtractDirPath(String str, String str2) {
        File file = new File(str2);
        return new File(str, UCCyclone.getSourceHash(file.getAbsolutePath()) + "/" + UCCyclone.getSourceHash(file.length(), file.lastModified())).getAbsolutePath();
    }

    public static String getExtractDirPath(Context context, String str) {
        return getExtractDirPath(k.a(context, "decompresses2").getAbsolutePath(), str);
    }

    public static String getExtractDirPathByUrl(Context context, String str) {
        return new File(k.a(context, "updates"), UCCyclone.getSourceHash(str)).getAbsolutePath();
    }

    public static String getExtractRootDirPath(Context context) {
        return k.a(context, "decompresses2").getAbsolutePath();
    }

    public static String getODexDirPath(Context context, String str) {
        return k.b(k.a(context, "odexs"), k.e(k.b(context, str))).getAbsolutePath();
    }

    public static boolean setGlobalOption(String str, Object obj) {
        return d.a().a(str, obj);
    }

    public static Object getGlobalOption(String str) {
        return d.a().a(str);
    }

    public static void startTCPDevtools(String str, int i) {
        notifyCoreEvent(101, new Object[]{str, Integer.valueOf(i)});
    }

    public static void precacheResources(Map<String, WebResourceResponse> map, Map<String, String> map2) {
        if (WebView.getCoreType() == 3 && SDKFactory.d != null) {
            try {
                SDKFactory.d.invoke(103, new Object[]{map, map2});
            } catch (AbstractMethodError unused) {
            }
        }
    }

    public static void clearPrecacheResources(String[] strArr) {
        if (WebView.getCoreType() == 3 && SDKFactory.d != null) {
            try {
                SDKFactory.d.invoke(104, new Object[]{strArr});
            } catch (AbstractMethodError unused) {
            }
        }
    }

    public static void setMediaPlayerFactory(MediaPlayerFactory mediaPlayerFactory) {
        if (WebView.getCoreType() != 3 || SDKFactory.d == null) {
            com.uc.webview.export.internal.utility.Log.e("UCCore.ucmedia", "setMediaPlayerFactory failed - env invalid");
        } else {
            SDKFactory.d.setMediaPlayerFactory(mediaPlayerFactory);
        }
    }

    public static Object preInitCore(int i, Context context, Object[] objArr) {
        return ak.a(i, context, objArr);
    }

    static /* synthetic */ void a(String str, Object obj, Map map) {
        ValueCallback valueCallback;
        if (map == null) {
            return;
        }
        if (EVENT_DOWNLOAD_EXCEPTION.equals(str)) {
            ValueCallback valueCallback2 = (ValueCallback) map.get(EVENT_EXCEPTION);
            if (valueCallback2 != null) {
                try {
                    valueCallback2.onReceiveValue(((l) obj).getException());
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        } else if (EVENT_UPDATE_PROGRESS.equals(str) && (valueCallback = (ValueCallback) map.get("progress")) != null) {
            try {
                valueCallback.onReceiveValue(Integer.valueOf(((l) obj).getPercent()));
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
        }
    }

    static /* synthetic */ void a(Context context, String str, java.util.concurrent.Callable callable, Map map) throws UCSetupException {
        File uCPlayerRoot = UpdateTask.getUCPlayerRoot(context);
        SDKFactory.b((Long) 1024L);
        SDKFactory.b((Long) 2048L);
        SDKFactory.b(Long.valueOf(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM));
        SDKFactory.b(Long.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_URI));
        SDKFactory.b(Long.valueOf(PlaybackStateCompat.ACTION_PREPARE));
        SDKFactory.b(Long.valueOf(PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID));
        SDKFactory.b(Long.valueOf(PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH));
        SDKFactory.q.remove(IWaStat.VIDEO_ERROR_CODE_UPDATE_CHECK_REQUEST);
        SDKFactory.q.remove(IWaStat.VIDEO_ERROR_CODE_DOWNLOAD);
        SDKFactory.q.remove(IWaStat.VIDEO_ERROR_CODE_VERIFY);
        SDKFactory.q.remove(IWaStat.VIDEO_ERROR_CODE_UNZIP);
        Context context2 = context;
        String str2 = str;
        new UpdateTask(context2, str2, uCPlayerRoot.getAbsolutePath(), "libu3player.so", new g(), (Long) null, (Long) null).onEvent("beginDownload", new e()).onEvent("beginUnZip", new d()).onEvent("unzipSuccess", new n()).onEvent("check", new m(callable)).onEvent("success", new l(context, map)).onEvent(e.a, new k()).onEvent(EVENT_EXCEPTION, new i(map)).onEvent("exists", new h(map, uCPlayerRoot)).start();
    }

    static /* synthetic */ void a(Context context, Map map) {
        ValueCallback valueCallback;
        SDKFactory.d(context);
        if (map != null && (valueCallback = (ValueCallback) map.get("success")) != null) {
            try {
                valueCallback.onReceiveValue((Object) null);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
