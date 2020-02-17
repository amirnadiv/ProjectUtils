package com.amirnadiv.project.utils.common.file;

import com.amirnadiv.project.utils.common.Assert;
import com.amirnadiv.project.utils.common.EnumUtil;

public abstract class ProcessFactory {

    public static FileProcessor create(FileType type) {
        return type.createProcessor();
    }

    public static FileProcessor create(String ext) {
        FileType type = EnumUtil.parseName(FileType.class, ext.toUpperCase());
        Assert.assertNotNull(type, "ext is illegal");

        return type.createProcessor();
    }

}
