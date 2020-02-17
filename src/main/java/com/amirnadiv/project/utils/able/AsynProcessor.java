
package com.amirnadiv.project.utils.common.able;


public interface AsynProcessor<P, R> {

    void process(P processing);

    R get();

}
