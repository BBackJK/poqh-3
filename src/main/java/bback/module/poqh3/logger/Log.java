package bback.module.poqh3.logger;

public interface Log {
    boolean isDebugEnabled();
    boolean isTraceEnabled();
    void error(String s, Throwable e);
    void error(String s);
    void debug(String s);
    void info(String s);
    void trace(String s);
    void warn(String s);
}
