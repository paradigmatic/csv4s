package org.streum.csv4s

import org.scalatest.FreeSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.PropertyChecks

class CSVTokenizerSpec 
extends FreeSpec with ShouldMatchers with PropertyChecks {

  "The CSV tokenizer" -{

    val csv = CSVTokenizer

    "can accept a custom separator" in {
      val in = "1;2;3"
      val out = csv(in,";",None)
      out should be (Seq("1","2","3"))
    }

    "must treat repeated seps as empty element" in {
      val in = "1;2;;3"
      val out = csv(in,";",None)
      out should be (Seq("1","2","","3"))
    }

    "should not ignore whitespaces" in {
      val in = " 1;2 ; ;  3   "
      val out = csv(in,";",None)
      out should be (Seq(" 1","2 "," ","  3   "))
    }

    "must recognize quoted elts when asked" in {
      val in = "1;\"2\";3"
      val out = csv(in,";",Some("\""))
      out should be (Seq("1","2","3"))
    }

    "must ignore separators inside quotes" in {
      val in = """1;"2;3""""
      val out = csv(in,";",Some("\""))
      out should be (Seq("1","2;3"))
    }


  }

}
