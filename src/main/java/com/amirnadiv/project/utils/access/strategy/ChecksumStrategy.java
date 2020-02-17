package com.amirnadiv.project.utils.common.access.strategy;

import com.amirnadiv.project.utils.common.access.Checksum;
import com.amirnadiv.project.utils.common.access.AccessStrategy;

public class ChecksumStrategy implements AccessStrategy {

    private Checksum acs;

    public ChecksumStrategy(Checksum acs) {
        this.acs = acs;
    }

    @Override
    public String find(long id) {
        return id + "_" + acs.checksum();
    }

}
