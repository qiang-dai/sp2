package com.mkyong.common.controller;

import org.apache.spark.SparkConf;
import org.apache.spark.ml.classification.OneVsRestModel;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * Created by xinmei365 on 2017-10-17
 */
public class RandomForest2 {
//    public static void main(String[] args) {
//        RandomForest randomForest = new RandomForest();
//        randomForest.token();
//    }
    public RandomForest2() {}


    public String token() {

        SparkConf conf = new SparkConf()
                .set("spark.streaming.clock", "org.apache.spark.util.ManualClock");
        SparkSession spark = SparkSession.builder()
                .master("local[2]")
                .appName("JavaStatistics")
                .config(conf)
                .getOrCreate();
        Tokenizer tokenizer = Tokenizer.load("/Users/xinmei365/news/tokenizer");
        HashingTF hashingTF = HashingTF.load("/Users/xinmei365/news/hashing-tf");
        OneVsRestModel oneVsRestModel = OneVsRestModel.load("/Users/xinmei365/news/ovr_model");
        Dataset<Row> dataset = spark.read()
                .option("header", "true")
                .format("csv").load("/Users/xinmei365/news/sampled_topic_news/part-00008-b1352b0e-7994-4d45-8ac3-8e417d8e318d-c000.csv");

        Dataset<Row> tokenRes = tokenizer.transform(dataset);
        Dataset<Row> hashRes = hashingTF.transform(tokenRes);
        Dataset<Row> oneRes = oneVsRestModel.transform(hashRes);
        System.out.println(oneRes);
        oneRes.take(1);
        return "RandomForest run over";
    }
}
