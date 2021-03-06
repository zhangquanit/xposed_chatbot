package com.taobao.android.dinamic.expression.parser.resolver;

import java.util.Map;

class MapValueResolver implements ValueResolver {
    MapValueResolver() {
    }

    public boolean canResolve(Object obj, Class<?> cls, String str) {
        return obj instanceof Map;
    }

    public Object resolve(Object obj, Class<?> cls, String str) {
        return ((Map) obj).get(str);
    }
}
