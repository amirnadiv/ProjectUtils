package com.amirnadiv.project.utils.common;

import static com.amirnadiv.project.utils.common.StringPool.Symbol.DOT;

public abstract class IpUtil {

    public static long encodeIp(String ip) {
        long ret = 0;
        if (ip == null) {
            return ret;
        }
        String[] segs = ip.split("\\.");

        for (int i = 0; i < segs.length; i++) {
            long seg = Long.parseLong(segs[i]);
            ret += (seg << ((3 - i) * 8));
        }

        return ret;
    }

    public static String decodeIp(long ipLong) {
        StringBuilder ip = new StringBuilder(String.valueOf(ipLong >> 24) + DOT);

        ip.append(String.valueOf((ipLong & 16711680) >> 16) + DOT);
        ip.append(String.valueOf((ipLong & 65280) >> 8) + DOT);
        ip.append(String.valueOf(ipLong & 255));

        return ip.toString();
    }

}
