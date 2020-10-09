package android.taobao.windvane;

public class WindvaneException extends Exception {
    private static final long serialVersionUID = -4046535764429263458L;
    private int errorCode;

    public WindvaneException() {
    }

    public WindvaneException(String str) {
        super(str);
    }

    public WindvaneException(String str, int i) {
        super(str);
        this.errorCode = i;
    }

    public WindvaneException(String str, Throwable th) {
        super(str, th);
    }

    public WindvaneException(Throwable th) {
        super(th);
    }

    public WindvaneException(Throwable th, int i) {
        super(th);
        this.errorCode = i;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
