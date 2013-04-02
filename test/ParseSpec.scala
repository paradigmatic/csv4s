package org.streum.csv4s

import org.scalatest.FreeSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.PropertyChecks

class ParserSpec extends FreeSpec with ShouldMatchers with PropertyChecks {

  "A simple parser" -{

    val parser = Parsers.string

    "is a functor" in {
      val mappedParser = parser.map( _.size )
      val pos = Pos(3,5)
      forAll { (s:String) =>
	val len = s.size
	val res = mappedParser.parse(Seq(s),pos)
	val Success(i,rest,p) = res
	i should be (len)
	rest should be ('empty)
	pos should be (pos)
      }
    }

    "is joinable" in {
      val pos = Pos(3,5)
      val joined = parser ~ parser ^^ {
	case s1 ~ s2 => (s1,s2)
      }
      forAll { (s1:String, s2: String ) =>
	val in = Seq(s1,s2)
	val Success((a,b),_,p) = joined.parse(in,pos)
	a should be (s1)
	b should be (s2)
	p should be (pos.nextColumn)
      }
    }

    "is repeatable" in {
      val pos = Pos(3,5)
      val repeated = parser(3)
      forAll { (s1:String, s2: String, s3: String ) =>
	val in = Seq(s1,s2,s3)
	val Success(out,_,p) = repeated.parse(in,pos)
	in should be (out)	
	p should be (pos.nextColumn.nextColumn)
      }
    }


/*
    "is a monad (law 1)" in {
      val parser2 = parser.flatMap( Parser.unit )
      val pos = Pos(3,5)
      forAll { (s:String) =>
	val in = Seq(s,"")
	parser.parse(in,pos) should be (parser2.parse(in,pos))
      }
    }
*/
  }

}
