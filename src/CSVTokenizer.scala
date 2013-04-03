package org.streum.csv4s

object CSVTokenizer {

  private def tokenize( 
    line: String, sep: Char, quote: Char 
  ): Seq[String] = {
    def tok( 
      rest: String, tokens: Seq[String], inquote: Boolean 
    ): Seq[String] = 
      if( rest.isEmpty ) 
	tokens.reverse
      else if( rest.head == sep && ! inquote ) 
	     tok( rest.tail, "" +: tokens, inquote )
      else if( rest.head == quote )
	     tok( rest.tail, tokens, !inquote )
      else 
	tok( rest.tail, (tokens.head :+ rest.head) +: tokens.tail, inquote )

    tok( line, Seq(""), false )
  }


  def apply( 
    line: String, 
    sep: Char, 
    quote: Option[Char], 
    trim: Boolean 
  ): Seq[String] = {
    val q = quote.getOrElse( "\0".head )
    tokenize(line,sep,q)
  } 

  sealed trait TokenState
  case class Elt( elt: String ) extends TokenState
  case class Quoted( elt: String ) extends TokenState
  case class End( elt: String )



}
