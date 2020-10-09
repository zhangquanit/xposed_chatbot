package android.taobao.windvane.util;

import com.alibaba.android.enhance.svg.SVGPlugin;

public enum MimeTypeEnum {
    JS("js", "application/x-javascript"),
    CSS("css", "text/css"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpep", "image/jpeg"),
    SVG(SVGPlugin.TAG, "image/svg+xml"),
    PNG("png", "image/png"),
    WEBP("webp", "image/webp"),
    GIF("gif", "image/gif"),
    HTM("htm", "text/html"),
    HTML("html", "text/html");
    
    private String mimeType;
    private String suffix;

    private MimeTypeEnum(String str, String str2) {
        this.suffix = str;
        this.mimeType = str2;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String str) {
        this.suffix = str;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String str) {
        this.mimeType = str;
    }
}
