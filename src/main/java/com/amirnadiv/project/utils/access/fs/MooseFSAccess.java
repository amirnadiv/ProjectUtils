
package com.amirnadiv.project.utils.common.access.fs;

import com.amirnadiv.project.utils.common.access.Access;
import com.amirnadiv.project.utils.common.access.AccessDecorator;

public class MooseFSAccess extends AccessDecorator {

    public MooseFSAccess(Access access) {
        super(access, "MooseFS");
    }

}
