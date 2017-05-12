package org.apache.spark.smtt

import java.nio.ByteBuffer

/**
  * Created by root on 17-2-24.
  */
object SMTT {

  val ACCESSTYPE_WRITE = "write"
  val ACCESSTYPE_READ = "read"

  /**
    * Trace storage serialized memory.
    *
    * @param accessType
    * @param blockId
    * @param chunks
    */
  def traceStorageSeri(accessType: String, blockId: String, chunks: Array[ByteBuffer]): Unit = {
    for (byteBuffer <- chunks) {
      JSMTT.traceStorageSeri(accessType, blockId, byteBuffer)
    }
  }

  /**
    * Trace storage deserialized memory.
    *
    * @param accessType
    * @param blockId
    * @param value
    * @tparam T
    */
  def traceStorageDese[T](accessType: String, blockId: String, value: Array[T]): Unit = {
    for (i <- 0 until value.length) {
      val obj = value(i)
      JSMTT.traceStorageDese(accessType, blockId, obj)
    }
  }

  def registerWriter2RddInfo(writerHashcode: Int, rddInfo: String): Unit = {
    JSMTT.registerWriter2RddInfo(writerHashcode, rddInfo)
  }

  def removeWriter2RddInfo(writerHashcode: Int): Unit = {
    JSMTT.removeWriter2RddInfo(writerHashcode)
  }

  def registerSorter2Writer(sorterHashcode: Int, writerHashcode: Int): Unit = {
    JSMTT.registerSorter2Writer(sorterHashcode, writerHashcode)
  }

  def removeSorter2Writer(sorterHashcode: Int): Unit = {
    JSMTT.removeSorter2Writer(sorterHashcode)
  }

  /**
    * Trace execution intermediate results.
    *
    * @param accessType
    * @param sorterHashcode
    * @param baseObj
    * @param offset
    * @param length
    */
  def traceExecutionData(accessType: String, sorterHashcode: Int, baseObj: Object, offset: Long, length: Long): Unit = {
    JSMTT.traceExecutionData(accessType, sorterHashcode, baseObj, offset, length)
  }

  def registerInMemSorter2Sorter(inMemSorterHashcode: Int, sorterHashcode: Int): Unit = {
    JSMTT.registerInMemSorter2Sorter(inMemSorterHashcode, sorterHashcode)
  }

  def removeInMemSorter2Sorter(inMemSorterHashcode: Int): Unit = {
    JSMTT.removeInMemSorter2Sorter(inMemSorterHashcode)
  }

  def registerArray2InMemSorter(arrayHashcode: Int, inMemSorterHashcode: Int): Unit = {
    JSMTT.registerArray2InMemSorter(arrayHashcode, inMemSorterHashcode)
  }

  def removeArray2InMemSorter(arrayHashcode: Int): Unit = {
    JSMTT.removeArray2InMemSorter(arrayHashcode)
  }

  /**
    * Trace execution pointers of intermediate results.
    *
    * @param accessType
    * @param arrayHashcode
    * @param baseObj
    * @param offset
    * @param length
    */
  def traceExecutionPointer(accessType: String, arrayHashcode: Int, baseObj: Object, offset: Long, length: Long): Unit = {
    JSMTT.traceExecutionPointer(accessType, arrayHashcode, baseObj, offset, length)
  }

}
