package com.amirnadiv.project.utils.common.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.amirnadiv.project.utils.common.bo.BaseObject;

public class ThreadContext {

    private final static ThreadLocal<Map<String, Object>> CTX_HOLDER = new ThreadLocal<Map<String, Object>>();

    static {
        CTX_HOLDER.set(new HashMap<String, Object>());
    }

    public final static void putContext(String key, Object value) {
        Map<String, Object> ctx = CTX_HOLDER.get();
        if (ctx == null) {
            // FIXME 需要注意释放，或者用软引用之类的替代，timewait或回收队列均可考虑
            ctx = new HashMap<String, Object>();
            CTX_HOLDER.set(ctx);
        }
        ctx.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public final static <T extends Object> T getContext(String key) {
        Map<String, Object> ctx = CTX_HOLDER.get();
        if (ctx == null) {
            return null;
        }
        return (T) ctx.get(key);
    }

    public final static Map<String, Object> getContext() {
        Map<String, Object> ctx = CTX_HOLDER.get();
        if (ctx == null) {
            return null;
        }
        return ctx;
    }

    public final static void remove(String key) {
        Map<String, Object> ctx = CTX_HOLDER.get();
        if (ctx != null) {
            ctx.remove(key);
        }
    }

    public final static boolean contains(String key) {
        Map<String, Object> ctx = CTX_HOLDER.get();
        if (ctx != null) {
            return ctx.containsKey(key);
        }
        return false;
    }

    public final static void clean() {
        CTX_HOLDER.set(null);
    }

    public final static void init() {
        CTX_HOLDER.set(new HashMap<String, Object>());
    }

    @SuppressWarnings("unchecked")
    public final static <K extends Serializable> K getShardKey() {
        return (K) getContext(SHARD_KEY);
    }

    public final static <K extends Serializable> void putShardKey(K shardKey) {
        putContext(SHARD_KEY, shardKey);
    }

    @SuppressWarnings("unchecked")
    public final static <U extends BaseObject<?>> U getSessionVisitor() {
        return (U) getContext(VISITOR_KEY);
    }

    public final static <K extends Serializable, U extends BaseObject<K>> void putSessionVisitor(U sessionVisitor) {
        putContext(VISITOR_KEY, sessionVisitor);
    }

    public final static void putThreadLog(Integer logLevel) {
        putContext(THREAD_LOG_KEY, logLevel);
    }

    public final static Integer getThreadLog() {
        return getContext(THREAD_LOG_KEY);
    }

    private final static String SHARD_KEY = "shardKey";

    private final static String VISITOR_KEY = "sessionVisitor";

    private final static String THREAD_LOG_KEY = "threadLog";
}
