package org.apache.spark.smtt.entity;

import org.apache.spark.smtt.entity.vo.*;
import org.apache.spark.smtt.jni.ProcJni;
import org.apache.spark.smtt.unsafe.UnsafeManager;
import org.apache.spark.smtt.util.ProcessUtil;
import sun.misc.Unsafe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Created by root on 17-4-4.
 */
public class Tracer {

    private static BigInteger pfnMask;

    private static BigInteger hugeTlbMask;

    private static BigInteger thpMask;

    static {
        /*
         * PFN mask.
         */
        pfnMask = BigInteger.ONE.shiftLeft(55).subtract(BigInteger.ONE);

        /*
         * Huge TLB mask.
         */
        hugeTlbMask = BigInteger.ONE.shiftLeft(17);

        /*
         * THP mask.
         */
        thpMask = BigInteger.ONE.shiftLeft(22);
    }

    /**
     * Trace storage serialized memory.
     *
     * @param accessType
     * @param blockId
     * @param buffer
     * @throws Exception
     */
    public static void traceStorageSeri(String accessType, String blockId, ByteBuffer buffer) throws Exception {
        /*
         * Get the base virtual address.
         */
        Field hbField = ByteBuffer.class.getDeclaredField("hb");
        Unsafe unsafe = UnsafeManager.getUnsafe();
        long hbOffset = unsafe.objectFieldOffset(hbField);
        long hbAddr = unsafe.getLong(buffer, hbOffset);

        long hbBaseAddr = hbAddr + Unsafe.ARRAY_BYTE_BASE_OFFSET;
        int capacity = buffer.capacity();

        /*
         * Make the address item.
         */
        mkAddrItem(accessType, blockId, hbBaseAddr, capacity, Unsafe.ARRAY_BYTE_INDEX_SCALE, AddrItemStorageSeri.class);
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
        // Get the base virtual address of this object.
        long objAddr = getObjectAddr(obj);

        Unsafe unsafe = UnsafeManager.getUnsafe();

        /*
         * Get all fields of this object and trace each.
         */
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            long fieldOffset = unsafe.objectFieldOffset(field);
            long fieldAddr = objAddr + fieldOffset;

            /*
             * The field size.
             */
            long fieldSize = 0;
            Class<?> fieldType = field.getType();
            if (byte.class == fieldType) {
                fieldSize = 1;
            } else if (boolean.class == fieldType) {
                fieldSize = 1;
            } else if (char.class == fieldType) {
                fieldSize = 2;
            } else if (short.class == fieldType) {
                fieldSize = 2;
            } else if (int.class == fieldType) {
                fieldSize = 4;
            } else if (long.class == fieldType) {
                fieldSize = 8;
            } else if (float.class == fieldType) {
                fieldSize = 4;
            } else if (double.class == fieldType) {
                fieldSize = 8;
            }

            /*
             * Make the address item.
             */
            mkAddrItem(accessType, blockId, fieldAddr, fieldSize, 1, AddrItemStorageDese.class);
        }
    }

    /**
     * Trace execution intermediate results.
     *
     * @param accessType
     * @param rddInfo
     * @param baseObj
     * @param offset
     * @param length
     * @throws Exception
     */
    public static void traceExecutionData(String accessType, String rddInfo, Object baseObj, long offset, long length) throws Exception {
        /*
         * Get the base virtual address.
         */
        long baseObjAddr = getObjectAddr(baseObj);
        long virBaseAddr = baseObjAddr + offset;

        /*
         * Make the address item.
         */
        mkAddrItem(accessType, rddInfo, virBaseAddr, length, Unsafe.ARRAY_BYTE_INDEX_SCALE, AddrItemExecutionData.class);
    }

    /**
     * Trace execution pointers of intermediate results.
     *
     * @param accessType
     * @param rddInfo
     * @param baseObj
     * @param offset
     * @param length
     * @throws Exception
     */
    public static void traceExecutionPointer(String accessType, String rddInfo, Object baseObj, long offset, long length) throws Exception {
        /*
         * Get the base virtual address.
         */
        long baseObjAddr = getObjectAddr(baseObj);
        long virBaseAddr = baseObjAddr + offset;

        /*
         * Make the address item.
         */
        mkAddrItem(accessType, rddInfo, virBaseAddr, length, Unsafe.ARRAY_BYTE_INDEX_SCALE, AddrItemExecutionPointer.class);
    }

    private static void mkAddrItem(String accessType, String rddInfo, long virBaseAddr, long length, int increment, Class<?> addrItemClass) throws Exception {
        // Time stamp.
        long timeStamp = System.currentTimeMillis();

        // Current pid.
        String pid = ProcessUtil.getCurrentPid();

        for (int i = 0; i < length; i++) {
            AddrItem addrItem = (AddrItem) addrItemClass.newInstance();

            // Time stamp.
            addrItem.setTimeStamp(timeStamp);

            // Current pid.
            addrItem.setPid(pid);

            // BlockId.
            addrItem.setBlockId(rddInfo);

            // Access type.
            addrItem.setAccessType(accessType);

            // Virtual address.
            long virAddr = virBaseAddr + i * increment;

            /*
             * Fill up addrItem with address information.
             */
            fillAddrInfo(addrItem, virAddr);

            /*
             * Write to file.
             */
            writeAddrItem(addrItem);
        }
    }

    private static long getObjectAddr(Object object) throws Exception {
        Unsafe unsafe = UnsafeManager.getUnsafe();
        Object[] objs = {object};
        long objsBaseOffset = unsafe.arrayBaseOffset(objs.getClass());
        long objAddr = unsafe.getLong(objs, objsBaseOffset);
        return objAddr;
    }

    private static void fillAddrInfo(AddrItem addrItem, long virAddr) throws Exception {

        // The virtual address.
        addrItem.setVirAddr(BigInteger.valueOf(virAddr));

        /*
         * Get the PageInfo.
         */
        PageInfo pageInfo = new PageInfo();
        ProcJni.getPageInfo(pageInfo, addrItem.getPid(), virAddr);

        // The pagemap.
        BigInteger pagemap = pageInfo.getPagemap();

        // The pageflags.
        BigInteger pageflags = pageInfo.getPageflags();

        /*
         * Page frame number.
         */
        BigInteger pfn = pagemap.and(pfnMask);
        addrItem.setPfn(pfn);

        /*
         * Physical address.
         */
        BigInteger pageSize = pageInfo.getPageSize();
        BigInteger offsetInPage = pageInfo.getOffsetInPage();
        BigInteger pAddr = pfn.multiply(pageSize).add(offsetInPage);
        addrItem.setPhyAddr(pAddr);

        /*
         * Whether present.
         */
        BigInteger present = pagemap.shiftRight(63);
        if (present.equals(BigInteger.ONE)) {
            addrItem.setPresent(true);
        } else {
            addrItem.setPresent(false);
        }

        /*
         * Whether huge tlb.
         */
        BigInteger hugeTlb = pageflags.and(hugeTlbMask).shiftRight(17);
        if (hugeTlb.equals(BigInteger.ONE)) {
            addrItem.setHugeTlb(true);
        } else {
            addrItem.setHugeTlb(false);
        }

        /*
         * Whether THP.
         */
        BigInteger thp = pageflags.and(thpMask).shiftRight(22);
        if (thp.equals(BigInteger.ONE)) {
            addrItem.setThp(true);
        } else {
            addrItem.setThp(false);
        }
    }

    private static void writeAddrItem(AddrItem addrItem) throws Exception {
        BufferedWriter writer = null;
        try {
            File outputDir = new File(addrItem.getOutputDir());
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            writer = new BufferedWriter(new FileWriter(addrItem.getOutputPath(), true));

            if (!addrItem.hasHeader()) {
                writer.write(AddrItem.header);
                writer.newLine();
                addrItem.hasHeader(true);
            }

            writer.write(addrItem.toString());
            writer.newLine();
        } catch (Exception e) {
            throw e;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
