#include <stdio.h>
#include <stdint.h>
#include <unistd.h>
#include <string.h>

#define PAGEMAP_PATH_PREFIX "/proc/"
#define PAGEMAP_PATH_POSTFIX "/pagemap"
#define PAGEMAP_PATH_SIZE 50
#define PAGEMAP_ITEM_SIZE sizeof(uint64_t)

#define KPAGEFLAGS_PATH "/proc/kpageflags"
#define KPAGEFLAGS_ITEM_SIZE sizeof(uint64_t)

struct ret_rpagemap_struct {
    uint64_t pagemap_item;
    unsigned long pagesize;
    unsigned long offset_in_page;
};

struct ret_rkpageflags_struct {
    uint64_t kpageflags_item;
};

struct ret_rpagemap_struct read_pagemap(const char* pid, unsigned long vaddr) {

    struct ret_rpagemap_struct result;

    char pagemap_path[PAGEMAP_PATH_SIZE] = PAGEMAP_PATH_PREFIX;
    FILE* pagemap_file;

    result.pagesize = getpagesize();
    unsigned long vpfn = vaddr / result.pagesize;
    result.offset_in_page = vaddr % result.pagesize;

    /*
     * Open the file.
     */
    strcat(pagemap_path, pid);
    strcat(pagemap_path, PAGEMAP_PATH_POSTFIX);

    pagemap_file = fopen(pagemap_path, "rb");
    if(pagemap_file != NULL) {
        /*
         * Find the item.
         */
        unsigned long offset = vpfn * PAGEMAP_ITEM_SIZE;
        if(fseek(pagemap_file, offset, SEEK_SET) == 0) {
            /*
             * Read the item.
             */
            if(fread(&(result.pagemap_item), 1, PAGEMAP_ITEM_SIZE, pagemap_file) != PAGEMAP_ITEM_SIZE) {
                printf("Cannot read the item by offset %lx in pagemap.\n", offset);
            }
        }else {
            printf("Cannot find the item by offset %lx in pagemap.\n", offset);
        }
    }else {
        printf("Cannot open file %s .\n", pagemap_path);
    }

    /*
     * Close the file.
     */
    fclose(pagemap_file);

    return result;
}

struct ret_rkpageflags_struct read_kpageflags(unsigned long pfn) {
    struct ret_rkpageflags_struct result;

    FILE* kpageflags_file;

    /*
     * Open the file.
     */
    kpageflags_file = fopen(KPAGEFLAGS_PATH, "rb");
    if(kpageflags_file != NULL) {
        /*
         * Find the item.
         */
        unsigned long offset = pfn * KPAGEFLAGS_ITEM_SIZE;
        if(fseek(kpageflags_file, offset, SEEK_SET) == 0) {
            /*
             * Read the item.
             */
            if(fread(&(result.kpageflags_item), 1, KPAGEFLAGS_ITEM_SIZE, kpageflags_file) != KPAGEFLAGS_ITEM_SIZE) {
                printf("Cannot read the item by offset %lx in kpageflags.\n", offset);
            }
        }else {
            printf("Cannot find the item by offset %lx in kpageflags.\n", offset);
        }
    }else {
        printf("Cannot open file %s .\n", KPAGEFLAGS_PATH);
    }

    /*
     * Close the file.
     */
    fclose(kpageflags_file);

    return result;
}