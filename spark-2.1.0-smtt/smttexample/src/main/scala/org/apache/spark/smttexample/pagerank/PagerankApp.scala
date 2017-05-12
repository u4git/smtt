package org.apache.spark.smttexample.pagerank

import org.apache.spark.graphx.GraphLoader
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.storage.StorageLevel

/**
  * Created by root on 17-4-25.
  */
object PagerankApp {

  def main(args: Array[String]): Unit = {
    val input = "data/smttexample/pagerank/input"
    val output = "data/smttexample/pagerank/output"
    val minEdgePartitions = 10000
    val maxIterations = 3
    val resetProb = 0.15

    val config = new SparkConf()
    config.setAppName("Pagerank App")
    config.setMaster("local")

    val sc = new SparkContext(config)

    val graph = GraphLoader.edgeListFile(sc, input, true, minEdgePartitions, StorageLevel.MEMORY_AND_DISK, StorageLevel.MEMORY_AND_DISK)

    val staticRank = graph.staticPageRank(maxIterations, resetProb).vertices
    staticRank.saveAsTextFile(output)

    sc.stop()
  }

}
