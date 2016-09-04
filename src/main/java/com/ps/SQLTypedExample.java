package com.ps;

import com.ps.pojos.User;
import org.apache.spark.sql.*;

import java.nio.file.Paths;
import java.util.Collections;

import static org.apache.spark.sql.functions.col;


public class SQLTypedExample {

    public static void main(String[] args) {
        Utils.fakeHadoopIfAbsent();
        SparkSession spark = SparkSession.builder()
                .appName("sql-example")
                .master("local[4]")
                .config("spark.sql.warehouse.dir", Paths.get(System.getProperty("user.home"), ".spark", "temp").toString())
                .getOrCreate();
        Encoder<User> userEncoder = Encoders.bean(User.class);
        // Select all
        Dataset<User> dataset = spark.read().json(Utils.makeResourceAsFile("users-json.txt").toString()).as(userEncoder);
        dataset.select("*").show();
        // Create a new dataset
        User user = new User();
        user.setId(11);
        user.setFirstName("Pavlo");
        user.setLastName("Pohrebnyi");
        user.setEmail("pogrebnij@gmail.com");
        user.setIpAddress("0.0.0.0");
        user.setGender("Male");
        user.setAge(26);
        Dataset<User> newUserDataset = spark.createDataset(Collections.singletonList(user), userEncoder);
        newUserDataset.show();

    }
}

