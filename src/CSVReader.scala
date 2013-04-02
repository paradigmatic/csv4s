package org.streum.csv4s

import scala.io.Source

trait CSVReader[+A] {

  def separator: String

  def quoted: Boolean

  def parser: Parser[A]

  private def splitLine( line: String ) = 
    line.split(separator).toList

  def read( lines: Stream[String] ) = {
    val ll = lines.zipWithIndex
    ll.map{ 
      case (l,i) => parser.parse( splitLine(l), Pos(i,0) ) 
    }
  }
}
