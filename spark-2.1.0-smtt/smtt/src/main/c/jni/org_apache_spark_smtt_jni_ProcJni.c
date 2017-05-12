#include "org_apache_spark_smtt_jni_ProcJni.h"
#include <stdio.h>

#ifndef PROC_OP
    #define PROC_OP
    #include "proc/proc_op.c"
#endif

#define PAGEMAP_ITEM_PFN_MASK (((uint64_t)1)<<55)-1

#define SUCCESS 0
#define FAILURE -1

union pagemap_item_union {
        uint64_t pagemap_item;
        char bytes[PAGEMAP_ITEM_SIZE];
};

union kpageflags_item_union {
        uint64_t kpageflags_item;
        char bytes[KPAGEFLAGS_ITEM_SIZE];
};

JNIEXPORT jint JNICALL Java_org_apache_spark_smtt_jni_ProcJni_getPageInfo
  (JNIEnv * env, jclass clazz, jobject object, jstring pid, jlong vaddr)
{
    struct ret_rpagemap_struct rpagemap_ret;
    struct ret_rkpageflags_struct rkpageflags_ret;

    union pagemap_item_union pagemap_item_bytes;
    union kpageflags_item_union kpageflags_item_bytes;

    int i;

    unsigned long pfn;

    /*
     * Read the pagemap.
     */
    const char *pid_c = (*env)->GetStringUTFChars(env, pid, 0);

    rpagemap_ret = read_pagemap(pid_c, vaddr);
    (*env)->ReleaseStringUTFChars(env, pid, pid_c);

    /*
     * Read the kpageflags.
     */
    pfn = rpagemap_ret.pagemap_item & PAGEMAP_ITEM_PFN_MASK;
    rkpageflags_ret = read_kpageflags(pfn);

    /*
     * Split pagemap_item and kpageflags_item into bytes.
     */
    pagemap_item_bytes.pagemap_item = rpagemap_ret.pagemap_item;
    kpageflags_item_bytes.kpageflags_item = rkpageflags_ret.kpageflags_item;

    // Get the class of object.
    jclass object_class = (*env)->GetObjectClass(env, object);

    /*
     * Set pagemap_item into object.
     */
    jmethodID setPagemapBytesID = (*env)->GetMethodID(env, object_class, "setPagemapBytes", "(IB)V");

    if(setPagemapBytesID==NULL) {
        printf("Cannot get the method setPagemapBytes.\n");
        return FAILURE;
    }

    for(i=0; i<PAGEMAP_ITEM_SIZE; i++) {
        (*env)->CallVoidMethod(env, object, setPagemapBytesID, PAGEMAP_ITEM_SIZE-1-i, pagemap_item_bytes.bytes[i]);
    }

    jmethodID mkPagemapID = (*env)->GetMethodID(env, object_class, "mkPagemap", "()V");
    if(mkPagemapID==NULL) {
        printf("Cannot get the method mkPagemap.\n");
        return FAILURE;
    }
    (*env)->CallVoidMethod(env, object, mkPagemapID);

    /*
     * Set offset in page.
     */
    jmethodID setOffsetInPageID = (*env)->GetMethodID(env, object_class, "setOffsetInPage", "(J)V");
    if(setOffsetInPageID==NULL) {
        printf("Cannot get the method setOffsetInPage.\n");
        return FAILURE;
    }
    (*env)->CallVoidMethod(env, object, setOffsetInPageID, rpagemap_ret.offset_in_page);

    /*
     * Set pagesize.
     */
     jmethodID setPageSizeID = (*env)->GetMethodID(env, object_class, "setPageSize", "(J)V");
    if(setPageSizeID==NULL) {
        printf("Cannot get the method setPageSize.\n");
        return FAILURE;
    }
    (*env)->CallVoidMethod(env, object, setPageSizeID, rpagemap_ret.pagesize);


    /*
     * Set kpageflags_item into object.
     */
    jmethodID setPageflagsBytesID = (*env)->GetMethodID(env, object_class, "setPageflagsBytes", "(IB)V");

    if(setPageflagsBytesID==NULL) {
        printf("Cannot get the method setPageflagsBytes.\n");
        return FAILURE;
    }

    for(i=0; i<KPAGEFLAGS_ITEM_SIZE; i++) {
        (*env)->CallVoidMethod(env, object, setPageflagsBytesID, KPAGEFLAGS_ITEM_SIZE-1-i, kpageflags_item_bytes.bytes[i]);
    }

    jmethodID mkPageflagsID = (*env)->GetMethodID(env, object_class, "mkPageflags", "()V");
    if(mkPageflagsID==NULL) {
        printf("Cannot get the method mkPageflags.\n");
        return FAILURE;
    }
    (*env)->CallVoidMethod(env, object, mkPageflagsID);

    return SUCCESS;
}
