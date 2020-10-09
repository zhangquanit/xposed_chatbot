package com.alibaba.aliweex.adapter.module.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import mtopsdk.mtop.upload.domain.UploadConstants;

public class WXAudioModule extends WXModule implements IWXAudio, Destroyable {
    /* access modifiers changed from: private */
    public List<Long> autoStartPlayWhenReady = new ArrayList();
    private Map<Long, JSCallback> callbackMap = new HashMap();
    private AtomicLong generateId = new AtomicLong(1);
    Handler handler = new Handler(Looper.getMainLooper());
    private Map<String, List<Long>> instancePlayerIdMap = new HashMap();
    /* access modifiers changed from: private */
    public List<Long> loopWhenPlayEnded = new ArrayList();
    AudioManager mAudioManager = null;
    WXAudioOnCompletionListener mCompletionListener = new WXAudioOnCompletionListener();
    WXAudioOnErrorListener mErrorListener = new WXAudioOnErrorListener();
    boolean mHasErrorInGainAudioFocus = false;
    WXAudioOnPreparedListener mPreparedListener = new WXAudioOnPreparedListener();
    private WXAudioFocusListener mWXAudioFocusListener = new WXAudioFocusListener();
    /* access modifiers changed from: private */
    public Map<Long, Map<String, String>> optionsMap = new HashMap();
    /* access modifiers changed from: private */
    public List<Long> pausedWhenLoseAudioFocus = new ArrayList();
    /* access modifiers changed from: private */
    public Map<Long, MediaPlayer> playerMap = new HashMap();
    private Map<Long, Integer> statusMap = new ConcurrentHashMap();

    @JSMethod(uiThread = false)
    public String canPlayType(String str) {
        if ("audio/wav".equalsIgnoreCase(str) || "audio/x-wav".equalsIgnoreCase(str) || "audio/mp3".equalsIgnoreCase(str) || "audio/aac".equalsIgnoreCase(str) || "audio/mp4".equalsIgnoreCase(str) || "audio/amr".equalsIgnoreCase(str)) {
            return IWXAudio.CAN_PLAY_TYPE_PROBABLY;
        }
        return UploadConstants.FILE_CONTENT_TYPE.equalsIgnoreCase(str) ? "" : IWXAudio.CAN_PLAY_TYPE_MAYBE;
    }

    @JSMethod
    public void load(Map<String, String> map, @Nullable JSCallback jSCallback) {
        long j;
        HashMap hashMap = new HashMap(3);
        if (map == null) {
            invokeCallbackAndKeepAlive(generateCallbackValue(-1L, 6, "5", "empty option"));
            return;
        }
        if (map.get("id") == null) {
            j = this.generateId.getAndIncrement();
        } else {
            j = Long.parseLong(map.get("id"));
        }
        hashMap.put("id", String.valueOf(j));
        if (map.get("url") == null) {
            invokeCallbackAndKeepAlive(generateCallbackValue(Long.valueOf(j), 6, "4", "empty option url"));
            return;
        }
        Uri parse = Uri.parse(map.get("url"));
        if (!parse.isHierarchical()) {
            invokeCallbackAndKeepAlive(generateCallbackValue(Long.valueOf(j), 6, "4", "not a vaild url"));
            return;
        }
        collectPlayerIdByInstanceId(this.mWXSDKInstance.getInstanceId(), j);
        if (this.optionsMap.containsKey(Long.valueOf(j))) {
            this.autoStartPlayWhenReady.remove(Long.valueOf(j));
            this.loopWhenPlayEnded.remove(Long.valueOf(j));
        } else {
            this.optionsMap.put(Long.valueOf(j), map);
        }
        this.callbackMap.put(Long.valueOf(j), jSCallback);
        changeStatus(Long.valueOf(j), 1);
        MediaPlayer player = getPlayer(Long.valueOf(j));
        if (map.get(IWXAudio.KEY_VOLUME) != null) {
            Float valueOf = Float.valueOf(0.0f);
            try {
                valueOf = Float.valueOf(Float.parseFloat(map.get(IWXAudio.KEY_VOLUME)));
            } catch (NumberFormatException unused) {
            }
            if (valueOf.floatValue() > 0.0f) {
                player.setVolume(valueOf.floatValue(), valueOf.floatValue());
            }
        }
        if (String.valueOf(Boolean.TRUE).equals(map.get("autoplay"))) {
            this.autoStartPlayWhenReady.add(Long.valueOf(j));
        }
        if (String.valueOf(Boolean.TRUE).equals(map.get(IWXAudio.KEY_LOOP))) {
            this.loopWhenPlayEnded.add(Long.valueOf(j));
        }
        try {
            getAudioManager();
            player.reset();
            player.setDataSource(this.mWXSDKInstance.getContext(), parse);
            player.setOnCompletionListener(this.mCompletionListener);
            player.setOnErrorListener(this.mErrorListener);
            player.setOnPreparedListener(this.mPreparedListener);
            invokeCallbackAndKeepAlive(generateCallbackValue(Long.valueOf(j), 1, ""));
            player.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            changeStatus(Long.valueOf(j), 6);
            invokeCallbackAndKeepAlive(generateCallbackValue(Long.valueOf(j), 6, "5", e.getMessage()));
        }
    }

