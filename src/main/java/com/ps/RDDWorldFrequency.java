package com.ps;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import static java.util.Arrays.asList;

public class RDDWorldFrequency {

    public static void main(String[] args) {
        Utils.fakeHadoopIfAbsent();
        SparkConf conf = new SparkConf().setAppName("world-frequency").setMaster("local[4]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> file = sc.textFile(Utils.makeResourceAsFile("spark-wiki.txt").toString());
        // Count each word
        JavaPairRDD<String, Integer> countRDD = file
                .flatMap(line -> asList(line.split(" ")).iterator())
                .map(word -> word.replaceAll("[^a-zA-Z0-9\\-]", "").toLowerCase())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((count1, count2) -> count1 + count2);
        // Calculate frequency
        JavaPairRDD<Integer, Iterable<String>> frequencyRDD = countRDD
                .mapToPair(tuple -> new Tuple2<>(tuple._2, tuple._1))
                .groupByKey()
                .sortByKey(false);
        // Print
        frequencyRDD.collect()
                .forEach(tuple -> System.out.println(tuple._1 + ": " + tuple._2));
    }
}

