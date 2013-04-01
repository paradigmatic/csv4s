package org.streum.csv4s

case class ~[+A,+B]( a: A, b: B )

sealed trait Result[+A] {
  def map[B]( f: A=>B ): Result[B] = this match {
    case Success( a, r ) => Success( f(a), r )
    case Failure => Failure
  }
}
case class Success[+A]( a: A, rest: Seq[String] ) extends Result[A]
case object Failure extends Result[Nothing]


trait Parser[+A] {
  def parse( s: Seq[String] ): Result[A]
  def map[B]( f: A=>B ): Parser[B] = Parser( s => parse(s).map(f) )
  def ^^[B]( f: A=>B ): Parser[B] = map(f)
  def flatMap[B]( f: A=>Parser[B] ): Parser[B] = Parser{ s => 
    parse(s) match {
      case Success(a,r) => f(a).parse(r)
      case Failure => Failure
    }
  }
  def ~[B]( pb: Parser[B] ): Parser[~[A,B]] = Parser{ s =>
    parse( s ) match {
      case Failure => Failure
      case Success(a,r) => pb.parse(r) match {
	case Failure => Failure
	case Success(b,r2) => Success( org.streum.csv4s.~(a,b), r2)
      }
    }
  }
  def repeat( n: Int ): Parser[Seq[A]] = {
    def rep( n: Int, prev: Success[Seq[A]] ): Result[Seq[A]] = {
      if( n <= 0 ) prev
      else {
	parse( prev.rest ) match {
	  case Failure => Failure
	  case Success(a,r) => rep( n-1, Success( prev.a :+ a, r ) )
	}
      }
    }
    Parser( s => rep( n, Success( Seq[A](), s ) ) )
  }
}
object Parser {
  def apply[A]( f: Seq[String]=>Result[A] ): Parser[A] = new Parser[A] {
    def parse( s: Seq[String] ) = f(s)
  }
  def single[A]( f: String=>A ): Parser[A] = Parser{ s=>
    Success( f(s.head), s.tail )
  }

  val integer = single[Int]( s => s.toInt )
  val boolean = single[Boolean]( s => s.toBoolean )
  val string = single[String](identity)
}



