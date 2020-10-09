package com.facebook.common.util;

import com.taobao.tao.image.Logger;
import com.taobao.weex.el.parse.Operators;
import kotlin.UByte;

public class Hex {
    private static final byte[] DIGITS = new byte[103];
    private static final char[] FIRST_CHAR = new char[256];
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', Logger.LEVEL_D, Logger.LEVEL_E, 'F'};
    private static final char[] SECOND_CHAR = new char[256];

    static {
        for (int i = 0; i < 256; i++) {
            FIRST_CHAR[i] = HEX_DIGITS[(i >> 4) & 15];
            SECOND_CHAR[i] = HEX_DIGITS[i & 15];
        }
        for (int i2 = 0; i2 <= 70; i2++) {
            DIGITS[i2] = -1;
        }
        for (byte b = 0; b < 10; b = (byte) (b + 1)) {
            DIGITS[b + 48] = b;
        }
        for (byte b2 = 0; b2 < 6; b2 = (byte) (b2 + 1)) {
            byte b3 = (byte) (b2 + 10);
            DIGITS[b2 + 65] = b3;
            DIGITS[b2 + 97] = b3;
        }
    }

    public static String byte2Hex(int i) {
        if (i > 255 || i < 0) {
            throw new IllegalArgumentException("The int converting to hex should be in range 0~255");
        }
        return String.valueOf(FIRST_CHAR[i]) + String.valueOf(SECOND_CHAR[i]);
    }

    public static String encodeHex(byte[] bArr, boolean z) {
        byte b;
        char[] cArr = new char[(bArr.length * 2)];
        int i = 0;
        int i2 = 0;
        while (i < bArr.length && ((b = bArr[i] & UByte.MAX_VALUE) != 0 || !z)) {
            int i3 = i2 + 1;
            cArr[i2] = FIRST_CHAR[b];
            i2 = i3 + 1;
            cArr[i3] = SECOND_CHAR[b];
            i++;
        }
        return new String(cArr, 0, i2);
    }

    public static byte[] decodeHex(String str) {
        boolean z;
        byte b;
        byte b2;
        int length = str.length();
        if ((length & 1) == 0) {
            byte[] bArr = new byte[(length >> 1)];
            int i = 0;
            int i2 = 0;
            while (true) {
                z = true;
                if (i >= length) {
                    z = false;
                    break;
                }
                int i3 = i + 1;
                char charAt = str.charAt(i);
                if (charAt > 'f' || (b = DIGITS[charAt]) == -1) {
                    break;
                }
                int i4 = i3 + 1;
                char charAt2 = str.charAt(i3);
                if (charAt2 > 'f' || (b2 = DIGITS[charAt2]) == -1) {
                    break;
                }
                bArr[i2] = (byte) ((b << 4) | b2);
                i2++;
                i = i4;
            }
            if (!z) {
                return bArr;
            }
            throw new IllegalArgumentException("Invalid hexadecimal digit: " + str);
        }
        throw new IllegalArgumentException("Odd number of characters.");
    }

    public static byte[] hexStringToByteArray(String str) {
        return decodeHex(str.replaceAll(Operators.SPACE_STR, ""));
    }
}
