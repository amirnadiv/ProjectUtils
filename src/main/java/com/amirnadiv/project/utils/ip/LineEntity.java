package com.amirnadiv.project.utils.common.ip;

import com.amirnadiv.project.utils.common.apache.ToStringBuilder;
import com.amirnadiv.project.utils.common.apache.ToStringStyle;

public class LineEntity {

    private String line;
    private boolean isFinished;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public byte[] getBytes() {
        return (line + "\n").getBytes();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
