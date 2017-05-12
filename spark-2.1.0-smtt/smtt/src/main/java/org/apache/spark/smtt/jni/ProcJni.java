package org.apache.spark.smtt.jni;

import org.apache.spark.smtt.entity.vo.PageInfo;

/**
 * Created by root on 17-2-24.
 * <p>
 * cd /root/working/spark/spark-2.1.0-smtt/smtt/src/main/java
 * javac org/apache/spark/smtt/jni/ProcJni.java
 * javah org.apache.spark.smtt.jni.ProcJni
 */
public class ProcJni {

    static {
        // cd /root/working/spark/spark-2.1.0-smtt/smtt/src/main/c
        // gcc ./jni/org_apache_spark_smtt_jni_ProcJni.c -I /usr/local/bin/jdk1.8.0_121/include -I /usr/local/bin/jdk1.8.0_121/include/linux -I /root/working/spark/spark-2.1.0-smtt/smtt/src/main/c -fPIC -shared -o ../../../lib/libprocjni.so
        System.loadLibrary("procjni");
    }

    public static native int getPageInfo(PageInfo pageInfo, String pid, long vAddr);
}
