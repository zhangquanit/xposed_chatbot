package com.alibaba.aliweex;

import java.util.Map;

public interface IConfigAdapter {
    boolean checkMode(String str);

    String getConfig(String str, String str2, String str3);

    Map<String, String> getConfigs(String str);
}
