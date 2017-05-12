package org.apache.spark.smttexample.pagerank

import java.io.File

import org.apache.spark.graphx.util.GraphGenerators
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by root on 17-4-25.
  */
object DataGenerator {
  def main(args: Array[String]): Unit = {
    val outputPath = "data/smttexample/pagerank/input"
    val numVertices = 10000
    val numPartitions = 10
    val mu = 4.0
    val sigma = 1.3

    val config = new SparkConf()
    config.setAppName("Pagerank Data Generator")
    config.setMaster("local")

    val sc = new SparkContext(config)

    val outputPathFile = new File(outputPath)
    if (outputPathFile.exists()) {
      deleteDir(outputPathFile)
    }

    val graph = GraphGenerators.logNormalGraph(sc, numVertices, numPartitions, mu, sigma)
    graph.edges.map(s => s.srcId.toString + " " + s.dstId.toString + " " + s.attr.toString).saveAsTextFile(outputPath)

    sc.stop()
  }

  def deleteDir(file: File): Unit = {
    if (file.isDirectory) {
      val subFiles = file.listFiles()
      for (subFile <- subFiles) {
        if (subFile.isFile) {
          subFile.delete()
        } else {
          deleteDir(subFile)
        }
      }
      file.delete()
    }
  }
}
