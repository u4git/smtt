package org.apache.spark.smtt.util;

import java.lang.management.ManagementFactory;

/**
 * Created by root on 17-4-5.
 */
public class ProcessUtil {
    public static String getCurrentPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.substring(0, name.indexOf("@"));
        return pid;
    }
}
