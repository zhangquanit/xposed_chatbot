package com.taobao.tao.log.godeye.core.plugin.runtime;

import com.taobao.tao.log.godeye.api.plugin.IPluginInitializerContextAware;
import com.taobao.tao.log.godeye.core.control.Godeye;
import java.io.Serializable;

public abstract class Plugin {
    protected PluginData pluginData;

    public abstract void execute() throws Exception;

    public Plugin(PluginData pluginData2) {
        this.pluginData = pluginData2;
    }

    /* access modifiers changed from: protected */
    public void executePluginMainClass(Class<?> cls) throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        if (cls != null && IPluginInitializerContextAware.class.isAssignableFrom(cls)) {
            ((IPluginInitializerContextAware) cls.newInstance()).init(Godeye.sharedInstance().getApplication(), Godeye.sharedInstance());
        }
    }

    public static class PluginData implements Serializable {
        private String mainClass;

        public String getMainClass() {
            return this.mainClass;
        }

        public void setMainClass(String str) {
            this.mainClass = str;
        }
    }
}
