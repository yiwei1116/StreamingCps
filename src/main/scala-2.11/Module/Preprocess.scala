package Module

/**
  * Created by yiwei on 2017/4/23.
  */
object Preprocess {



  def subValue(data: Array[Int]): Array[Int] ={
    val base = 200
    var preprocessList =  Array[Int]()
    var i = 0
    var p = 0
    var q = 0
    var k = 0
    preprocessList +:= (data.apply(0)-base)
    for ( i <- 0 until data.size-1 ){
      p = data.apply(i)
      q = data.apply(i+1)
      k = q - p
      preprocessList +:= k

    }
    //println(preprocessList.mkString(" "))// all element
    return preprocessList



  }

}
