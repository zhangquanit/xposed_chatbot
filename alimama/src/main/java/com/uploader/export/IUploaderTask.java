package com.uploader.export;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Map;

public interface IUploaderTask {
    @NonNull
    String getBizType();

    @NonNull
    String getFilePath();

    @NonNull
    String getFileType();

    @Nullable
    Map<String, String> getMetaInfo();
}
