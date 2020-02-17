package com.amirnadiv.project.utils.common.ip;

public interface IpManagerFactory {

    IpManager getDefaultIpManager();

    IpManager getDefaultIpManager(boolean asyn);

    IpManager getIpManager(IpData ipdata);

    IpManager getIpManager(IpData ipdata, boolean asyn);

}
