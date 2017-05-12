package org.apache.spark.smtt.entity;

import org.apache.spark.smtt.unsafe.UnsafeManager;
import org.junit.Test;
import sun.misc.Unsafe;

import java.nio.ByteBuffer;

/**
 * Created by root on 17-4-20.
 */
public class TracerTest {
    @Test
    public void testTraceStorageSeri() {
        try {
            byte[] bytes = {0x01, 0x03, 0x05};
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            Tracer.traceStorageSeri("read", "rdd1_1", buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTraceStorageDese() {
        try {
            ObjTest objTest = new ObjTest();
            Tracer.traceStorageDese("read", "rdd1_1", objTest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetObjAddr() {
        try {
            byte[] obj = {0x09};

            Unsafe unsafe = UnsafeManager.getUnsafe();
            Object[] objs = {obj};
            long objsBaseOffset = unsafe.arrayBaseOffset(objs.getClass());
            long objAddr = unsafe.getLong(objs, objsBaseOffset);
            long objDataAddr = objAddr + unsafe.arrayBaseOffset(obj.getClass());

            System.out.println("Data: " + unsafe.getByte(objDataAddr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTraceExecutionData() {
        try {
            byte[] bytes = {0x01, 0x02, 0x03};
            Tracer.traceExecutionData("read", "rdd1_1", bytes, UnsafeManager.getUnsafe().ARRAY_BYTE_BASE_OFFSET, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTraceExecutionPointer() {
        try {
            byte[] bytes = {0x09, 0x008, 0x007};
            Tracer.traceExecutionPointer("read", "rdd1_1", bytes, UnsafeManager.getUnsafe().ARRAY_BYTE_BASE_OFFSET, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
