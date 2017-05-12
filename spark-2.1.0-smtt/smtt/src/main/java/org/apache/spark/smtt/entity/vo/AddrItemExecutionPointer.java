package org.apache.spark.smtt.entity.vo;

import org.apache.spark.smtt.config.GlobalConfig;

/**
 * Created by root on 17-4-19.
 */
public class AddrItemExecutionPointer extends AddrItem {

    private static boolean hasHeader = false;

    @Override
    public boolean hasHeader() {
        return hasHeader;
    }

    @Override
    public void hasHeader(boolean hasHeader) {
        AddrItemExecutionPointer.hasHeader = hasHeader;
    }

    @Override
    public String getOutputDir() {
        return GlobalConfig.executionPointerOutputDir;
    }

    @Override
    public String getOutputPath() {
        return GlobalConfig.executionPointerOutputPath;
    }
}
