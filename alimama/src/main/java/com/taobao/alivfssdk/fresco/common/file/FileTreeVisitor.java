package com.taobao.alivfssdk.fresco.common.file;

import java.io.File;

public interface FileTreeVisitor {
    void postVisitDirectory(File file);

    void preVisitDirectory(File file);

    void visitFile(File file);
}