    private AudioManager getAudioManager() {
        try {
            if (this.mAudioManager == null) {
                this.mAudioManager = (AudioManager) this.mWXSDKInstance.getContext().getSystemService("audio");
            }
        } catch (Exception e) {
            WXLogUtils.w("error in getSystemService of audio", (Throwable) e);
            this.mHasErrorInGainAudioFocus = true;
        }
        return this.mAudioManager;
    }

    private class WXAudioOnPreparedListener implements MediaPlayer.OnPreparedListener {
        private WXAudioOnPreparedListener() {
        }

        public void onPrepared(MediaPlayer mediaPlayer) {
            Long access$100 = WXAudioModule.this.getIdByPlayer(mediaPlayer);
            WXAudioModule.this.changeStatus(access$100, 2);
            if (WXAudioModule.this.autoStartPlayWhenReady.size() > 0 && WXAudioModule.this.autoStartPlayWhenReady.contains(access$100)) {
                WXAudioModule.this.play(access$100);
                WXAudioModule.this.autoStartPlayWhenReady.remove(access$100);
            }
            if (WXAudioModule.this.optionsMap.get(access$100) != null) {
                ((Map) WXAudioModule.this.optionsMap.get(access$100)).put("duration", String.valueOf(mediaPlayer.getDuration()));
            }
            HashMap hashMap = new HashMap(1);
            hashMap.put("duration", String.valueOf(mediaPlayer.getDuration()));
            WXAudioModule.this.invokeCallbackAndKeepAlive(WXAudioModule.this.generateCallbackValue(access$100, 2, hashMap));
        }
    }

    private class WXAudioOnErrorListener implements MediaPlayer.OnErrorListener {
        private WXAudioOnErrorListener() {
        }

