package com.amirnadiv.project.utils.common;

import java.util.Random;

public abstract class RandomUtil {

    private final static Random random = new Random();

    public static int next(int begin, int end) {
        if (end <= begin) {
            throw new IllegalArgumentException("end must larger than begin");
        }

        int minus = random.nextInt(end - begin + 1);
        return (begin + minus);
    }

    public static long next(long begin, long end) {
        if (end <= begin) {
            throw new IllegalArgumentException("end must larger than begin");
        }

        long minus = random.nextInt((int) (end - begin + 1));
        return (begin + minus);
    }

    public static double getRandomNum(double pSngBegin, double pSngEnd) {
        if (pSngEnd <= pSngBegin) {
            throw new IllegalArgumentException("pSngEnd must larger than pSngBegin");
        }

        return (pSngEnd - pSngBegin) * Math.random() + pSngBegin;
    }

    public static double getRndNumP(double pSngBegin, double pSngEnd, double pSngPB, double pSngPE, double pBytP) {
        double sngPLen = pSngPE - pSngPB;
        // total length
        double sngTLen = pSngEnd - pSngBegin;
        // FIXME may throw java.lang.ArithmeticException : / by zero
        if ((sngPLen / sngTLen) * 100 == pBytP) {
            return getRandomNum(pSngBegin, pSngEnd);
        }

        // ((sngPLen + sngIncreased) / (sngTLen + sngIncreased)) * 100 =
        // bytP
        double sngIncreased = ((pBytP / 100) * sngTLen - sngPLen) / (1 - (pBytP / 100));
        // 缩放回原来区间
        double sngResult = getRandomNum(pSngBegin, pSngEnd + sngIncreased);
        if (pSngBegin <= sngResult && sngResult <= pSngPB) {
            return sngResult;
        }

        if (pSngPB <= sngResult && sngResult <= (pSngPE + sngIncreased)) {
            return pSngPB + (sngResult - pSngPB) * sngPLen / (sngPLen + sngIncreased);
        }

        if ((pSngPE + sngIncreased) <= sngResult && sngResult <= (pSngEnd + sngIncreased)) {
            return sngResult - sngIncreased;
        }

        return 0d;
    }

}
