package org.apache.spark.smtt.config;

import java.io.File;

/**
 * Created by root on 17-4-4.
 */
public class GlobalConfig {
    public static String outputColSep = "\t";

    public static String outputBaseDir = "/root/smttresult";

    public static String storageSeriOutputDir = outputBaseDir;
    public static String storageSeriOutputPath = storageSeriOutputDir + File.separatorChar + "storage_seri";

    public static String storageDeseOutputDir = outputBaseDir;
    public static String storageDeseOutputPath = storageDeseOutputDir + File.separatorChar + "storage_dese";

    public static String executionDataOutputDir = outputBaseDir;
    public static String executionDataOutputPath = executionDataOutputDir + File.separatorChar + "execution_data";

    public static String executionPointerOutputDir = outputBaseDir;
    public static String executionPointerOutputPath = executionPointerOutputDir + File.separatorChar + "execution_pointer";
}
