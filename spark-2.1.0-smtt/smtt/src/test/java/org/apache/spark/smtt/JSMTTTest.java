package org.apache.spark.smtt;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by root on 17-4-3.
 */
public class JSMTTTest {
    @Test
    public void testTraceStorageSeri() {
        byte[] bytes = {0x01};
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            JSMTT.traceStorageSeri("read", "rdd0_1", buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
