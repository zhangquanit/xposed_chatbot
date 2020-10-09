package androidx.core.net;

import android.net.Uri;
import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\r\u0010\u0003\u001a\u00020\u0002*\u00020\u0001H\b\u001a\r\u0010\u0003\u001a\u00020\u0002*\u00020\u0004H\b¨\u0006\u0005"}, d2 = {"toFile", "Ljava/io/File;", "Landroid/net/Uri;", "toUri", "", "core-ktx_release"}, k = 2, mv = {1, 1, 10})
/* compiled from: Uri.kt */
public final class UriKt {
    @NotNull
    public static final Uri toUri(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        Uri parse = Uri.parse(str);
        Intrinsics.checkExpressionValueIsNotNull(parse, "Uri.parse(this)");
        return parse;
    }

    @NotNull
    public static final Uri toUri(@NotNull File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Uri fromFile = Uri.fromFile(file);
        Intrinsics.checkExpressionValueIsNotNull(fromFile, "Uri.fromFile(this)");
        return fromFile;
    }

    @NotNull
    public static final File toFile(@NotNull Uri uri) {
        Intrinsics.checkParameterIsNotNull(uri, "$receiver");
        if (Intrinsics.areEqual((Object) uri.getScheme(), (Object) "file")) {
            return new File(uri.getPath());
        }
        throw new IllegalArgumentException(("Uri lacks 'file' scheme: " + uri).toString());
    }
}
