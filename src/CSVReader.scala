package org.streum.csv4s

import scala.io.Source

trait CSVReader[+A] extends Parsers {

  def separator: String

  def quoted: Boolean

  def parser: Parser[A]

  private def splitLine( line: String ) = 
    line.split(separator).toList

  def read( lines: Stream[String] ) = {
    def acc( 
      elts: Stream[Result[A]], res: Success[Seq[A]] 
    ): Result[Seq[A]] = 
      if( elts.isEmpty ) res.copy( pos=res.pos.prevColumn )
      else elts.head match {
	case f: Failure => f
	case Success( a, rest, pos ) => 
	  acc( elts.tail, Success( res.get :+ a, rest, pos ) )
      }

    val ll = lines.zipWithIndex.map{ 
      case (l,i) => parser.parse( splitLine(l), Pos(i,0) ) 
    }

    acc( ll, Success( Seq.empty[A], Seq(), Pos(0,0) ) )
  }
}
