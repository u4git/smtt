package org.apache.spark.smtt.entity.vo;

import java.math.BigInteger;

/**
 * Created by root on 17-2-24.
 */
public class PageInfo {
    private byte[] pagemapBytes = new byte[8];
    private byte[] pageflagsBytes = new byte[8];

    private BigInteger pagemap;
    private BigInteger pageflags;

    private BigInteger pageSize;

    private BigInteger offsetInPage;

    public void setPagemapBytes(int index, byte value) {
        this.pagemapBytes[index] = value;
    }

    public void setPageflagsBytes(int index, byte value) {
        this.pageflagsBytes[index] = value;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = new BigInteger(Long.valueOf(pageSize).toString());
    }

    public void setOffsetInPage(long offset) {
        this.offsetInPage = new BigInteger(Long.valueOf(offset).toString());
    }

    public byte[] getPagemapBytes() {
        return this.pagemapBytes.clone();
    }

    public byte[] getPageflagsBytes() {
        return this.pageflagsBytes.clone();
    }

    public void mkPagemap() {
        this.pagemap = new BigInteger(1, this.pagemapBytes);
    }

    public void mkPageflags() {
        this.pageflags = new BigInteger(1, this.pageflagsBytes);
    }

    public BigInteger getPagemap() {
        return this.pagemap;
    }

    public BigInteger getPageflags() {
        return this.pageflags;
    }

    public BigInteger getPageSize() {
        return this.pageSize;
    }

    public BigInteger getOffsetInPage() {
        return this.offsetInPage;
    }
}
