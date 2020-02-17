
package com.amirnadiv.project.utils.common.access.fs;

import java.io.File;

import com.amirnadiv.project.utils.common.access.AccessStrategy;

public class LocalFSAccess extends FileSystemAccess {

    private String storageHome;

    public LocalFSAccess(AccessStrategy strategy) {
        super(strategy);
    }

    public LocalFSAccess(AccessStrategy strategy, String storageHome) {
        this(strategy);
        this.storageHome = storageHome;
    }

    @Override
    protected String physicalLocation(String logicalLocation) {
        return (storageHome == null) ? logicalLocation : storageHome + File.separator + logicalLocation;
    }

    public void setStorageHome(String storageHome) {
        this.storageHome = storageHome;
    }

    @Override
    public String name() {
        return "Local File System";
    }

}
