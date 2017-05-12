package org.apache.spark.smttexample.wordcount

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by root on 17-5-4.
  */
object WordCounter {

  def main(args: Array[String]): Unit = {
    val input = "/root/tmp/test_wordcount"

    val conf = new SparkConf()
    conf.setAppName("Word Counting App")
    conf.setMaster("local")

    val sc = new SparkContext(conf)

    val textRdd = sc.textFile(input)

    val wordsRdd = textRdd.flatMap(line => line.split(" "))

    val initRdd = wordsRdd.map(word => (word, 1))

    val rsltRdd = initRdd.reduceByKey((v1, v2) => v1 + v2)

    rsltRdd.foreach(c => println(c._1 + ", " + c._2))
  }

}
