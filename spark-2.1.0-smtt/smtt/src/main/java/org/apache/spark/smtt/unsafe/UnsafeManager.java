package org.apache.spark.smtt.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by root on 17-3-27.
 */
public class UnsafeManager {
    private static Unsafe unsafe;

    public static Unsafe getUnsafe() {
        if(unsafe==null) {
            try {
                Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                unsafe = (Unsafe) unsafeField.get(null);
            }catch(Exception e){
                unsafe = null;
            }
        }
        return unsafe;
    }
}
