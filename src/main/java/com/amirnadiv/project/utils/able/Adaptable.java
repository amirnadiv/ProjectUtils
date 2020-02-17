
package com.amirnadiv.project.utils.common.able;


public interface Adaptable<O, N> {

    N forNew(O old);

}
