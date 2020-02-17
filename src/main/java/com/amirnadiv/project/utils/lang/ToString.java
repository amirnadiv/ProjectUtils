package com.amirnadiv.project.utils.common.lang;

import java.io.Serializable;

import com.amirnadiv.project.utils.common.able.ToStringable;
import com.amirnadiv.project.utils.common.apache.ToStringBuilder;
import com.amirnadiv.project.utils.common.apache.ToStringStyle;

public class ToString implements ToStringable, Serializable {

    private static final long serialVersionUID = -7273706793161702222L;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
