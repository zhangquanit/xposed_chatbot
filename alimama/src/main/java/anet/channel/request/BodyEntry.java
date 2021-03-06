package anet.channel.request;

import android.os.Parcelable;
import java.io.IOException;
import java.io.OutputStream;

public interface BodyEntry extends Parcelable {
    String getContentType();

    int writeTo(OutputStream outputStream) throws IOException;
}
