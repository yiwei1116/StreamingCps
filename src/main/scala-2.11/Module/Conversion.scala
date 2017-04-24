package Module
import collection.mutable.HashMap
/**
  * Created by yiwei on 2017/4/23.
  */
object Conversion {



  class Test extends java.io.Serializable {
  def convertToChar(data : Array[Int]): String ={
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

}}
