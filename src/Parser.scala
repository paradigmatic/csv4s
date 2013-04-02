package org.streum.csv4s

case class ~[+A,+B]( a: A, b: B )

trait Parser[+A] {

  def parse( s: Seq[String], pos: Pos ): Result[A]

  def map[B]( f: A=>B ): Parser[B] = Parser( (s,p) => parse(s,p).map(f) )
  def ^^[B]( f: A=>B ): Parser[B] = map(f)
  /*def flatMap[B]( f: A=>Parser[B] ): Parser[B] = Parser{ (s,p) => 
    parse(s,p) match {
      case Success(a,r,q) => f(a).parse(r,q.nextColumn)
      case f: Failure => f
    }
  }*/
  def ~[B]( pb: Parser[B] ): Parser[~[A,B]] = Parser{ (s,p) =>
    parse( s, p ) match {
      case f: Failure => f
      case Success(a,r,q) => pb.parse(r,q.nextColumn) match {
	case f: Failure => f
	case Success(b,r2,q2) => Success( org.streum.csv4s.~(a,b), r2, q2)
      }
    }
  }
  def apply( n: Int ): Parser[List[A]] = repeat(n)
  def repeat( n: Int ): Parser[List[A]] = {
    def rep( n: Int, prev: Success[List[A]] ): Result[List[A]] = {
      if( n <= 0 ) prev.copy( pos=prev.pos.prevColumn )
      else {
	parse( prev.rest, prev.pos ) match {
	  case f: Failure => f
	  case Success(a,r,q) => 
	    rep( n-1, Success( prev.get :+ a, r, q.nextColumn ) )
	}
      }
    }
    Parser( (s,p) => rep( n, Success( List.empty[A], s, p ) ) )
  }
}
object Parser {
  def apply[A]( f: (Seq[String],Pos)=>Result[A] ): Parser[A] = 
    new Parser[A] {
      def parse( s: Seq[String], pos: Pos ) = f(s,pos)
    }

  def unit[A]( a: A ): Parser[A] = Parser{ (s,pos) =>
    Success( a, s.tail, pos )
  }

}



