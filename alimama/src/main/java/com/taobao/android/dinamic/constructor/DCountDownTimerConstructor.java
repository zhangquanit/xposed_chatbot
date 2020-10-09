package com.taobao.android.dinamic.constructor;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.dinamic.property.DAttrUtils;
import com.taobao.android.dinamic.property.ScreenTool;
import com.taobao.android.dinamic.view.DCountDownTimerView;
import java.util.ArrayList;
import java.util.Map;

public class DCountDownTimerConstructor extends DinamicViewAdvancedConstructor {
    public static final int DEFAULT_TEXT_COLOR = -16777216;
    public static final String TAG = "DCountDownTimerConstructor";
    private final int MARGIN_BOTTOM = 3;
    private final int MARGIN_LEFT = 0;
    private final int MARGIN_RIGHT = 2;
    private final int MARGIN_TOP = 1;
    private final int TEXT_COLOR = 7;
    private final int TEXT_SIZE = 6;
    private final int[] colonTextDefaults = {0, 0, 0, 0, -1, -1, 10, -16777216};
    private final int[] seeMoreTextDefaults = {0, 0, 0, 0, -1, -1, 12, -16777216};
    private final int[] timerTextDefaults = {0, 0, 0, 0, 20, 20, 12, -1};

    public View initializeView(String str, Context context, AttributeSet attributeSet) {
        return new DCountDownTimerView(context, attributeSet);
    }

    public void setAttributes(View view, Map<String, Object> map, ArrayList<String> arrayList, DinamicParams dinamicParams) {
        Map<String, Object> map2 = map;
        ArrayList<String> arrayList2 = arrayList;
        super.setAttributes(view, map, arrayList, dinamicParams);
        DCountDownTimerView dCountDownTimerView = (DCountDownTimerView) view;
        if (arrayList2.contains(DAttrConstant.CV_TIMER_TEXT_MARGIN_LEFT) || arrayList2.contains(DAttrConstant.CV_TIMER_TEXT_MARGIN_TOP) || arrayList2.contains(DAttrConstant.CV_TIMER_TEXT_MARGIN_RIGHT) || arrayList2.contains(DAttrConstant.CV_TIMER_TEXT_MARGIN_BOTTOM) || arrayList2.contains(DAttrConstant.CV_TIMER_TEXT_WIDTH) || arrayList2.contains(DAttrConstant.CV_TIMER_TEXT_HEIGHT) || arrayList2.contains(DAttrConstant.CV_TIMER_TEXT_SIZE) || arrayList2.contains(DAttrConstant.CV_TIMER_TEXT_COLOR) || arrayList2.contains(DAttrConstant.CV_TIMER_BACKGROUND_COLOR) || arrayList2.contains(DAttrConstant.CV_TIMER_CORNER_RADIUS)) {
            setTimerTextViewStyle(dCountDownTimerView, (String) map2.get(DAttrConstant.CV_TIMER_TEXT_MARGIN_LEFT), (String) map2.get(DAttrConstant.CV_TIMER_TEXT_MARGIN_TOP), (String) map2.get(DAttrConstant.CV_TIMER_TEXT_MARGIN_RIGHT), (String) map2.get(DAttrConstant.CV_TIMER_TEXT_MARGIN_BOTTOM), (String) map2.get(DAttrConstant.CV_TIMER_TEXT_WIDTH), (String) map2.get(DAttrConstant.CV_TIMER_TEXT_HEIGHT), (String) map2.get(DAttrConstant.CV_TIMER_TEXT_SIZE), (String) map2.get(DAttrConstant.CV_TIMER_TEXT_COLOR), (String) map2.get(DAttrConstant.CV_TIMER_BACKGROUND_COLOR), (String) map2.get(DAttrConstant.CV_TIMER_CORNER_RADIUS));
        }
        if (arrayList2.contains(DAttrConstant.CV_COLON_TEXT_MARGIN_LEFT) || arrayList2.contains(DAttrConstant.CV_COLON_TEXT_MARGIN_TOP) || arrayList2.contains(DAttrConstant.CV_COLON_TEXT_MARGIN_RIGHT) || arrayList2.contains(DAttrConstant.CV_COLON_TEXT_MARGIN_BOTTOM) || arrayList2.contains(DAttrConstant.CV_COLON_TEXT_WIDTH) || arrayList2.contains(DAttrConstant.CV_COLON_TEXT_HEIGHT) || arrayList2.contains(DAttrConstant.CV_COLON_TEXT_SIZE) || arrayList2.contains(DAttrConstant.CV_COLON_TEXT_COLOR) || arrayList2.contains(DAttrConstant.CV_COLON_TEXT)) {
            setColonTextViewStyle(dCountDownTimerView, (String) map2.get(DAttrConstant.CV_COLON_TEXT_MARGIN_LEFT), (String) map2.get(DAttrConstant.CV_COLON_TEXT_MARGIN_TOP), (String) map2.get(DAttrConstant.CV_COLON_TEXT_MARGIN_RIGHT), (String) map2.get(DAttrConstant.CV_COLON_TEXT_MARGIN_BOTTOM), (String) map2.get(DAttrConstant.CV_COLON_TEXT_WIDTH), (String) map2.get(DAttrConstant.CV_COLON_TEXT_HEIGHT), (String) map2.get(DAttrConstant.CV_COLON_TEXT_SIZE), (String) map2.get(DAttrConstant.CV_COLON_TEXT_COLOR), (String) map2.get(DAttrConstant.CV_COLON_TEXT));
        }
        if (arrayList2.contains(DAttrConstant.CV_FUTURE_TIME) || arrayList2.contains(DAttrConstant.CV_CURRENT_TIME)) {
            setFutureTime(dCountDownTimerView, (String) map2.get(DAttrConstant.CV_FUTURE_TIME), (String) map2.get(DAttrConstant.CV_CURRENT_TIME));
        }
        if (arrayList2.contains(DAttrConstant.CV_SEE_MORE_TEXT) || arrayList2.contains(DAttrConstant.CV_SEE_MORE_TEXT_MARGIN_LEFT) || arrayList2.contains(DAttrConstant.CV_SEE_MORE_TEXT_MARGIN_TOP) || arrayList2.contains(DAttrConstant.CV_SEE_MORE_TEXT_MARGIN_RIGHT) || arrayList2.contains(DAttrConstant.CV_SEE_MORE_TEXT_MARGIN_BOTTOM) || arrayList2.contains(DAttrConstant.CV_SEE_MORE_TEXT_WIDTH) || arrayList2.contains(DAttrConstant.CV_SEE_MORE_TEXT_HEIGHT) || arrayList2.contains(DAttrConstant.CV_SEE_MORE_TEXT_SIZE) || arrayList2.contains(DAttrConstant.CV_SEE_MORE_TEXT_COLOR)) {
            setSeeMoreTextViewStyle(dCountDownTimerView, (String) map2.get(DAttrConstant.CV_SEE_MORE_TEXT), (String) map2.get(DAttrConstant.CV_SEE_MORE_TEXT_MARGIN_LEFT), (String) map2.get(DAttrConstant.CV_SEE_MORE_TEXT_MARGIN_TOP), (String) map2.get(DAttrConstant.CV_SEE_MORE_TEXT_MARGIN_RIGHT), (String) map2.get(DAttrConstant.CV_SEE_MORE_TEXT_MARGIN_BOTTOM), (String) map2.get(DAttrConstant.CV_SEE_MORE_TEXT_WIDTH), (String) map2.get(DAttrConstant.CV_SEE_MORE_TEXT_HEIGHT), (String) map2.get(DAttrConstant.CV_SEE_MORE_TEXT_SIZE), (String) map2.get(DAttrConstant.CV_SEE_MORE_TEXT_COLOR));
        }
    }

