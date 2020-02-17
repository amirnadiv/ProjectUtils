
package com.amirnadiv.project.utils.common.able;

public interface Processor<P, R> {

    R processAndGet(P processing);

}
