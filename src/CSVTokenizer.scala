package org.streum.csv4s

object CSVTokenizer {

  case class CSVTokenException(msg:String) extends RuntimeException(msg)

  sealed trait TokenState {
    def elt: String
  }
  case class Element( elt: String ) extends TokenState
  case class Quote( elt: String ) extends TokenState
  case class EndQuote( elt: String ) extends TokenState

  def apply( 
    line: String, 
    sep: Char, 
    quote: Option[Char], 
    trim: Boolean 
  ): Seq[String] = {
    def rec( 
      rest: String, state: TokenState, tokens: Seq[String] 
    ): Seq[String] = if( rest.isEmpty ) {
      state match {
	case Quote(_) => throw CSVTokenException(
	  "Malformed CSV: last elements lacks an end-quote."
	)
	case other => (other.elt +: tokens).reverse
      }
    } else {
      val next = rest.head
      if( next == sep ) state match {
	case Quote( elt ) => 
	  rec( rest.tail, Quote(elt :+ next), tokens )
	case other => rec( rest.tail, Element(""), other.elt +: tokens )
      } else if( next == ' ' && trim ) state match {
	case Quote(elt) => rec( rest.tail, Quote(elt :+ next), tokens )
	case other => rec( rest.tail, other, tokens )
      } else if( quote.isDefined && next == quote.get ) state match {
	case Element(_) =>
	  rec( rest.tail, Quote(""), tokens )
	case Quote(elt) => 
	  rec( rest.tail, EndQuote(elt), tokens )
	case EndQuote(_) => 
	  throw CSVTokenException(
	    "Malformed CSV: quote closed several times"
	  )
      } else state match {
	case Quote(elt) => rec( rest.tail, Quote(elt :+ next), tokens )
	case Element(elt) => rec( rest.tail, Element(elt :+ next), tokens )
	case EndQuote(elt) => rec( rest.tail, EndQuote(elt), tokens )
      }
    }
    rec( line, Element(""), Seq() )
  } 





}
