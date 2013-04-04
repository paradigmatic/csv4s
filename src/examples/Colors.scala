package org.streum.csv4s
package examples

object Colors extends App {

  case class Color( name: String, red: Int, green: Int, blue: Int )

  val csvParser = new CSVReader[Color](',') {
    lazy val parser = string ~ integer(3) ^^ {
      case label ~ rgb => {
	val r :: g :: b :: Nil = rgb
	Color( label, r, g, b )
      }
    }
  }

  val output = csvParser.readFile( "data/colors.csv" )

  println( output )
  
}
