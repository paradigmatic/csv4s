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

  val string = single[String](identity)
  val integer = single[Int]( s => s.toInt )
  val long = single[Int]( s => s.toLong )
  val long = single[Int]( s => s.toLong )
  val boolean = single[Boolean]( s => s.toBoolean )



}

object Parsers extends Parsers
