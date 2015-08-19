package com.redhadoop.hive.udf;


import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * HSVRank.
 *
 */
@Description(name = "HSVRank", value = "_FUNC_(expr) - Example DAF that returns the HSVRank")
public class HSVRank extends UDF {

  public Double evaluate(Double HM,Double HS,Double HT,Double HM2,Double HS2,Double HT2) {
	  return Math.sqrt(Math.pow(HM-HM2,2)+Math.pow((HT-HT2),2)+Math.pow((HS-HS2),2));
  }
  
  public static void main(String[] args) {
	  HSVRank rank = new HSVRank();
	  System.out.println(rank.evaluate(100.00, 200.00, 300.00, 110.00, 120.00, 230.00));
  }
}
