package org.streum.csv4s

object CSVTokenizer {

  private def unquote(quote: String): String=>String = s =>
    if( s.startsWith(quote) && s.endsWith(quote) )
      s.substring(1,s.size-1)
    else s

  def apply( line: String, sep: String, quote: Option[String] ): Seq[String] = {
    val elts = line.split(sep).toList
    quote match {
      case Some(q) => elts.map(unquote(q))
      case None => elts
    }
  } 

}