    public void setSeeMoreTextViewStyle(DCountDownTimerView dCountDownTimerView, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        TextView seeMoreView = dCountDownTimerView.getSeeMoreView();
        String str10 = str;
        seeMoreView.setText(str);
        setTextViewStyle(seeMoreView, str2, str3, str4, str5, str6, str7, str8, str9, this.seeMoreTextDefaults);
    }

    public void setFutureTime(DCountDownTimerView dCountDownTimerView, String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            dCountDownTimerView.hideCountDown();
            dCountDownTimerView.setFutureTime(-1);
            dCountDownTimerView.getTimer().stop();
            return;
        }
        dCountDownTimerView.setFutureTime(Long.valueOf(str).longValue());
        if (!TextUtils.isEmpty(str2)) {
            dCountDownTimerView.setCurrentTime(Long.valueOf(str2).longValue());
        }
        if (dCountDownTimerView.getLastTime() > 0) {
            dCountDownTimerView.showCountDown();
            dCountDownTimerView.updateCountDownViewTime();
            dCountDownTimerView.getTimer().start();
            return;
        }
        dCountDownTimerView.hideCountDown();
        dCountDownTimerView.getTimer().stop();
    }

    public void setColonTextViewStyle(DCountDownTimerView dCountDownTimerView, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        String str10 = str9;
        TextView colonFirst = dCountDownTimerView.getColonFirst();
        TextView colonSecond = dCountDownTimerView.getColonSecond();
        String str11 = str;
        String str12 = str2;
        String str13 = str3;
        String str14 = str4;
        String str15 = str5;
        String str16 = str6;
        String str17 = str7;
        String str18 = str8;
        setTextViewStyle(colonFirst, str11, str12, str13, str14, str15, str16, str17, str18, this.colonTextDefaults);
        setTextViewStyle(colonSecond, str11, str12, str13, str14, str15, str16, str17, str18, this.colonTextDefaults);
        if (!TextUtils.isEmpty(str9)) {
            colonFirst.setText(str10);
            colonSecond.setText(str10);
        }
    }

    public void setTimerTextViewStyle(DCountDownTimerView dCountDownTimerView, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        TextView hour = dCountDownTimerView.getHour();
        TextView minute = dCountDownTimerView.getMinute();
        TextView second = dCountDownTimerView.getSecond();
        String str11 = str;
        String str12 = str2;
        String str13 = str3;
        String str14 = str4;
        String str15 = str5;
        String str16 = str6;
        String str17 = str7;
        String str18 = str8;
        setTextViewStyle(hour, str11, str12, str13, str14, str15, str16, str17, str18, this.timerTextDefaults);
        setTextViewStyle(minute, str11, str12, str13, str14, str15, str16, str17, str18, this.timerTextDefaults);
        setTextViewStyle(second, str11, str12, str13, str14, str15, str16, str17, str18, this.timerTextDefaults);
        setTimerTextBackground(hour, minute, second, str9, str10);
    }

    private void setTextViewStyle(TextView textView, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, int[] iArr) {
        int parseColor;
        TextView textView2 = textView;
        if (!TextUtils.isEmpty(str7)) {
            textView.setTextSize(0, (float) ScreenTool.getPx(textView.getContext(), str7, 0));
        }
        if (!TextUtils.isEmpty(str8) && (parseColor = DAttrUtils.parseColor(str8, -16777216)) != -16777216) {
            textView.setTextColor(parseColor);
        }
        int[] textViewMargin = getTextViewMargin(textView.getContext(), str, str2, str3, str4, iArr);
        if (textViewMargin != null || !TextUtils.isEmpty(str5) || !TextUtils.isEmpty(str6)) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
            if (!TextUtils.isEmpty(str5)) {
                marginLayoutParams.width = ScreenTool.getPx(textView.getContext(), str5, 0);
            }
            if (!TextUtils.isEmpty(str6)) {
                marginLayoutParams.height = ScreenTool.getPx(textView.getContext(), str6, 0);
            }
            if (textViewMargin != null) {
                marginLayoutParams.setMargins(textViewMargin[0], textViewMargin[1], textViewMargin[2], textViewMargin[3]);
            }
            textView.setLayoutParams(marginLayoutParams);
        }
    }

    private void setTimerTextBackground(TextView textView, TextView textView2, TextView textView3, String str, String str2) {
        int parseColor = !TextUtils.isEmpty(str) ? DAttrUtils.parseColor(str, -16777216) : -16777216;
        int px = ScreenTool.getPx(textView.getContext(), str2, 0);
        if (parseColor != -16777216 || px != 0) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius((float) px);
            gradientDrawable.setColor(parseColor);
            textView.setBackgroundDrawable(gradientDrawable);
            textView2.setBackgroundDrawable(gradientDrawable);
            textView3.setBackgroundDrawable(gradientDrawable);
        }
    }

    public void applyDefaultProperty(View view, Map<String, Object> map, DinamicParams dinamicParams) {
        super.applyDefaultProperty(view, map, dinamicParams);
        DCountDownTimerView dCountDownTimerView = (DCountDownTimerView) view;
        TextView seeMoreView = dCountDownTimerView.getSeeMoreView();
        TextView hour = dCountDownTimerView.getHour();
        TextView minute = dCountDownTimerView.getMinute();
        TextView second = dCountDownTimerView.getSecond();
        TextView colonFirst = dCountDownTimerView.getColonFirst();
        TextView colonSecond = dCountDownTimerView.getColonSecond();
        setTextViewMarginAndColorAndSize(seeMoreView, this.seeMoreTextDefaults);
        seeMoreView.setText("");
        setTextViewMarginAndColorAndSize(hour, this.timerTextDefaults);
        setTextViewMarginAndColorAndSize(minute, this.timerTextDefaults);
        setTextViewMarginAndColorAndSize(second, this.timerTextDefaults);
        hour.setPadding(0, 0, 0, 0);
        minute.setPadding(0, 0, 0, 0);
        second.setPadding(0, 0, 0, 0);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(0.0f);
        gradientDrawable.setColor(-16777216);
        hour.setBackgroundDrawable(gradientDrawable);
        minute.setBackgroundDrawable(gradientDrawable);
        second.setBackgroundDrawable(gradientDrawable);
        setTextViewMarginAndColorAndSize(colonFirst, this.colonTextDefaults);
        setTextViewMarginAndColorAndSize(colonSecond, this.colonTextDefaults);
        colonFirst.setText(":");
        colonSecond.setText(":");
    }

    private void setTextViewMarginAndColorAndSize(TextView textView, int[] iArr) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        marginLayoutParams.setMargins(iArr[0], iArr[1], iArr[2], iArr[3]);
        textView.setLayoutParams(marginLayoutParams);
        textView.setTextColor(iArr[7]);
        textView.setTextSize(1, (float) iArr[6]);
    }

    private int[] getTextViewMargin(Context context, String str, String str2, String str3, String str4, int[] iArr) {
        int px = ScreenTool.getPx(context, str, iArr[0]);
        int px2 = ScreenTool.getPx(context, str2, iArr[1]);
        int px3 = ScreenTool.getPx(context, str3, iArr[2]);
        int px4 = ScreenTool.getPx(context, str4, iArr[3]);
        if (px == iArr[0] && px2 == iArr[1] && px3 == iArr[2] && px4 == iArr[3]) {
            return null;
        }
        return new int[]{px, px2, px3, px4};
    }
}
