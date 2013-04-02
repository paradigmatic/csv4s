package org.streum.csv4s

object Main extends App {

  import Parser._

  val csvParser = new CSVReader[String] {
    
    val separator=" "
    val quoted=false

    lazy val color = integer(3) ^^ { lst=>
      val r :: g :: b :: Nil  = lst
      (r,g,b)
    } 

    lazy val parser = string ~ color ^^ {
      case label ~ c => s"$label: $c"
    }

  }

  
  val in1 = "purple,255,0,255"
  val in2 = "red,255,0,0"
  val in3 = "blue,0,0,255"

  val input = List( in1, in2, in3 ).toStream

  val output = csvParser.read(input)

  println( output.toList )
  

}
