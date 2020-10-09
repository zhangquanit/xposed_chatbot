package com.alibaba.motu.crashreporter;

import android.content.Context;
import android.os.Process;
import android.os.SystemClock;
import com.alibaba.motu.crashreporter.CrashReporter;
import com.alibaba.motu.tbrest.utils.AppUtils;
import com.alibaba.motu.tbrest.utils.StringUtils;
import java.io.File;

final class RunningStateMonitor implements Runnable {
    public static final int STARTUP_STATE_CRASH_TOO_MANY = 16;
    public static final int STARTUP_STATE_NORMAL = 0;
    public static final int STARTUP_STATE_STARTUP_TOO_FAST = 1;
    Context mContext;
    CrashReporter.DefaultStartupStateAnalyzeCallback mDefaultStartupStateAnalyzeCallback;
    RunningState mLastRunningState;
    File mMonitorFile;
    RunningState mRunningState;
    StorageManager mStorageManager;

    public void run() {
        this.mMonitorFile = this.mStorageManager.getProcessTombstoneFile("STARTUP_MONITOR");
        if (this.mMonitorFile.exists()) {
            try {
                String readLineAndDel = AppUtils.readLineAndDel(this.mMonitorFile);
                if (StringUtils.isNotBlank(readLineAndDel)) {
                    RunningState runningState = new RunningState();
                    try {
                        runningState.deserialize(readLineAndDel);
                        this.mLastRunningState = runningState;
                    } catch (Exception e) {
                        LogUtil.e("lastRunningState deserialize", e);
                    }
                }
            } catch (Exception unused) {
            }
        }
        if (this.mLastRunningState != null) {
            boolean z = this.mRunningState.mElapsedRealtime < this.mLastRunningState.mElapsedRealtime;
            this.mRunningState.mTotalStartCount += this.mLastRunningState.mTotalStartCount;
            if (!z) {
                this.mRunningState.mContinuousStartCount += this.mLastRunningState.mContinuousStartCount;
                if (this.mRunningState.mElapsedRealtime / 60000 == this.mLastRunningState.mElapsedRealtime / 60000) {
                    this.mRunningState.mContinuousStartCount1Minute += this.mLastRunningState.mContinuousStartCount1Minute;
                    this.mRunningState.mContinuousStartCount5Minute += this.mLastRunningState.mContinuousStartCount5Minute;
                    this.mRunningState.mContinuousStartCount1Hour += this.mLastRunningState.mContinuousStartCount1Hour;
                    this.mRunningState.mContinuousStartCount24Hour += this.mLastRunningState.mContinuousStartCount24Hour;
                } else if (this.mRunningState.mElapsedRealtime / 300000 == this.mLastRunningState.mElapsedRealtime / 300000) {
                    this.mRunningState.mContinuousStartCount5Minute += this.mLastRunningState.mContinuousStartCount5Minute;
                    this.mRunningState.mContinuousStartCount1Hour += this.mLastRunningState.mContinuousStartCount1Hour;
                    this.mRunningState.mContinuousStartCount24Hour += this.mLastRunningState.mContinuousStartCount24Hour;
                } else if (this.mRunningState.mElapsedRealtime / 3600000 == this.mLastRunningState.mElapsedRealtime / 3600000) {
                    this.mRunningState.mContinuousStartCount1Hour += this.mLastRunningState.mContinuousStartCount1Hour;
                    this.mRunningState.mContinuousStartCount24Hour += this.mLastRunningState.mContinuousStartCount24Hour;
                } else if (this.mRunningState.mElapsedRealtime / 86400000 == this.mLastRunningState.mElapsedRealtime / 86400000) {
                    this.mRunningState.mContinuousStartCount24Hour += this.mLastRunningState.mContinuousStartCount24Hour;
                }
            }
        }
        flushRunningState();
        analyzeStartupState();
    }

    public RunningStateMonitor(Context context, String str, String str2, String str3, String str4, long j, StorageManager storageManager, CrashReporter.DefaultStartupStateAnalyzeCallback defaultStartupStateAnalyzeCallback) {
        this.mContext = context;
        this.mStorageManager = storageManager;
        this.mRunningState = new RunningState(this.mContext, str, str2, str3, str4, j);
        this.mDefaultStartupStateAnalyzeCallback = defaultStartupStateAnalyzeCallback;
    }

