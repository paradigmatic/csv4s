package org.streum.csv4s
package examples

object Particles extends App {

  case class Particle( 
    mass: Double, 
    radius: Double, 
    pos: Vector[Double], 
    v: Vector[Double]
  )

  val csvParser = new CSVReader[Particle](
    separator=',',
    header=true,
    trim=true
  ) {
    lazy val vec = double(3) ^^ { _.toVector }

    lazy val parser = double(2) ~ vec(2) ^^ {
      case Seq(m,r) ~ Seq(p,v) => Particle(m,r,p,v)
    }
  }

  val output = csvParser.readFile( "data/particles.csv" )

  println( output )
  
}
