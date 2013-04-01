package org.streum.csv4s

object Main extends App {

  import Parser._

  val color = integer.repeat(3) ^^ {
    case r :: g :: b :: Nil  => (r,g,b)
  }

  val parser = string ~ color ^^ {
    case label ~ c => s"$label: $c"
  }

  val input = Seq( "purple", "255", "0", "255" )

  parser.parse(input) match {
    case Success( s, _ ) => println(s)
    case Failure => println("FAILURE")
  }
  

}
