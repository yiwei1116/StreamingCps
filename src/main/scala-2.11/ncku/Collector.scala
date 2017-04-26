package ncku

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.HashMap
/**
  * Created by yiwei on 2017/2/21.
  */

object Collector extends Serializable{

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Receiver")
    /*  val sc = new StreamingContext(conf,Seconds(3))
      val realTimeData = sc.textFileStream("/home/yiwei/IdeaProjects/FPro/PubNub.txt")*/
    @transient
    val sc = new SparkContext(conf)
    //  val input =  sc.textFile("/home/yiwei/IdeaProjects/FPro/1K.txt").map(x=>x.toInt)
    val input =  sc.textFile("/home/steve02/StreamingCps/1K.txt").map(x=>x.toInt)

    val recevData = input.map(Module.Preprocess.subValue(input.collect())).cache()
    recevData.collect.foreach(println)
    val transferToChar = recevData.map(Module.Conversion.convertToChar(recevData.collect())).cache()

   // transferToChar.collect().foreach(println)

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

/*    def convertToChar(data : Array[Int]): String ={
      var tfToNum = ""


      val converseT = new HashMap[Int,String]()  { override def default(key:Int) = "-" }
      converseT += (1 -> "A", 2 -> "B", 3 -> "C",4 -> "D", 5 -> "E", 6 -> "F",7 -> "G", 8 -> "H",
        9 -> "I",10 -> "J", 11 -> "K", 12 -> "L",13 -> "M", 14 -> "N", 15 -> "O",16 -> "P",
        17 -> "Q", 18 -> "R",19 -> "S", 20 -> "T", 21 -> "U",22 -> "V", 23 -> "W", 24 -> "X",
        25 -> "Y", 0 -> "Z",

        -1 -> "a", -2 -> "b", -3 -> "c",-4 -> "d", -5 -> "e", -6 -> "f",-7 -> "g", -8 -> "h",
        -9 -> "i",-10 -> "j", -11 -> "k", -12 -> "l",-13 -> "m", -14 -> "n", -15 -> "o",-16 -> "p",
        -17 -> "q", -18 -> "r",-19 -> "s", -20 -> "t", -21 -> "u",-22 -> "v", -23 -> "w", -24 -> "x",
        -25 -> "y"
        )

      for( i <- 0 until data.size){



        tfToNum  += converseT.get(data.apply(i)).map(_.toString).getOrElse("")// transfer option String to String
        // println(converseT.get(data.apply(i)).map(_.toString).getOrElse(""))

      }


      return tfToNum

    }
    def subValue(data: Array[Int]): Array[Int] = {
      val base = 200
      var preprocessList = Array[Int]()
      var i = 0
      var p = 0
      var q = 0
      var k = 0
      preprocessList +:= (data.apply(0) - base)
      for (i <- 0 until data.size - 1) {
        p = data.apply(i)
        q = data.apply(i + 1)
        k = q - p
        preprocessList +:= k

      }
      //println(preprocessList.mkString(" "))// all element
      return preprocessList


    }*/
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
    val text = "TOBEORNOTTOBEORTOBEORNOT"
    val compressed = compress(text)
    println(compressed)
    val result = decompress(compressed)
    println(result)

    }



}
