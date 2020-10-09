package android.taobao.windvane.config;

public class UCHASettings {
    public String appSecret = "";
    public String appid = "";
    public int configUpdateMode = 0;
    public boolean debug = false;
    public int logUploadMode = 0;

    public static class ConfigRate {
        public int u4BkpgSamplingRate = 100;
        public boolean u4CollectUrlQuery = true;
        public boolean u4CollectUrlRef = true;
        public double u4HarSamplingRate = 0.01d;
        public int u4JserrSamplingRate = 100;
        public int u4ResloadfailSamplingRate = 10;
        public int u4T1t3detailSamplingRate = 30;
        public int u4XhrSamplingRate = 1;
    }

    public UCHASettings() {
    }

    public UCHASettings(String str, String str2) {
        this.appid = str;
        this.appSecret = str2;
    }
}
