package com.amirnadiv.project.utils.common.ip;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.amirnadiv.project.utils.common.CollectionUtil;
import com.amirnadiv.project.utils.common.Emptys;
import com.amirnadiv.project.utils.common.ExceptionUtil;
import com.amirnadiv.project.utils.common.IpUtil;
import com.amirnadiv.project.utils.common.StringUtil;
import com.amirnadiv.project.utils.common.algorithms.BinarySearch;
import com.amirnadiv.project.utils.common.io.ReaderUtil;
import com.amirnadiv.project.utils.common.logger.CachedLogger;

class DefaultIpManager extends CachedLogger implements IpManager, IpLib {

    private long[] ipArray;
    private String[] ipInfoArray;
    // ip库文件,压缩后还是有3M左右，所以不包在一起，单独拷贝文件
    private boolean init;
    private int total; // 总条数
    private IpData ipData;

    public DefaultIpManager() {
        this(IpData.YAHOO);
    }

    public DefaultIpManager(IpData ipData) {
        this.ipData = ipData;
    }

    public void init() {
        if (!init) {
            loadIpLib();
        }
    }

    public void reload() {
        loadIpLib();
    }

    public void reload(IpData ipData) {
        this.setIpData(ipData);
        loadIpLib();
    }

    private List<String> loadRowData() {

        try {
            return ReaderUtil.readLinesAndClose(this.getClass().getResourceAsStream(ipData.getDataFile()));
        } catch (IOException e) {
            logger.error("loadIpLib error", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    private synchronized void loadIpLib() {
        long start = System.currentTimeMillis();

        List<String> lines = loadRowData();
        transformData(lines);

        long end = System.currentTimeMillis();
        logger.infoIfEnabled("load IP lib finished. have {}records, spend {}ms.", total, (end - start));
        init = true;
    }

    private void transformData(List<String> lines) {
        if (CollectionUtil.isEmpty(lines)) {
            throw new RuntimeException("load IP lib fail,no data found!");
        }

        ipArray = new long[ITEM_SIZE];
        ipInfoArray = new String[ITEM_SIZE];

        for (Iterator<String> iter = lines.iterator(); iter.hasNext(); total++) {
            String line = iter.next();
            String[] items = StringUtil.split(line, ipData.getSplitRegex());
            if (ipData.lineItems() != items.length) {
                iter.remove();
            }

            ipArray[total] = IpUtil.encodeIp(items[0].trim());
            ipInfoArray[total] = StringUtil.substringAfter(line, ipData.getSplit()).trim();

        }
    }

    private IpEntry getIpLocation(long ip) {
        // FIXME Arrays.binarySearch居然出错
        // int pos = Arrays.binarySearch(ipArray, ip);
        int pos = BinarySearch.rank(ip, ipArray);
        String info = ipInfoArray[pos];

        return convert(info, ip);
    }

    private IpEntry convert(String info, long ip) {
        String[] items = StringUtil.split(info, ipData.getSplitRegex());
        long ipLong = IpUtil.encodeIp(items[0].trim());

        if (ip <= ipLong) {
            return ipData.create(items);
        }

        return null;
    }

    public IpEntry getIpInfo(String ip) {
        long ipLong = IpUtil.encodeIp(ip);
        if (ipLong > 0) {
            return getIpLocation(ipLong);
        }

        return null;
    }

    public String getIpLocation(String ip) {
        IpEntry entry = getIpInfo(ip);

        if (entry != null) {
            return entry.getAddress();
        }

        return Emptys.EMPTY_STRING;
    }

    public boolean isInit() {
        return init;
    }

    public void setIpData(IpData ipData) {
        this.ipData = ipData;
    }
}
