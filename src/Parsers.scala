package org.streum.csv4s

trait Parsers {

  def single[A]( f: String=>A ): Parser[A] = Parser{ (s,pos) =>
    if( s.isEmpty ) Failure( "Not enough elements", pos )
    else try {
      val a = f(s.head)
      Success( a, s.tail, pos )
    } catch {
      case e: Exception => Failure( e.toString, pos )
    }
  }

  val integer = single[Int]( s => s.toInt )
  val boolean = single[Boolean]( s => s.toBoolean )
  val string = single[String](identity)


}

object Parsers extends Parsers
