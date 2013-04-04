package org.streum.csv4s

trait Parsers {

  case class SingleParser[+A]( f: String=>A ) extends Parser[A] {
    def parse( s: Seq[String], pos: Pos ): Result[A] = {
      if( s.isEmpty ) Failure( "Not enough elements", pos )
      else try {
	val a = f(s.head)
	Success( a, s.tail, pos )
      } catch {
	case e: Exception => Failure( e.toString, pos )
      }
    }
  }

  def single[A]( f: String=>A ): SingleParser[A] = SingleParser[A](f)

  val string = single[String](identity)
  val integer = single[Int]( s => s.toInt )
  val long = single[Long]( s => s.toLong )
  val float = single[Float]( s => s.toFloat )
  val double = single[Double]( s => s.toDouble )
  val boolean = single[Boolean]( s => s.toBoolean )

  def nullable[A]( p: SingleParser[A] ): Parser[Option[A]] =
    Parser{ (s,pos) =>
      if( s.isEmpty ) Failure( "Not enough elements", pos )
      else if( s.head.isEmpty ) Success( None, s.tail, pos )
      else p.parse(s,pos).map( Some.apply )
    }


}

object Parsers extends Parsers
