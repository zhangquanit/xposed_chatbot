package com.alibaba.aliweex.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.Array;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class BlurTool {
    private static final String TAG = "BlurTool";
    private static ExecutorService sExecutorService = Executors.newCachedThreadPool(new ThreadFactory() {
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "wx_blur_thread");
        }
    });

    public interface OnBlurCompleteListener {
        void onBlurComplete(@NonNull Bitmap bitmap);
    }

    private static double calculateSampling(int i) {
        if (i <= 3) {
            return 0.5d;
        }
        return i <= 8 ? 0.25d : 0.125d;
    }

    @NonNull
    public static Bitmap blur(@NonNull Bitmap bitmap, int i) {
        Bitmap bitmap2 = bitmap;
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = false;
        int min = Math.min(10, Math.max(0, i));
        if (min == 0) {
            return bitmap2;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= 0 || height <= 0) {
            return bitmap2;
        }
        double calculateSampling = calculateSampling(min);
        int i2 = 3;
        double d = (double) width;
        Double.isNaN(d);
        double d2 = (double) height;
        Double.isNaN(d2);
        boolean z2 = true;
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap2, (int) (calculateSampling * d), (int) (calculateSampling * d2), true);
        int i3 = min;
        Bitmap bitmap3 = createScaledBitmap;
        int i4 = 0;
        while (i4 < i2) {
            if (i3 == 0) {
                return bitmap2;
            }
            try {
                double calculateSampling2 = calculateSampling(i3);
                if (calculateSampling2 != calculateSampling) {
                    Double.isNaN(d);
                    int i5 = (int) (calculateSampling2 * d);
                    Double.isNaN(d2);
                    try {
                        bitmap3 = Bitmap.createScaledBitmap(bitmap2, i5, (int) (calculateSampling2 * d2), z2);
                        calculateSampling = calculateSampling2;
                    } catch (Exception e) {
                        e = e;
                        calculateSampling = calculateSampling2;
                        WXLogUtils.e(TAG, "thrown exception when blurred image(times = " + i4 + ")," + e.getMessage());
                        z = false;
                        i3 = Math.max(0, i3 + -1);
                        i4++;
                        i2 = 3;
                        z2 = true;
                    }
                }
                try {
                    Bitmap stackBlur = stackBlur(bitmap3, i3, z);
                    StringBuilder sb = new StringBuilder();
                    sb.append("elapsed time on blurring image(radius:");
                    sb.append(i3);
                    sb.append(",sampling: ");
                    sb.append(calculateSampling);
                    sb.append("): ");
                    double d3 = calculateSampling;
                    try {
                        sb.append(System.currentTimeMillis() - currentTimeMillis);
                        sb.append("ms");
                        WXLogUtils.d(TAG, sb.toString());
                        return stackBlur;
                    } catch (Exception e2) {
                        e = e2;
                        calculateSampling = d3;
                    }
                } catch (Exception e3) {
                    e = e3;
                    double d4 = calculateSampling;
                    WXLogUtils.e(TAG, "thrown exception when blurred image(times = " + i4 + ")," + e.getMessage());
                    z = false;
                    i3 = Math.max(0, i3 + -1);
                    i4++;
                    i2 = 3;
                    z2 = true;
                }
            } catch (Exception e4) {
                e = e4;
                WXLogUtils.e(TAG, "thrown exception when blurred image(times = " + i4 + ")," + e.getMessage());
                z = false;
                i3 = Math.max(0, i3 + -1);
                i4++;
                i2 = 3;
                z2 = true;
            }
        }
        WXLogUtils.d(TAG, "elapsed time on blurring image(radius:" + i3 + ",sampling: " + calculateSampling + "): " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        return bitmap2;
    }

    public static void asyncBlur(@NonNull final Bitmap bitmap, final int i, @Nullable final OnBlurCompleteListener onBlurCompleteListener) {
        sExecutorService.execute(new Runnable() {
            public void run() {
                if (onBlurCompleteListener != null) {
                    try {
                        final Bitmap blur = BlurTool.blur(bitmap, i);
                        BlurTool.post(new Runnable() {
                            public void run() {
                                onBlurCompleteListener.onBlurComplete(blur);
                            }
                        });
                    } catch (Exception unused) {
                        BlurTool.post(new Runnable() {
                            public void run() {
                                onBlurCompleteListener.onBlurComplete(bitmap);
                            }
                        });
                        WXLogUtils.e("blur failed,return original image.");
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public static void post(@NonNull Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static Bitmap stackBlur(Bitmap bitmap, int i, boolean z) {
        Bitmap bitmap2;
        int[] iArr;
        int i2 = i;
        if (z) {
            bitmap2 = bitmap;
        } else {
            bitmap2 = bitmap.copy(bitmap.getConfig(), true);
        }
        if (i2 < 1) {
            return null;
        }
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        int i3 = width * height;
        int[] iArr2 = new int[i3];
        bitmap2.getPixels(iArr2, 0, width, 0, 0, width, height);
        int i4 = width - 1;
        int i5 = height - 1;
        int i6 = i2 + i2 + 1;
        int[] iArr3 = new int[i3];
        int[] iArr4 = new int[i3];
        int[] iArr5 = new int[i3];
        int[] iArr6 = new int[Math.max(width, height)];
        int i7 = (i6 + 1) >> 1;
        int i8 = i7 * i7;
        int i9 = i8 * 256;
        int[] iArr7 = new int[i9];
        for (int i10 = 0; i10 < i9; i10++) {
            iArr7[i10] = i10 / i8;
        }
        int[][] iArr8 = (int[][]) Array.newInstance(int.class, new int[]{i6, 3});
        int i11 = i2 + 1;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        while (i12 < height) {
            Bitmap bitmap3 = bitmap2;
            int i15 = -i2;
            int i16 = 0;
            int i17 = 0;
            int i18 = 0;
            int i19 = 0;
            int i20 = 0;
            int i21 = 0;
            int i22 = 0;
            int i23 = 0;
            int i24 = 0;
            while (i15 <= i2) {
                int i25 = i5;
                int i26 = height;
                int i27 = iArr2[i13 + Math.min(i4, Math.max(i15, 0))];
                int[] iArr9 = iArr8[i15 + i2];
                iArr9[0] = (i27 & 16711680) >> 16;
                iArr9[1] = (i27 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr9[2] = i27 & 255;
                int abs = i11 - Math.abs(i15);
                i16 += iArr9[0] * abs;
                i17 += iArr9[1] * abs;
                i18 += iArr9[2] * abs;
                if (i15 > 0) {
                    i19 += iArr9[0];
                    i20 += iArr9[1];
                    i21 += iArr9[2];
                } else {
                    i22 += iArr9[0];
                    i23 += iArr9[1];
                    i24 += iArr9[2];
                }
                i15++;
                height = i26;
                i5 = i25;
            }
            int i28 = i5;
            int i29 = height;
            int i30 = i2;
            int i31 = 0;
            while (i31 < width) {
                iArr3[i13] = iArr7[i16];
                iArr4[i13] = iArr7[i17];
                iArr5[i13] = iArr7[i18];
                int i32 = i16 - i22;
                int i33 = i17 - i23;
                int i34 = i18 - i24;
                int[] iArr10 = iArr8[((i30 - i2) + i6) % i6];
                int i35 = i22 - iArr10[0];
                int i36 = i23 - iArr10[1];
                int i37 = i24 - iArr10[2];
                if (i12 == 0) {
                    iArr = iArr7;
                    iArr6[i31] = Math.min(i31 + i2 + 1, i4);
                } else {
                    iArr = iArr7;
                }
                int i38 = iArr2[i14 + iArr6[i31]];
                iArr10[0] = (i38 & 16711680) >> 16;
                iArr10[1] = (i38 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr10[2] = i38 & 255;
                int i39 = i19 + iArr10[0];
                int i40 = i20 + iArr10[1];
                int i41 = i21 + iArr10[2];
                i16 = i32 + i39;
                i17 = i33 + i40;
                i18 = i34 + i41;
                i30 = (i30 + 1) % i6;
                int[] iArr11 = iArr8[i30 % i6];
                i22 = i35 + iArr11[0];
                i23 = i36 + iArr11[1];
                i24 = i37 + iArr11[2];
                i19 = i39 - iArr11[0];
                i20 = i40 - iArr11[1];
                i21 = i41 - iArr11[2];
                i13++;
                i31++;
                iArr7 = iArr;
            }
            int[] iArr12 = iArr7;
            i14 += width;
            i12++;
            bitmap2 = bitmap3;
            height = i29;
            i5 = i28;
        }
        Bitmap bitmap4 = bitmap2;
        int i42 = i5;
        int i43 = height;
        int[] iArr13 = iArr7;
        int i44 = 0;
        while (i44 < width) {
            int i45 = -i2;
            int i46 = i45 * width;
            int i47 = 0;
            int i48 = 0;
            int i49 = 0;
            int i50 = 0;
            int i51 = 0;
            int i52 = 0;
            int i53 = 0;
            int i54 = 0;
            int i55 = 0;
            while (i45 <= i2) {
                int[] iArr14 = iArr6;
                int max = Math.max(0, i46) + i44;
                int[] iArr15 = iArr8[i45 + i2];
                iArr15[0] = iArr3[max];
                iArr15[1] = iArr4[max];
                iArr15[2] = iArr5[max];
                int abs2 = i11 - Math.abs(i45);
                i47 += iArr3[max] * abs2;
                i48 += iArr4[max] * abs2;
                i49 += iArr5[max] * abs2;
                if (i45 > 0) {
                    i50 += iArr15[0];
                    i51 += iArr15[1];
                    i52 += iArr15[2];
                } else {
                    i53 += iArr15[0];
                    i54 += iArr15[1];
                    i55 += iArr15[2];
                }
                int i56 = i42;
                if (i45 < i56) {
                    i46 += width;
                }
                i45++;
                i42 = i56;
                iArr6 = iArr14;
            }
            int[] iArr16 = iArr6;
            int i57 = i42;
            int i58 = i44;
            int i59 = i51;
            int i60 = i52;
            int i61 = 0;
            int i62 = i2;
            int i63 = i50;
            int i64 = i49;
            int i65 = i48;
            int i66 = i47;
            int i67 = i43;
            while (i61 < i67) {
                iArr2[i58] = (iArr2[i58] & -16777216) | (iArr13[i66] << 16) | (iArr13[i65] << 8) | iArr13[i64];
                int i68 = i66 - i53;
                int i69 = i65 - i54;
                int i70 = i64 - i55;
                int[] iArr17 = iArr8[((i62 - i2) + i6) % i6];
                int i71 = i53 - iArr17[0];
                int i72 = i54 - iArr17[1];
                int i73 = i55 - iArr17[2];
                if (i44 == 0) {
                    iArr16[i61] = Math.min(i61 + i11, i57) * width;
                }
                int i74 = iArr16[i61] + i44;
                iArr17[0] = iArr3[i74];
                iArr17[1] = iArr4[i74];
                iArr17[2] = iArr5[i74];
                int i75 = i63 + iArr17[0];
                int i76 = i59 + iArr17[1];
                int i77 = i60 + iArr17[2];
                i66 = i68 + i75;
                i65 = i69 + i76;
                i64 = i70 + i77;
                i62 = (i62 + 1) % i6;
                int[] iArr18 = iArr8[i62];
                i53 = i71 + iArr18[0];
                i54 = i72 + iArr18[1];
                i55 = i73 + iArr18[2];
                i63 = i75 - iArr18[0];
                i59 = i76 - iArr18[1];
                i60 = i77 - iArr18[2];
                i58 += width;
                i61++;
                i2 = i;
            }
            i44++;
            i42 = i57;
            i43 = i67;
            iArr6 = iArr16;
            i2 = i;
        }
        bitmap4.setPixels(iArr2, 0, width, 0, 0, width, i43);
        return bitmap4;
    }
}
