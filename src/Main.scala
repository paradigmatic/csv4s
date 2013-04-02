package org.streum.csv4s

object Main extends App {

  case class Color( name: String, red: Int, green: Int, blue: Int )

  val csvParser = new CSVReader[Color] {
    
    val separator=","
    val quoted=false

    lazy val parser = string ~ integer(3) ^^ {
      case label ~ rgb => {
	val r :: g :: b :: Nil = rgb
	Color( label, r, g, b )
      }
    }

  }

  
  val in1 = "purple,255,0,255"
  val in2 = "red,255,0,255"
  val in3 = "blue,0,0,255"

  val input = List( in1, in2, in3 ).toStream

  val output = csvParser.read(input)

  println( output )
  

}
