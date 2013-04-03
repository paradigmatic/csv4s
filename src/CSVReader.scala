package org.streum.csv4s

import scala.io.Source
import scala.io.Codec

abstract class CSVReader[+A]( 
  separator: Char, 
  quote: Option[Char]=None
) extends Parsers {

  def parser: Parser[A]

  private def splitLine( line: String ) = 
    CSVTokenizer( line, separator, quote )

  def readFile( filename: String )
	      ( implicit codec: Codec ): Result[Seq[A]] = 
    readSource( Source.fromFile(filename) )

  def readSource( src: Source ): Result[Seq[A]] =
    read( src.getLines.toStream )

  def read( lines: Stream[String] ): Result[Seq[A]] = {
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