    private void analyzeStartupState() {
        int i = (this.mRunningState.mContinuousStartCount1Minute >= 3 || this.mRunningState.mContinuousStartCount5Minute >= 10) ? 16 : 0;
        if (this.mLastRunningState != null && this.mRunningState.mElapsedRealtime - this.mLastRunningState.mElapsedRealtime < 30000) {
            i |= 1;
        }
        if (this.mDefaultStartupStateAnalyzeCallback != null) {
            this.mDefaultStartupStateAnalyzeCallback.onComplete(i);
        }
    }

    private synchronized void flushRunningState() {
        AppUtils.writeFile(this.mMonitorFile, this.mRunningState.serialize());
    }

    public void refreshAppVersion(String str) {
        if (StringUtils.isNotBlank(str) && !str.equals(this.mRunningState.mAppVersion)) {
            this.mRunningState.mAppVersion = str;
            flushRunningState();
        }
    }

    class RunningState {
        String mAppId;
        String mAppKey;
        String mAppVersion;
        int mContinuousStartCount;
        int mContinuousStartCount1Hour;
        int mContinuousStartCount1Minute;
        int mContinuousStartCount24Hour;
        int mContinuousStartCount5Minute;
        long mElapsedRealtime;
        int mPid;
        String mProcessName;
        long mStartupTime;
        long mTimestamp;
        int mTotalStartCount;
        long mUptimeMillis;

        RunningState() {
        }

        RunningState(Context context, String str, String str2, String str3, String str4, long j) {
            this.mAppId = str;
            this.mAppKey = str2;
            this.mAppVersion = str3;
            this.mStartupTime = j;
            this.mUptimeMillis = SystemClock.uptimeMillis();
            this.mElapsedRealtime = SystemClock.elapsedRealtime();
            this.mTimestamp = System.currentTimeMillis();
            this.mPid = Process.myPid();
            this.mProcessName = str4;
            this.mTotalStartCount = 1;
            this.mContinuousStartCount = 1;
            this.mContinuousStartCount24Hour = 1;
            this.mContinuousStartCount1Hour = 1;
            this.mContinuousStartCount1Minute = 1;
            this.mContinuousStartCount5Minute = 1;
        }

        /* access modifiers changed from: package-private */
        public String serialize() {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", new Object[]{this.mAppId, this.mAppKey, this.mAppVersion, Long.valueOf(this.mStartupTime), Long.valueOf(this.mUptimeMillis), Long.valueOf(this.mElapsedRealtime), Long.valueOf(this.mTimestamp), Integer.valueOf(this.mPid), this.mProcessName, Integer.valueOf(this.mTotalStartCount), Integer.valueOf(this.mContinuousStartCount), Integer.valueOf(this.mContinuousStartCount24Hour), Integer.valueOf(this.mContinuousStartCount1Hour), Integer.valueOf(this.mContinuousStartCount1Minute), Integer.valueOf(this.mContinuousStartCount5Minute)});
        }

        /* access modifiers changed from: package-private */
        public void deserialize(String str) {
            String[] split = str.split(",");
            this.mAppId = split[0];
            this.mAppKey = split[1];
            this.mAppVersion = split[2];
            this.mStartupTime = Long.parseLong(split[3]);
            this.mUptimeMillis = Long.parseLong(split[4]);
            this.mElapsedRealtime = Long.parseLong(split[5]);
            this.mTimestamp = Long.parseLong(split[6]);
            this.mPid = Integer.parseInt(split[7]);
            this.mProcessName = split[8];
            this.mTotalStartCount = Integer.parseInt(split[9]);
            this.mContinuousStartCount = Integer.parseInt(split[10]);
            this.mContinuousStartCount24Hour = Integer.parseInt(split[11]);
            this.mContinuousStartCount1Hour = Integer.parseInt(split[12]);
            this.mContinuousStartCount1Minute = Integer.parseInt(split[13]);
            this.mContinuousStartCount5Minute = Integer.parseInt(split[14]);
        }
    }
}
