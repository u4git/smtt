package org.apache.spark.smtt.jni;

import org.apache.spark.smtt.entity.vo.PageInfo;
import org.apache.spark.smtt.unsafe.UnsafeManager;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.management.ManagementFactory;

/**
 * Created by root on 17-3-26.
 */
public class ProcJniTest {

    @Test
    public void testGetPageInfo() {
        try {
            Unsafe unsafe = UnsafeManager.getUnsafe();
            long vaddr = unsafe.allocateMemory(1);
            unsafe.putChar(vaddr, 'A');
            char c = unsafe.getChar(vaddr);
            System.out.println(c);

            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.substring(0, name.indexOf("@"));

            PageInfo pageInfo = new PageInfo();
            ProcJni.getPageInfo(pageInfo, pid, vaddr);

            System.out.println("pagemap: " + pageInfo.getPagemap().toString(16));
            System.out.println("pagesize: " + pageInfo.getPageSize().toString(16));
            System.out.println("offsetInPage: " + pageInfo.getOffsetInPage().toString(16));
            System.out.println("pageflags: " + pageInfo.getPageflags().toString(16));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetPageInfo_Self_Pid() {
        try {
            Unsafe unsafe = UnsafeManager.getUnsafe();
            long vaddr = unsafe.allocateMemory(1);
            unsafe.putChar(vaddr, 'A');
            char c = unsafe.getChar(vaddr);
            System.out.println(c);

            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.substring(0, name.indexOf("@"));

            PageInfo pageInfo_pid = new PageInfo();
            ProcJni.getPageInfo(pageInfo_pid, pid, vaddr);

            PageInfo pageInfo_self = new PageInfo();
            ProcJni.getPageInfo(pageInfo_self, "self", vaddr);

            System.out.println("pagemap_pid: " + pageInfo_pid.getPagemap().toString(16));
            System.out.println("pagemap_self: " + pageInfo_self.getPagemap().toString(16));

            System.out.println("pagesize_pid: " + pageInfo_pid.getPageSize().toString(16));
            System.out.println("pagesize_self: " + pageInfo_self.getPageSize().toString(16));

            System.out.println("offsetInPage_pid: " + pageInfo_pid.getOffsetInPage().toString(16));
            System.out.println("offsetInPage_self: " + pageInfo_self.getOffsetInPage().toString(16));

            System.out.println("pageflags_pid: " + pageInfo_pid.getPageflags().toString(16));
            System.out.println("pageflags_self: " + pageInfo_self.getPageflags().toString(16));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
