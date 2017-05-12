package org.apache.spark.smtt;

import org.apache.spark.smtt.entity.Tracer;

import java.nio.ByteBuffer;
import java.util.Hashtable;

/**
 * Created by root on 17-3-24.
 */
public class JSMTT {

    private static Hashtable<Integer, String> writer2RddInfo = new Hashtable<>();

    private static Hashtable<Integer, Integer> sorter2Writer = new Hashtable<>();

    private static Hashtable<Integer, Integer> inMemSorter2Sorter = new Hashtable<>();

    private static Hashtable<Integer, Integer> array2InMemSorter = new Hashtable<>();

    /**
     * Trace storage serialized memory.
     *
     * @param accessType
     * @param blockId
     * @param buffer
     * @throws Exception
     */
    public static void traceStorageSeri(String accessType, String blockId, ByteBuffer buffer) throws Exception {
        Tracer.traceStorageSeri(accessType, blockId, buffer);
    }

    /**
     * Trace storage deserialized memory.
     *
     * @param accessType
     * @param blockId
     * @param obj
     * @throws Exception
     */
    public static void traceStorageDese(String accessType, String blockId, Object obj) throws Exception {
        Tracer.traceStorageDese(accessType, blockId, obj);
    }

    public static void registerWriter2RddInfo(int writerHashcode, String rddInfo) {
        synchronized (writer2RddInfo) {
            writer2RddInfo.put(writerHashcode, rddInfo);
        }
    }

    public static void removeWriter2RddInfo(int writerHashcode) {
        synchronized (writer2RddInfo) {
            writer2RddInfo.remove(writerHashcode);
        }
    }

    public static void registerSorter2Writer(int sorterHashcode, int writerHashcode) {
        synchronized (sorter2Writer) {
            sorter2Writer.put(sorterHashcode, writerHashcode);
        }
    }

    public static void removeSorter2Writer(int sorterHashcode) {
        synchronized (sorter2Writer) {
            sorter2Writer.remove(sorterHashcode);
        }
    }

    /**
     * Trace execution intermediate results.
     *
     * @param accessType
     * @param sorterHashcode
     * @param baseObj
     * @param offset
     * @param length
     * @throws Exception
     */
    public static void traceExecutionData(String accessType, int sorterHashcode, Object baseObj, long offset, long length) throws Exception {
        // Map sorter -> writer.
        int writerHashcode = -1;
        synchronized (sorter2Writer) {
            writerHashcode = sorter2Writer.get(sorterHashcode);
        }

        // Map writer -> rddInfo
        String rddInfo = "NONE";
        synchronized (writer2RddInfo) {
            rddInfo = writer2RddInfo.get(writerHashcode);
        }

        // Trace address.
        Tracer.traceExecutionData(accessType, rddInfo, baseObj, offset, length);
    }

    public static void registerInMemSorter2Sorter(int inMemSorterHashcode, int sorterHashcode) {
        synchronized (inMemSorter2Sorter) {
            inMemSorter2Sorter.put(inMemSorterHashcode, sorterHashcode);
        }
    }

    public static void removeInMemSorter2Sorter(int inMemSorterHashcode) {
        synchronized (inMemSorter2Sorter) {
            inMemSorter2Sorter.remove(inMemSorterHashcode);
        }
    }

    public static void registerArray2InMemSorter(int arrayHashcode, int inMemSorterHashcode) {
        synchronized (array2InMemSorter) {
            array2InMemSorter.put(arrayHashcode, inMemSorterHashcode);
        }
    }

    public static void removeArray2InMemSorter(int arrayHashcode) {
        synchronized (array2InMemSorter) {
            array2InMemSorter.remove(arrayHashcode);
        }
    }

    /**
     * Trace execution pointers of intermediate results.
     *
     * @param accessType
     * @param arrayHashcode
     * @param baseObj
     * @param offset
     * @param length
     * @throws Exception
     */
    public static void traceExecutionPointer(String accessType, int arrayHashcode, Object baseObj, Long offset, Long length) throws Exception {
        // Map array -> inMemSorter.
        int inMemSorterHashcode = -1;
        synchronized (array2InMemSorter) {
            inMemSorterHashcode = array2InMemSorter.get(arrayHashcode);
        }

        // Map inMemSorter -> sorter.
        int sorterHashcode = -1;
        synchronized (inMemSorter2Sorter) {
            sorterHashcode = inMemSorter2Sorter.get(inMemSorterHashcode);
        }

        // Map sorter -> writer.
        int writerHashcode = -1;
        synchronized (sorter2Writer) {
            writerHashcode = sorter2Writer.get(sorterHashcode);
        }

        // Map writer -> rddInfo
        String rddInfo = "NONE";
        synchronized (writer2RddInfo) {
            rddInfo = writer2RddInfo.get(writerHashcode);
        }

        // Trace address.
        Tracer.traceExecutionPointer(accessType, rddInfo, baseObj, offset, length);
    }
}
