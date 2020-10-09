package com.airbnb.lottie.parser;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableGradientColorValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.content.GradientStroke;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.model.content.ShapeStroke;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.weex.ui.component.WXComponent;
import java.io.IOException;
import java.util.ArrayList;

class GradientStrokeParser {
    private static final JsonReader.Options DASH_PATTERN_NAMES = JsonReader.Options.of("n", "v");
    private static final JsonReader.Options GRADIENT_NAMES = JsonReader.Options.of("p", "k");
    private static JsonReader.Options NAMES = JsonReader.Options.of("nm", "g", "o", "t", "s", "e", WXComponent.PROP_FS_WRAP_CONTENT, DinamicConstant.LOWER_PREFIX, "lj", "ml", "hd", "d");

    private GradientStrokeParser() {
    }

    static GradientStroke parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        JsonReader jsonReader2 = jsonReader;
        LottieComposition lottieComposition2 = lottieComposition;
        ArrayList arrayList = new ArrayList();
        String str = null;
        GradientType gradientType = null;
        AnimatableGradientColorValue animatableGradientColorValue = null;
        AnimatableIntegerValue animatableIntegerValue = null;
        AnimatablePointValue animatablePointValue = null;
        AnimatablePointValue animatablePointValue2 = null;
        AnimatableFloatValue animatableFloatValue = null;
        ShapeStroke.LineCapType lineCapType = null;
        ShapeStroke.LineJoinType lineJoinType = null;
        float f = 0.0f;
        AnimatableFloatValue animatableFloatValue2 = null;
        boolean z = false;
        while (jsonReader.hasNext()) {
            switch (jsonReader2.selectName(NAMES)) {
                case 0:
                    str = jsonReader.nextString();
                    break;
                case 1:
                    int i = -1;
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        switch (jsonReader2.selectName(GRADIENT_NAMES)) {
                            case 0:
                                i = jsonReader.nextInt();
                                break;
                            case 1:
                                animatableGradientColorValue = AnimatableValueParser.parseGradientColor(jsonReader2, lottieComposition2, i);
                                break;
                            default:
                                jsonReader.skipName();
                                jsonReader.skipValue();
                                break;
                        }
                    }
                    jsonReader.endObject();
                    break;
                case 2:
                    animatableIntegerValue = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                    break;
                case 3:
                    gradientType = jsonReader.nextInt() == 1 ? GradientType.LINEAR : GradientType.RADIAL;
                    break;
                case 4:
                    animatablePointValue = AnimatableValueParser.parsePoint(jsonReader, lottieComposition);
                    break;
                case 5:
                    animatablePointValue2 = AnimatableValueParser.parsePoint(jsonReader, lottieComposition);
                    break;
                case 6:
                    animatableFloatValue = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    break;
                case 7:
                    lineCapType = ShapeStroke.LineCapType.values()[jsonReader.nextInt() - 1];
                    break;
                case 8:
                    lineJoinType = ShapeStroke.LineJoinType.values()[jsonReader.nextInt() - 1];
                    break;
                case 9:
                    f = (float) jsonReader.nextDouble();
                    break;
                case 10:
                    z = jsonReader.nextBoolean();
                    break;
                case 11:
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        String str2 = null;
                        AnimatableFloatValue animatableFloatValue3 = null;
                        while (jsonReader.hasNext()) {
                            switch (jsonReader2.selectName(DASH_PATTERN_NAMES)) {
                                case 0:
                                    str2 = jsonReader.nextString();
                                    break;
                                case 1:
                                    animatableFloatValue3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                                    break;
                                default:
                                    jsonReader.skipName();
                                    jsonReader.skipValue();
                                    break;
                            }
                        }
                        jsonReader.endObject();
                        if (str2.equals("o")) {
                            animatableFloatValue2 = animatableFloatValue3;
                        } else if (str2.equals("d") || str2.equals("g")) {
                            lottieComposition2.setHasDashPattern(true);
                            arrayList.add(animatableFloatValue3);
                        }
                    }
                    jsonReader.endArray();
                    if (arrayList.size() != 1) {
                        break;
                    } else {
                        arrayList.add(arrayList.get(0));
                        break;
                    }
                default:
                    jsonReader.skipName();
                    jsonReader.skipValue();
                    break;
            }
        }
        return new GradientStroke(str, gradientType, animatableGradientColorValue, animatableIntegerValue, animatablePointValue, animatablePointValue2, animatableFloatValue, lineCapType, lineJoinType, f, arrayList, animatableFloatValue2, z);
    }
}
