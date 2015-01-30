import org.apache.spark.{SparkContext,SparkConf}
import org.apache.spark.SparkContext._

object SparkStatePoly {

  def main(args: Array[String]) {
    val sc = new SparkContext(new SparkConf())
    
    val points = Seq((-107.0,42.0), (-107.0,36.0), (-107.0,-100.0), (-75.0,40.0))
    
    val pointsRdd = sc.parallelize(points, 4)
    
    val states = pointsRdd.map(x => StatePoly.getState(x._1, x._2)).collect
    sc.stop()
    states.map(println)
  }
}