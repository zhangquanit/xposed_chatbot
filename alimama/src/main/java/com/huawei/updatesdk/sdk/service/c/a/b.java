package com.huawei.updatesdk.sdk.service.c.a;

import com.huawei.updatesdk.sdk.a.d.d;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class b {
    private static final char COMMA = ',';
    private static final String END_FLAG = "_";
    private static final String TAG = "b";

    private static class a implements PrivilegedAction {
        private Field a;

        public a(Field field) {
            this.a = field;
        }

        public Object run() {
            this.a.setAccessible(true);
            return null;
        }
    }

    private String arrayToJson(Object obj) throws IllegalAccessException, IllegalArgumentException {
        int length = Array.getLength(obj);
        if (length <= 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.ARRAY_START_STR);
        for (int i = 0; i < length; i++) {
            String valueToJson = valueToJson(Array.get(obj, i));
            if (valueToJson != null) {
                sb.append(valueToJson);
                sb.append(',');
            }
        }
        formatJsonStr(sb);
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }

    private void formatJsonStr(StringBuilder sb) {
        int length = sb.length();
        if (length > 0) {
            int i = length - 1;
            if (sb.charAt(i) == ',') {
                sb.delete(i, length);
            }
        }
    }

    private Object jsonBeanFromJson(Class cls, Object obj) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, JSONException {
        b bVar = (b) cls.newInstance();
        bVar.fromJson((JSONObject) obj);
        return bVar;
    }

    private String mapToJson(Map map) throws IllegalAccessException, IllegalArgumentException {
        if (map.size() <= 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.BLOCK_START_STR);
        Iterator it = map.entrySet().iterator();
        while (true) {
            Map.Entry entry = (Map.Entry) it.next();
            String str = (String) entry.getKey();
            String valueToJson = valueToJson(entry.getValue());
            if (valueToJson != null) {
                sb.append("\"");
                sb.append(str);
                sb.append("\":");
                sb.append(valueToJson);
            }
            if (!it.hasNext()) {
                sb.append("}");
                return sb.toString();
            } else if (valueToJson != null) {
                sb.append(',');
            }
        }
    }

    private void processValueError(Field field, Object obj) {
        Object valueOf;
        if (obj != null && (obj instanceof String)) {
            try {
                Class<?> type = field.getType();
                if (type.isPrimitive()) {
                    String name = type.getName();
                    if ("int".equals(name)) {
                        valueOf = Integer.valueOf(Integer.parseInt((String) obj));
                    } else if ("float".equals(name)) {
                        valueOf = Float.valueOf(Float.parseFloat((String) obj));
                    } else if ("long".equals(name)) {
                        valueOf = Long.valueOf(Long.parseLong((String) obj));
                    } else if ("boolean".equals(name)) {
                        valueOf = Boolean.valueOf(Boolean.parseBoolean((String) obj));
                    } else if ("double".equals(name)) {
                        valueOf = Double.valueOf(Double.parseDouble((String) obj));
                    } else if ("short".equals(name)) {
                        valueOf = Short.valueOf(Short.parseShort((String) obj));
                    } else if ("byte".equals(name)) {
                        valueOf = Byte.valueOf(Byte.parseByte((String) obj));
                    } else if ("char".equals(name)) {
                        valueOf = Character.valueOf(((String) obj).charAt(0));
                    } else {
                        return;
                    }
                    field.set(this, valueOf);
                }
            } catch (IllegalAccessException e) {
                String str = TAG;
                com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, "processValueError Throwable:" + e.getMessage());
            }
        }
    }

    private Object valueFromJson(Class cls, Class cls2, Object obj) throws IllegalAccessException, IllegalArgumentException, InstantiationException, ClassNotFoundException, JSONException {
        if (cls.isPrimitive() || cls.equals(String.class)) {
            return (!"float".equals(cls.getName()) || !(obj instanceof Double)) ? obj : Float.valueOf(((Double) obj).floatValue());
        }
        if (List.class.isAssignableFrom(cls)) {
            return listFromJson(cls2, obj);
        }
        if (b.class.isAssignableFrom(cls)) {
            if (!cls.equals(b.class)) {
                return jsonBeanFromJson(cls, obj);
            }
            throw new IllegalArgumentException("error type, type:" + cls);
        } else if (Map.class.isAssignableFrom(cls)) {
            return mapFromJson(cls2, obj);
        } else {
            throw new IllegalArgumentException("unsupport type, Type:" + cls);
        }
    }

    private String valueToJson(Object obj) throws IllegalAccessException, IllegalArgumentException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return JSONObject.quote(obj.toString());
        }
        if ((obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Boolean) || (obj instanceof Float) || (obj instanceof Byte) || (obj instanceof Character) || (obj instanceof Double) || (obj instanceof Short)) {
            return String.valueOf(obj);
        }
        if (obj instanceof b) {
            return ((b) obj).toJson();
        }
        if (obj instanceof List) {
            return listToJson((List) obj);
        }
        if (obj instanceof Map) {
            return mapToJson((Map) obj);
        }
        if (obj.getClass().isArray()) {
            return arrayToJson(obj);
        }
        return null;
    }

    public void fromJson(JSONObject jSONObject) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, JSONException {
        Field field;
        Field[] a2 = d.a((Class) getClass());
        Object obj = null;
        for (int i = 0; i < a2.length; i++) {
            AccessController.doPrivileged(new a(a2[i]));
            String name = a2[i].getName();
            if (name.endsWith("_")) {
                String substring = name.substring(0, name.length() - "_".length());
                if (jSONObject.has(substring)) {
                    Object obj2 = jSONObject.get(substring);
                    if (!JSONObject.NULL.equals(obj2)) {
                        try {
                            Object valueFromJson = valueFromJson(a2[i].getType(), d.a(a2[i]), obj2);
                            try {
                                a2[i].set(this, valueFromJson);
                                obj = valueFromJson;
                            } catch (IllegalAccessException e) {
                                Object obj3 = valueFromJson;
                                e = e;
                                obj = obj3;
                                com.huawei.updatesdk.sdk.a.c.a.a.a.a(TAG, getClass().getName() + ".fromJson error, fieldName:" + substring, e);
                                field = a2[i];
                                processValueError(field, obj);
                            } catch (IllegalArgumentException e2) {
                                Object obj4 = valueFromJson;
                                e = e2;
                                obj = obj4;
                                com.huawei.updatesdk.sdk.a.c.a.a.a.a(TAG, getClass().getName() + ".fromJson error, fieldName:" + substring, e);
                                field = a2[i];
                                processValueError(field, obj);
                            } catch (InstantiationException e3) {
                                Object obj5 = valueFromJson;
                                e = e3;
                                obj = obj5;
                                com.huawei.updatesdk.sdk.a.c.a.a.a.a(TAG, getClass().getName() + ".fromJson error, fieldName:" + substring, e);
                                field = a2[i];
                                processValueError(field, obj);
                            } catch (ClassNotFoundException e4) {
                                Object obj6 = valueFromJson;
                                e = e4;
                                obj = obj6;
                                com.huawei.updatesdk.sdk.a.c.a.a.a.a(TAG, getClass().getName() + ".fromJson error, fieldName:" + substring, e);
                                field = a2[i];
                                processValueError(field, obj);
                            } catch (JSONException e5) {
                                Object obj7 = valueFromJson;
                                e = e5;
                                obj = obj7;
                                com.huawei.updatesdk.sdk.a.c.a.a.a.a(TAG, getClass().getName() + ".fromJson error, fieldName:" + substring, e);
                                field = a2[i];
                                processValueError(field, obj);
                            }
                        } catch (IllegalAccessException e6) {
                            e = e6;
                            com.huawei.updatesdk.sdk.a.c.a.a.a.a(TAG, getClass().getName() + ".fromJson error, fieldName:" + substring, e);
                            field = a2[i];
                            processValueError(field, obj);
                        } catch (IllegalArgumentException e7) {
                            e = e7;
                            com.huawei.updatesdk.sdk.a.c.a.a.a.a(TAG, getClass().getName() + ".fromJson error, fieldName:" + substring, e);
                            field = a2[i];
                            processValueError(field, obj);
                        } catch (InstantiationException e8) {
                            e = e8;
                            com.huawei.updatesdk.sdk.a.c.a.a.a.a(TAG, getClass().getName() + ".fromJson error, fieldName:" + substring, e);
                            field = a2[i];
                            processValueError(field, obj);
                        } catch (ClassNotFoundException e9) {
                            e = e9;
                            com.huawei.updatesdk.sdk.a.c.a.a.a.a(TAG, getClass().getName() + ".fromJson error, fieldName:" + substring, e);
                            field = a2[i];
                            processValueError(field, obj);
                        } catch (JSONException e10) {
                            e = e10;
                            com.huawei.updatesdk.sdk.a.c.a.a.a.a(TAG, getClass().getName() + ".fromJson error, fieldName:" + substring, e);
                            field = a2[i];
                            processValueError(field, obj);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public Object listFromJson(Class cls, Object obj) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, JSONException {
        if (cls == null) {
            throw new IllegalArgumentException("generic type is null");
        } else if (obj instanceof JSONArray) {
            ArrayList arrayList = new ArrayList();
            JSONArray jSONArray = (JSONArray) obj;
            for (int i = 0; i < jSONArray.length(); i++) {
                Object valueFromJson = valueFromJson(cls, (Class) null, jSONArray.get(i));
                if (valueFromJson != null) {
                    if (cls.equals(valueFromJson.getClass())) {
                        arrayList.add(valueFromJson);
                    } else {
                        String str = TAG;
                        com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, "listFromJson error, memberClass:" + cls + ", valueClass:" + valueFromJson.getClass());
                    }
                }
            }
            return arrayList;
        } else {
            throw new IllegalArgumentException("jsonobject is not JSONArray, jsonValue:" + obj);
        }
    }

    /* access modifiers changed from: protected */
    public String listToJson(List list) throws IllegalAccessException, IllegalArgumentException {
        if (list.size() <= 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.ARRAY_START_STR);
        for (int i = 0; i < list.size(); i++) {
            String valueToJson = valueToJson(list.get(i));
            if (valueToJson != null) {
                sb.append(valueToJson);
                sb.append(',');
            }
        }
        formatJsonStr(sb);
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public Object mapFromJson(Class cls, Object obj) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, JSONException {
        if (cls == null) {
            throw new IllegalArgumentException("generic type is null");
        } else if (obj instanceof JSONObject) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            JSONObject jSONObject = (JSONObject) obj;
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                Object valueFromJson = valueFromJson(cls, (Class) null, jSONObject.get(next));
                if (valueFromJson != null) {
                    if (cls.equals(valueFromJson.getClass())) {
                        linkedHashMap.put(next, valueFromJson);
                    } else {
                        String str = TAG;
                        com.huawei.updatesdk.sdk.a.c.a.a.a.d(str, "mapFromJson error, memberClass:" + cls + ", valueClass:" + valueFromJson.getClass());
                    }
                }
            }
            return linkedHashMap;
        } else {
            throw new IllegalArgumentException("jsonobject is not JSONObject, jsonValue:" + obj);
        }
    }

    public String toJson() throws IllegalAccessException, IllegalArgumentException {
        Field[] a2 = d.a((Class) getClass());
        if (a2.length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.BLOCK_START_STR);
        for (int i = 0; i < a2.length; i++) {
            AccessController.doPrivileged(new a(a2[i]));
            String name = a2[i].getName();
            if (name != null && name.endsWith("_")) {
                String substring = name.substring(0, name.length() - "_".length());
                String valueToJson = valueToJson(a2[i].get(this));
                if (valueToJson != null) {
                    sb.append("\"");
                    sb.append(substring);
                    sb.append("\":");
                    sb.append(valueToJson);
                    sb.append(',');
                }
            }
        }
        formatJsonStr(sb);
        sb.append("}");
        return sb.toString();
    }
}
