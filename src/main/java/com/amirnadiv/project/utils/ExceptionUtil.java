package com.amirnadiv.project.utils.common;

import static com.amirnadiv.project.utils.common.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class ExceptionUtil {

    public static boolean causedBy(Throwable throwable, Class<? extends Throwable> causeType) {
        assertNotNull(causeType, "causeType");

        Set<Throwable> causes = CollectionUtil.createHashSet();

        for (; throwable != null && !causeType.isInstance(throwable) && !causes.contains(throwable); throwable =
                throwable.getCause()) {
            causes.add(throwable);
        }

        return throwable != null && causeType.isInstance(throwable);
    }

    public static Throwable getRootCause(Throwable throwable) {
        List<Throwable> causes = getCauses(throwable, true);

        return causes.isEmpty() ? null : causes.get(0);
    }

    public static List<Throwable> getCauses(Throwable throwable) {
        return getCauses(throwable, false);
    }

    public static List<Throwable> getCauses(Throwable throwable, boolean reversed) {
        LinkedList<Throwable> causes = CollectionUtil.createLinkedList();

        for (; throwable != null && !causes.contains(throwable); throwable = throwable.getCause()) {
            if (reversed) {
                causes.addFirst(throwable);
            } else {
                causes.addLast(throwable);
            }
        }

        return causes;
    }

    public static RuntimeException toRuntimeException(Exception exception) {
        return toRuntimeException(exception, null);
    }

    public static RuntimeException toRuntimeException(Exception exception,
            Class<? extends RuntimeException> runtimeExceptionClass) {
        if (exception == null) {
            return null;
        }

        if (exception instanceof RuntimeException) {
            return (RuntimeException) exception;
        }
        if (runtimeExceptionClass == null) {
            return new RuntimeException(exception);
        }

        RuntimeException runtimeException;

        try {
            runtimeException = runtimeExceptionClass.newInstance();
        } catch (Exception ee) {
            return new RuntimeException(exception);
        }

        runtimeException.initCause(exception);
        return runtimeException;

    }

    public static void throwExceptionOrError(Throwable throwable) throws Exception {
        if (throwable instanceof Exception) {
            throw (Exception) throwable;
        }

        if (throwable instanceof Error) {
            throw (Error) throwable;
        }

        throw new RuntimeException(throwable); // unreachable code
    }

    public static void throwRuntimeExceptionOrError(Throwable throwable) {
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }

        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }

        throw new RuntimeException(throwable);
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter buffer = new StringWriter();
        PrintWriter out = new PrintWriter(buffer);

        throwable.printStackTrace(out);
        out.flush();

        return buffer.toString();
    }

}
