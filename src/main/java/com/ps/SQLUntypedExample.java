package com.ps;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.nio.file.Paths;

import static org.apache.spark.sql.functions.col;


public class SQLUntypedExample {

    public static void main(String[] args) {
        Utils.fakeHadoopIfAbsent();
        SparkSession spark = SparkSession.builder()
                .appName("sql-example")
                .master("local[4]")
                .config("spark.sql.warehouse.dir", Paths.get(System.getProperty("user.home"), ".spark", "temp").toString())
                .getOrCreate();
        Dataset<Row> dataset = spark.read().json(Utils.makeResourceAsFile("users-json.txt").toString());
        // Select all
        dataset.select("*").show();
        // Select all users older then 25
        dataset.select(col("firstName"), col("age")).filter(col("age").gt(25)).show();
        // Group by age
        dataset.groupBy(col("age")).count().show();
        // Select all users older then 25 with SQL statement
        dataset.createOrReplaceTempView("users");
        spark.sql("SELECT * FROM users").show();
    }
}

