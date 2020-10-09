package com.taobao.weex.ui.action;

import androidx.annotation.NonNull;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.component.WXComponent;

public class GraphicActionCreateFinish extends BasicGraphicAction {
    private int mLayoutHeight;
    private int mLayoutWidth;

    public GraphicActionCreateFinish(@NonNull WXSDKInstance wXSDKInstance) {
        super(wXSDKInstance, "");
        WXComponent rootComponent = wXSDKInstance.getRootComponent();
        if (rootComponent != null) {
            this.mLayoutWidth = (int) rootComponent.getLayoutWidth();
            this.mLayoutHeight = (int) rootComponent.getLayoutHeight();
        }
        wXSDKInstance.getApmForInstance().onStage(WXInstanceApm.KEY_PAGE_STAGES_CREATE_FINISH);
        wXSDKInstance.getApmForInstance().extInfo.put(WXInstanceApm.KEY_PAGE_STAGES_CREATE_FINISH, true);
    }

    public void executeAction() {
        WXSDKInstance wXSDKIntance = getWXSDKIntance();
        if (wXSDKIntance != null && wXSDKIntance.getContext() != null && !wXSDKIntance.mHasCreateFinish) {
            if (wXSDKIntance.getRenderStrategy() == WXRenderStrategy.APPEND_ONCE) {
                wXSDKIntance.onCreateFinish();
            } else if (!"platform".equals(wXSDKIntance.getRenderType())) {
                wXSDKIntance.onCreateFinish();
            }
            wXSDKIntance.mHasCreateFinish = true;
            if (wXSDKIntance.getWXPerformance() != null) {
                wXSDKIntance.getWXPerformance().callCreateFinishTime = System.currentTimeMillis() - wXSDKIntance.getWXPerformance().renderTimeOrigin;
            }
            wXSDKIntance.onOldFsRenderTimeLogic();
        }
    }
}
