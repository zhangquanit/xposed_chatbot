package com.uc.webview.export.extension;

import com.uc.webview.export.annotations.Interface;

@Interface
/* compiled from: U4Source */
public class MustOverrideException extends RuntimeException {
    public MustOverrideException() {
        super("abstract function called: must be overriden!");
    }
}
