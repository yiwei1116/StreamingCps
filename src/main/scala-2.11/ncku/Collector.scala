package ncku

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.HashMap
/**
  * Created by yiwei on 2017/2/21.
  */

object Collector extends Serializable{

 /* def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Sender")
    /*  val sc = new StreamingContext(conf,Seconds(3))
      val realTimeData = sc.textFileStream("/home/yiwei/IdeaProjects/FPro/PubNub.txt")*/
    val sc = new SparkContext(conf)
    //  val input =  sc.textFile("/home/yiwei/IdeaProjects/FPro/1K.txt").map(x=>x.toInt)
    val input =  sc.textFile("/home/steve02/StreamingCps/1K.txt").map(x=>x.toInt)
    var myList = Array(1,2,3,10)
    val recevData = input.map(Module.Preprocess.subValue(input.collect())).cache()
   // recevData.collect.foreach(println)
   // println(recevData.count())
   val transferToChar = recevData.map(Module.Conversion.convertToChar(recevData.collect()))
   // val transferToChar = Module.Conversion.convertToChar(recevData.collect())
    //println(transferToChar.mkString(""))
  //  transferToChar.collect.foreach(println)

    def compress(tc:String) = {
      //initial dictionary
      val startDict = (1 to 255).map(a=>(""+a.toChar,a)).toMap
      val (fullDict, result, remain) = tc.foldLeft ((startDict, List[Int](), "")) {
        case ((dict,res,leftOver),nextChar) =>
          if (dict.contains(leftOver + nextChar)) // current substring already in dict
            (dict, res, leftOver+nextChar)
          else if (dict.size < 4096) // add to dictionary
            (dict + ((leftOver+nextChar, dict.size+1)), dict(leftOver) :: res, ""+nextChar)
          else // dictionary is full
            (dict, dict(leftOver) :: res, ""+nextChar)
      }
      if (remain.isEmpty) result.reverse else (fullDict(remain) :: result).reverse
    }


    def decompress(ns: List[Int]): String = {
      val startDict = (1 to 255).map(a=>(a,""+a.toChar)).toMap
      val (_, result, _) =
        ns.foldLeft[(Map[Int, String], List[String], Option[(Int, String)])]((startDict, Nil, None)) {
          case ((dict, result, conjecture), n) => {
            dict.get(n) match {
              case Some(output) => {
                val (newDict, newCode) = conjecture match {
                  case Some((code, prefix)) => ((dict + (code -> (prefix + output.head))), code + 1)
                  case None => (dict, dict.size + 1)
                }
                (newDict, output :: result, Some(newCode -> output))
              }
              case None => {
                // conjecture being None would be an encoding error
                val (code, prefix) = conjecture.get
                val output = prefix + prefix.head
                (dict + (code -> output), output :: result, Some(code + 1 -> output))
              }
            }
          }
        }
      result.reverse.mkString("")
    }
    // test
    val text = transferToChar.collect()
    val compressed = Module.CompressModule.compress(text.toString)
    println(compressed)
    val result = Module.CompressModule.decompress(compressed)
    println(result)

    }
*/


}
