package org.streum.csv4s

case class Pos( row: Int, column: Int ) {
  def nextColumn = copy( column = column+1 )
  def prevColumn = copy( column = column-1 )
}
