package com.taobao.uc;

import java.io.Serializable;

public class UCSoSettings implements Serializable {
    private static UCSoSettings instance;
    public String UC_CORE_THICK = "false";
    public String UC_CORE_TYPE = BuildConfig.UC_CORE_TYPE;
    public String UC_CORE_URL_32 = BuildConfig.UC_CORE_URL_THIN_32;
    public String UC_CORE_URL_64 = BuildConfig.UC_CORE_URL_THIN_64;
    public String UC_CORE_URL_DEBUG_32 = BuildConfig.UC_CORE_URL_THIN_DEBUG_32;
    public String UC_CORE_URL_DEBUG_64 = BuildConfig.UC_CORE_URL_THIN_DEBUG_64;
    public String UC_CORE_URL_DEBUG_X86 = "";
    public String UC_DEBUGGABLE = "false";
    public String UC_PLAYER_URL = BuildConfig.UC_PLAYER_URL;

    public static UCSoSettings getInstance() {
        if (instance == null) {
            synchronized (UCSoSettings.class) {
                if (instance == null) {
                    instance = new UCSoSettings();
                }
            }
        }
        return instance;
    }

    public UCSoSettings setUCPlayerUrl(String str) {
        this.UC_PLAYER_URL = str;
        return this;
    }

    public UCSoSettings setUCCoreDebug32(String str) {
        this.UC_CORE_URL_DEBUG_32 = str;
        return this;
    }

    public UCSoSettings setUCCoreDebug64(String str) {
        this.UC_CORE_URL_DEBUG_64 = str;
        return this;
    }

    public UCSoSettings setUCCoreRelease32(String str) {
        this.UC_CORE_URL_32 = str;
        return this;
    }

    public UCSoSettings setUCCoreRelease64(String str) {
        this.UC_CORE_URL_64 = str;
        return this;
    }
}
