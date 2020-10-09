package com.taobao.weex.ui.action;

import androidx.collection.ArrayMap;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.dom.transition.WXTransition;
import com.taobao.weex.ui.component.WXComponent;
import java.util.Map;

public class GraphicActionUpdateStyle extends BasicGraphicAction {
    private WXComponent component;
    private boolean mIsBorderSet;
    private boolean mIsCausedByPesudo;
    private Map<String, Object> mStyle;

    public GraphicActionUpdateStyle(WXSDKInstance wXSDKInstance, String str, Map<String, Object> map, Map<String, String> map2, Map<String, String> map3, Map<String, String> map4) {
        this(wXSDKInstance, str, map, map2, map3, map4, false);
    }

    public GraphicActionUpdateStyle(WXSDKInstance wXSDKInstance, String str, Map<String, Object> map, CSSShorthand cSSShorthand, CSSShorthand cSSShorthand2, CSSShorthand cSSShorthand3, boolean z) {
        super(wXSDKInstance, str);
        this.mStyle = map;
        this.mIsCausedByPesudo = z;
        this.component = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), getRef());
        if (this.component != null) {
            if (this.mStyle != null) {
                this.component.updateStyle(this.mStyle, this.mIsCausedByPesudo);
                if (map.containsKey("transform") && this.component.getTransition() == null) {
                    ArrayMap arrayMap = new ArrayMap(2);
                    arrayMap.put("transform", map.get("transform"));
                    arrayMap.put(Constants.Name.TRANSFORM_ORIGIN, map.get(Constants.Name.TRANSFORM_ORIGIN));
                    this.component.addAnimationForElement(arrayMap);
                }
            }
            if (cSSShorthand != null) {
                this.component.setPaddings(cSSShorthand);
            }
            if (cSSShorthand2 != null) {
                this.component.setMargins(cSSShorthand2);
            }
            if (cSSShorthand3 != null) {
                this.mIsBorderSet = true;
                this.component.setBorders(cSSShorthand3);
            }
        }
    }

    public GraphicActionUpdateStyle(WXSDKInstance wXSDKInstance, String str, Map<String, Object> map, Map<String, String> map2, Map<String, String> map3, Map<String, String> map4, boolean z) {
        super(wXSDKInstance, str);
        this.mStyle = map;
        this.mIsCausedByPesudo = z;
        this.component = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), getRef());
        if (this.component != null) {
            if (this.mStyle != null) {
                this.component.addStyle(this.mStyle, this.mIsCausedByPesudo);
                if (map.containsKey("transform") && this.component.getTransition() == null) {
                    ArrayMap arrayMap = new ArrayMap(2);
                    arrayMap.put("transform", map.get("transform"));
                    arrayMap.put(Constants.Name.TRANSFORM_ORIGIN, map.get(Constants.Name.TRANSFORM_ORIGIN));
                    this.component.addAnimationForElement(arrayMap);
                    WXBridgeManager.getInstance().markDirty(this.component.getInstanceId(), this.component.getRef(), true);
                }
            }
            if (map2 != null) {
                this.component.addShorthand(map2);
            }
            if (map3 != null) {
                this.component.addShorthand(map3);
            }
            if (map4 != null) {
                this.mIsBorderSet = true;
                this.component.addShorthand(map4);
            }
        }
    }

    public void executeAction() {
        if (this.component != null) {
            if (this.mStyle != null) {
                if (this.component.getTransition() != null) {
                    this.component.getTransition().updateTranstionParams(this.mStyle);
                    if (this.component.getTransition().hasTransitionProperty(this.mStyle)) {
                        this.component.getTransition().startTransition(this.mStyle);
                        return;
                    }
                    return;
                }
                this.component.setTransition(WXTransition.fromMap(this.mStyle, this.component));
                this.component.updateStyles(this.mStyle);
            } else if (this.mIsBorderSet) {
                this.component.updateStyles(this.component);
            }
        }
    }
}
