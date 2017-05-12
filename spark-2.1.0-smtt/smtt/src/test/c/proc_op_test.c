#include "proc/proc_op.c"

// cd /root/working/spark/spark-2.1.0/smtt/src/test/c
// gcc proc_op_test.c -I /root/working/spark/spark-2.1.0/smtt/src/main/c

#define PAGEMAP_ITEM_PFN_MASK (((uint64_t)1)<<55)-1

int main() {
    printf("Start to test.\n");

    char* pid_c = "1";
    unsigned long vaddr = 0x7f0c73f16000;
    unsigned long pfn = 0;

    printf("pid=%s, vaddr=%lx \n", pid_c, vaddr);

    printf("Test read_pagemap...\n");
    struct ret_rpagemap_struct rpagemap_ret = read_pagemap(pid_c, vaddr);
    printf("pagemap_item = %lx \n", rpagemap_ret.pagemap_item);
    printf("pagesize = %lx \n", rpagemap_ret.pagesize);
    printf("offset_in_page = %lx \n", rpagemap_ret.offset_in_page);
    printf("Test read_pagemap...Done.\n");

    printf("Test read_kpageflags...\n");
    pfn = rpagemap_ret.pagemap_item & PAGEMAP_ITEM_PFN_MASK;
    struct ret_rkpageflags_struct rkpageflags_ret = read_kpageflags(pfn);
    printf("kpageflags_item = %lx \n", rkpageflags_ret.kpageflags_item);
    printf("Test read_kpageflags...Done.\n");

    printf("End to test.\n");

    return 0;
}
