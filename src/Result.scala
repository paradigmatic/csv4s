package org.streum.csv4s

sealed trait Result[+A] {
  def map[B]( f: A=>B ): Result[B] = this match {
    case Success( a, r, p ) => Success( f(a), r, p )
    case f:Failure => f
  }
  def toOption: Option[A]
}

case class Success[+A]( get: A, rest: Seq[String], pos: Pos ) extends Result[A] { 
  def toOption = Some(get) 
}

case class Failure( cause: String, pos: Pos ) extends Result[Nothing] {
  val toOption = None
}
