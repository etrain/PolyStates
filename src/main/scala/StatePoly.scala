import scala.util.parsing.json.JSON
import scala.io

object StatePoly {
  case class Point(val lat: Double, val lon: Double)
  case class Border(val points: List[Point])
  case class State(val name: String, val code: String, val borders: List[Border])

  def getPoint(points: List[Double]): Point = new Point(points(0), points(1))
  def getBorder(border: List[Any]): Border = new Border(border.map(i => getPoint(i.asInstanceOf[List[Double]])))
  def getBorders(borders: List[Any]): List[Border] = borders.map(b => getBorder(b.asInstanceOf[List[Any]]))

  def makeState(state: Map[String,Any]): State = new State(
	  state("name").asInstanceOf[String],
	  state("code").asInstanceOf[String],
	  getBorders(state("borders").asInstanceOf[List[Any]])
  )

  def loadPolys: Seq[State] = {
    val x = JSON.parseFull(io.Source.fromURL(getClass.getResource("/statepoly.json")).mkString)
	  for (
		  state <- x.get.asInstanceOf[List[Any]]
	  ) yield makeState(state.asInstanceOf[Map[String,Any]])
  }
  
  val states = loadPolys
  
  def point_inside_poly(x: Double, y: Double, poly: List[Point]) = {
   val n = poly.length
   var inside = false
  
   var p1x = poly(0).lat
   var p1y = poly(0).lon
   var p2x = p1x
   var p2y = p1y
   
   var xinters = 0.0
  
   for (i <- 0 to n) {
     p2x = poly(i % n).lat
     p2y = poly(i % n).lon
     if (y > (p1y min p2y)) {
       if (y <= (p1y max p2y)) {
         if (x <= (p1x max p2x)) {
           if (p1y != p2y) xinters = (y - p1y)*(p2x-p1x)/(p2y-p1y)+p1x
           if (p1x == p2x || x <= xinters) inside = !inside
         }
       }
     }
     p1x = p2x
     p1y = p2y
   }
   inside
  }
  
  def getState(x: Double , y: Double): String = {
    if(x < -130 || x > -60 || y < 25 || y > 50)
      return ""

    val state = states.find(state => state.borders.exists(border => point_inside_poly(x,y,border.points)))
    if (states == None) ""
    else state.get.code
  }
  
  def main(args: Array[String]) {
    println(states(0).borders(0).points)
    println(states.filter(state => state.borders.exists(border => point_inside_poly(-107.0,42.0,border.points))))
    println(point_inside_poly(-107.0, 42.0,states(0).borders(0).points))
    println(getState(-107.0,42.0))
    println(getState(-107.0,36.0))
    println(getState(-107.0,-100.0))
    println(getState(-75.0,40.0))
  }
}

