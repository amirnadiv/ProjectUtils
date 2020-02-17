package com.amirnadiv.project.utils.common.ip;

public interface IpManager {

    IpEntry getIpInfo(String ip);

    String getIpLocation(String ip);

}
