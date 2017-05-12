package org.apache.spark.smtt.entity.vo;

import org.apache.spark.smtt.config.GlobalConfig;

import java.math.BigInteger;

/**
 * Created by root on 17-4-3.
 */
public abstract class AddrItem {
    private long timeStamp;
    private String pid;
    private String accessType;
    private String blockId;
    private BigInteger virAddr;
    private BigInteger phyAddr;
    private BigInteger pfn;
    private boolean present;
    private boolean hugeTlb;
    private boolean thp;

    public static String header = "TimeStamp" + GlobalConfig.outputColSep + "Pid" + GlobalConfig.outputColSep + "AccessType" + GlobalConfig.outputColSep + "BlockId" + GlobalConfig.outputColSep + "VirAddr" + GlobalConfig.outputColSep + "PhyAddr" + GlobalConfig.outputColSep + "PFN" + GlobalConfig.outputColSep + "Present" + GlobalConfig.outputColSep + "HugeTlb" + GlobalConfig.outputColSep + "THP";

    public String toString() {
        String addrItem = this.timeStamp + GlobalConfig.outputColSep;
        addrItem = addrItem + this.pid + GlobalConfig.outputColSep;
        addrItem = addrItem + this.accessType + GlobalConfig.outputColSep;
        addrItem = addrItem + this.blockId + GlobalConfig.outputColSep;
        addrItem = addrItem + this.virAddr.toString(16) + GlobalConfig.outputColSep;
        addrItem = addrItem + this.phyAddr.toString(16) + GlobalConfig.outputColSep;
        addrItem = addrItem + this.pfn.toString(16) + GlobalConfig.outputColSep;
        addrItem = addrItem + this.present + GlobalConfig.outputColSep;
        addrItem = addrItem + this.hugeTlb + GlobalConfig.outputColSep;
        addrItem = addrItem + this.thp;

        return addrItem;
    }

    public abstract boolean hasHeader();

    public abstract void hasHeader(boolean hasHeader);

    public abstract String getOutputDir();

    public abstract String getOutputPath();

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public BigInteger getVirAddr() {
        return virAddr;
    }

    public void setVirAddr(BigInteger virAddr) {
        this.virAddr = virAddr;
    }

    public BigInteger getPhyAddr() {
        return phyAddr;
    }

    public void setPhyAddr(BigInteger phyAddr) {
        this.phyAddr = phyAddr;
    }

    public BigInteger getPfn() {
        return pfn;
    }

    public void setPfn(BigInteger pfn) {
        this.pfn = pfn;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isHugeTlb() {
        return hugeTlb;
    }

    public void setHugeTlb(boolean hugeTlb) {
        this.hugeTlb = hugeTlb;
    }

    public boolean isThp() {
        return thp;
    }

    public void setThp(boolean thp) {
        this.thp = thp;
    }
}
