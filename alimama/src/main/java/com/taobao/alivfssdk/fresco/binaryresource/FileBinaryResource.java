package com.taobao.alivfssdk.fresco.binaryresource;

import com.taobao.alivfssdk.fresco.common.internal.Files;
import com.taobao.alivfssdk.fresco.common.internal.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileBinaryResource implements BinaryResource {
    private final File mFile;

    private FileBinaryResource(File file) {
        this.mFile = (File) Preconditions.checkNotNull(file);
    }

    public File getFile() {
        return this.mFile;
    }

    public InputStream openStream() throws IOException {
        return new FileInputStream(this.mFile);
    }

    public long size() {
        return this.mFile.length();
    }

    public byte[] read() throws IOException {
        return Files.toByteArray(this.mFile);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof FileBinaryResource)) {
            return false;
        }
        return this.mFile.equals(((FileBinaryResource) obj).mFile);
    }

    public int hashCode() {
        return this.mFile.hashCode();
    }

    public static FileBinaryResource createOrNull(File file) {
        if (file != null) {
            return new FileBinaryResource(file);
        }
        return null;
    }
}