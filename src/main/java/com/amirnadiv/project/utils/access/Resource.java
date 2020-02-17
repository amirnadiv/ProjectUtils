package com.amirnadiv.project.utils.common.access;

import com.amirnadiv.project.utils.common.io.ByteArray;

public interface Resource extends Checksum {

    long getId();

    ByteArray getBody();

    ResourceHeader getHeader();

    long getSize();

}
