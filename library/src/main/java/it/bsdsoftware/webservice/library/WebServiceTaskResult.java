package it.bsdsoftware.webservice.library;

/**
 * Created by Simone on 30/06/16.
 */
public class WebServiceTaskResult<T> {

    public final boolean result;
    public final String message;
    public final Throwable exception;
    public T data;

    public static WebServiceTaskResult<?> ok = new WebServiceTaskResult(true);

    public static WebServiceTaskResult fail(String msg) {
        return fail(null, msg);
    }

    public static WebServiceTaskResult fail(Throwable t) {
        return fail(t, null);
    }

    public static WebServiceTaskResult fail(Throwable t, String msg) {
        return new WebServiceTaskResult(false, msg, t);
    }

    public static WebServiceTaskResult success(String msg){
        return new WebServiceTaskResult(true, msg);
    }

    private WebServiceTaskResult(boolean result, String msg, Throwable t) {
        this.result = result;
        this.message = msg;
        this.exception = t;
    }

    public WebServiceTaskResult(T data){
        this(true);
        this.data = data;
    }

    public WebServiceTaskResult(T data, String msg){
        this(true, msg);
        this.data = data;
    }

    private WebServiceTaskResult(boolean result, String msg) {
        this(result, msg, null);
    }

    private WebServiceTaskResult(boolean result) {
        this(result, null, null);
    }

    public String getMessageForUser() {
        StringBuilder msg = new StringBuilder();
        if (message != null)
            msg.append(message);

        if (exception != null) {
            Throwable t = exception;
            do {
                msg.append(System.getProperty("line.separator")).append(t.getLocalizedMessage());
            } while((t = t.getCause()) != null);
        }

        return msg.toString();
    }

    public WebServiceTaskResult addResult(WebServiceTaskResult r) {
        boolean result = this.result;
        String msg = this.message == null ? "" : this.message;
        Throwable t = this.exception;

        if (!r.result) {
            result = false;
        }
        if (r.message != null) {
            if (msg.length() != 0)
                msg += System.getProperty("line.separator") + System.getProperty("line.separator");

            msg += r.message;
        }
        if (r.exception != null) {
            try {
                Throwable newT = (Throwable) r.clone();
                newT.initCause(t);
                t = newT;
            } catch (CloneNotSupportedException e) {
                t = r.exception;
            }
        }
        return new WebServiceTaskResult(result, msg, t);
    }
}