        public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
            long longValue = WXAudioModule.this.getIdByPlayer(mediaPlayer).longValue();
            WXAudioModule.this.changeStatus(Long.valueOf(longValue), 6);
            WXAudioModule wXAudioModule = WXAudioModule.this;
            WXAudioModule wXAudioModule2 = WXAudioModule.this;
            Long valueOf = Long.valueOf(longValue);
            wXAudioModule.invokeCallbackAndKeepAlive(wXAudioModule2.generateCallbackValue(valueOf, 6, "2", "error in loading audio. what:" + i + " extra:" + i2));
            return true;
        }
    }

    private class WXAudioOnCompletionListener implements MediaPlayer.OnCompletionListener {
        private WXAudioOnCompletionListener() {
        }

        public void onCompletion(MediaPlayer mediaPlayer) {
            final long longValue = WXAudioModule.this.getIdByPlayer(mediaPlayer).longValue();
            WXAudioModule.this.changeStatus(Long.valueOf(longValue), 5);
            int i = 100;
            try {
                if (((Map) WXAudioModule.this.optionsMap.get(Long.valueOf(longValue))).get("delay") != null) {
                    i = Integer.parseInt((String) ((Map) WXAudioModule.this.optionsMap.get(Long.valueOf(longValue))).get("delay"));
                }
            } catch (Exception unused) {
            }
            WXAudioModule.this.handler.postDelayed(new Runnable() {
                public void run() {
                    if (WXAudioModule.this.loopWhenPlayEnded.contains(Long.valueOf(longValue))) {
                        WXAudioModule.this.play(Long.valueOf(longValue));
                        return;
                    }
                    WXAudioModule.this.invokeCallbackAndKeepAlive(WXAudioModule.this.generateCallbackValue(Long.valueOf(longValue), 5, ""));
                    WXAudioModule.this.abandonAudioFocus();
                }
            }, (long) i);
        }
    }

    /* access modifiers changed from: private */
    public Map<String, Object> generateCallbackValue(Long l, Integer num, String str, String str2) {
        HashMap hashMap = new HashMap(2);
        hashMap.put("code", str);
        hashMap.put("message", str2);
        return generateCallbackValue(l, num, hashMap);
    }

    /* access modifiers changed from: private */
    public Map<String, Object> generateCallbackValue(Long l, Integer num, Object obj) {
        HashMap hashMap = new HashMap(3);
        hashMap.put("id", l);
        hashMap.put("status", num);
        hashMap.put("value", obj);
        return hashMap;
    }

    /* access modifiers changed from: private */
    public void changeStatus(Long l, int i) {
        this.statusMap.put(l, Integer.valueOf(i));
    }

    /* access modifiers changed from: private */
    public void invokeCallbackAndKeepAlive(Map<String, Object> map) {
        JSCallback jSCallback;
        if (map.get("id") != null && (jSCallback = this.callbackMap.get((Long) map.get("id"))) != null) {
            jSCallback.invokeAndKeepAlive(map);
        }
    }

    /* access modifiers changed from: private */
    public MediaPlayer getPlayer(Long l) {
        MediaPlayer mediaPlayer = this.playerMap.get(l);
        if (mediaPlayer != null) {
            return mediaPlayer;
        }
        MediaPlayer mediaPlayer2 = new MediaPlayer();
        this.playerMap.put(l, mediaPlayer2);
        return mediaPlayer2;
    }

    /* access modifiers changed from: private */
    public Long getIdByPlayer(MediaPlayer mediaPlayer) {
        for (Map.Entry next : this.playerMap.entrySet()) {
            if (next.getValue() == mediaPlayer) {
                return (Long) next.getKey();
            }
        }
        return -1L;
    }

    private void collectPlayerIdByInstanceId(String str, long j) {
        List list = this.instancePlayerIdMap.get(str);
        if (list == null) {
            list = new ArrayList();
            this.instancePlayerIdMap.put(str, list);
        }
        if (!list.contains(Long.valueOf(j))) {
            list.add(Long.valueOf(j));
        }
    }

    @JSMethod
    public void play(Long l) {
        if (l != null && this.playerMap.get(l) != null) {
            Integer num = -1;
            try {
                if (this.optionsMap.get(l) != null) {
                    num = Integer.valueOf(Integer.parseInt((String) this.optionsMap.get(l).get("duration")));
                }
            } catch (Exception unused) {
            }
            if (tryToGainAudioFocus(num.intValue()) || this.mHasErrorInGainAudioFocus) {
                try {
                    MediaPlayer player = getPlayer(l);
                    if (3 == this.statusMap.get(l).intValue()) {
                        return;
                    }
                    if (!player.isPlaying()) {
                        if (2 != this.statusMap.get(l).intValue()) {
                            if (4 != this.statusMap.get(l).intValue()) {
                                if (5 == this.statusMap.get(l).intValue()) {
                                    player.pause();
                                    player.seekTo(0);
                                    doPlayInner(l);
                                    return;
                                } else if (1 == this.statusMap.get(l).intValue()) {
                                    this.autoStartPlayWhenReady.add(l);
                                    return;
                                } else {
                                    return;
                                }
                            }
                        }
                        doPlayInner(l);
                    }
                } catch (IllegalStateException e) {
                    invokeCallbackAndKeepAlive(generateCallbackValue(l, 6, "exception occur. IllegalStateException " + e.getMessage()));
                }
            } else {
                invokeCallbackAndKeepAlive(generateCallbackValue(l, 6, "request audio focus failed. maybe there is a high prior audio task is playing"));
            }
        }
    }

    private void doPlayInner(Long l) {
        try {
            getPlayer(l).start();
            changeStatus(l, 3);
            invokeCallbackAndKeepAlive(generateCallbackValue(l, 3, ""));
        } catch (Exception e) {
            WXLogUtils.w("error in play", (Throwable) e);
            invokeCallbackAndKeepAlive(generateCallbackValue(l, 6, "error in start play"));
        }
    }

    private boolean tryToGainAudioFocus(int i) {
        int i2 = 2;
        if (i > 100000) {
            i2 = 1;
        } else if (i > 0 && i < 2500) {
            i2 = 3;
        }
        try {
            int requestAudioFocus = getAudioManager().requestAudioFocus(this.mWXAudioFocusListener, 3, i2);
            if (requestAudioFocus == 1) {
                return true;
            }
            return requestAudioFocus == 0 ? false : false;
        } catch (Exception e) {
            WXLogUtils.w("error in tryToGainAudioFocus", (Throwable) e);
            this.mHasErrorInGainAudioFocus = true;
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void abandonAudioFocus() {
        boolean z = true;
        try {
            for (Integer intValue : this.statusMap.values()) {
                if (intValue.intValue() == 3) {
                    z = false;
                }
            }
            if (z) {
                getAudioManager().abandonAudioFocus(this.mWXAudioFocusListener);
            }
        } catch (Exception e) {
            WXLogUtils.w("error in abandonAudioFocus", (Throwable) e);
        }
    }

    class WXAudioFocusListener implements AudioManager.OnAudioFocusChangeListener {
        boolean meetMayDuck = false;

        WXAudioFocusListener() {
        }

        public void onAudioFocusChange(int i) {
            if (i == -2) {
                synchronized (this) {
                    for (Long l : WXAudioModule.this.playerMap.keySet()) {
                        MediaPlayer access$1300 = WXAudioModule.this.getPlayer(l);
                        if (access$1300 != null && access$1300.isPlaying()) {
                            WXAudioModule.this.pause(l);
                            WXAudioModule.this.pausedWhenLoseAudioFocus.add(l);
                        }
                    }
                }
            } else if (i == 1) {
                synchronized (this) {
                    for (Long play : WXAudioModule.this.pausedWhenLoseAudioFocus) {
                        WXAudioModule.this.play(play);
                    }
                    if (this.meetMayDuck) {
                        WXAudioModule.this.setVolume(1.0f);
                        this.meetMayDuck = false;
                    }
                }
            } else if (i == 3) {
                synchronized (this) {
                    WXAudioModule.this.setVolume(0.3f);
                    this.meetMayDuck = true;
                }
            } else if (i == -1) {
                synchronized (this) {
                    stopAll();
                    WXAudioModule.this.abandonAudioFocus();
                }
            }
        }

        private void stopAll() {
            for (Long stop : WXAudioModule.this.playerMap.keySet()) {
                WXAudioModule.this.stop(stop);
            }
        }
    }

    @JSMethod
    public void pause(Long l) {
        if (l != null && this.playerMap.get(l) != null) {
            MediaPlayer player = getPlayer(l);
            if (player.isPlaying()) {
                try {
                    player.pause();
                    changeStatus(l, 4);
                    invokeCallbackAndKeepAlive(generateCallbackValue(l, 4, ""));
                } catch (Exception e) {
                    WXLogUtils.w("error in pause", (Throwable) e);
                    invokeCallbackAndKeepAlive(generateCallbackValue(l, 6, "error in pause"));
                }
            }
        }
    }

    @JSMethod
    public void stop(Long l) {
        if (l != null && this.playerMap.get(l) != null) {
            MediaPlayer player = getPlayer(l);
            this.loopWhenPlayEnded.remove(l);
            if (player.isPlaying()) {
                try {
                    player.pause();
                    player.seekTo(0);
                    changeStatus(l, 5);
                    invokeCallbackAndKeepAlive(generateCallbackValue(l, 5, ""));
                    abandonAudioFocus();
                } catch (Exception e) {
                    WXLogUtils.w("error in stop", (Throwable) e);
                    invokeCallbackAndKeepAlive(generateCallbackValue(l, 6, "error in stop"));
                }
            }
        }
    }

    @JSMethod
    public void seek(Long l, int i) {
        if (l != null && this.playerMap.get(l) != null) {
            Integer num = -1;
            try {
                if (this.optionsMap.get(l) != null) {
                    num = Integer.valueOf(Integer.parseInt((String) this.optionsMap.get(l).get("duration")));
                }
            } catch (Exception unused) {
            }
            if (tryToGainAudioFocus(num.intValue()) || this.mHasErrorInGainAudioFocus) {
                try {
                    MediaPlayer player = getPlayer(l);
                    if (3 != this.statusMap.get(l).intValue()) {
                        if (!player.isPlaying()) {
                            if (2 != this.statusMap.get(l).intValue()) {
                                if (4 != this.statusMap.get(l).intValue()) {
                                    if (5 == this.statusMap.get(l).intValue()) {
                                        player.pause();
                                        player.seekTo(i);
                                        doPlayInner(l);
                                        return;
                                    }
                                    return;
                                }
                            }
                            player.seekTo(i);
                            doPlayInner(l);
                            return;
                        }
                    }
                    player.pause();
                    player.seekTo(i);
                    doPlayInner(l);
                } catch (IllegalStateException e) {
                    invokeCallbackAndKeepAlive(generateCallbackValue(l, 6, "exception occur. IllegalStateException " + e.getMessage()));
                }
            } else {
                invokeCallbackAndKeepAlive(generateCallbackValue(l, 6, "request audio focus failed. maybe there is a high prior audio task is playing"));
            }
        }
    }

    @JSMethod
    public void setVolume(float f) {
        List<Long> list = this.instancePlayerIdMap.get(this.mWXSDKInstance.getInstanceId());
        if (list != null) {
            for (Long l : list) {
                MediaPlayer mediaPlayer = this.playerMap.get(l);
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(f, f);
                }
            }
        }
    }

    public void destroy() {
        if (this.mWXSDKInstance == null) {
            WXLogUtils.w("in WXAudioModule destory() mWXSDKInstance is null.");
            return;
        }
        List<Long> list = this.instancePlayerIdMap.get(this.mWXSDKInstance.getInstanceId());
        if (list != null) {
            for (Long l : list) {
                this.optionsMap.remove(l);
                MediaPlayer mediaPlayer = this.playerMap.get(l);
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                this.playerMap.remove(l);
                this.statusMap.remove(l);
                this.callbackMap.remove(l);
            }
        }
        this.instancePlayerIdMap.remove(this.mWXSDKInstance.getInstanceId());
        abandonAudioFocus();
    }
}
