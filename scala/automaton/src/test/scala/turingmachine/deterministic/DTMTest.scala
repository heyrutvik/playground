package turingmachine.deterministic

import common.UnitTest
import turingmachine.TMRule
import utils._
import turingmachine._

class DTMTest extends UnitTest("DTM Test") {

  "turing machine" should "simulation" in {

    val ruleBook = DTMRuleBook(Seq(
      TMRule(S(1), 'X', S(1), 'X', R),
      TMRule(S(1), 'a', S(2), 'X', R),
      TMRule(S(1), '_', S(6), '_', L),

      TMRule(S(2), 'a', S(2), 'a', R),
      TMRule(S(2), 'X', S(2), 'X', R),
      TMRule(S(2), 'b', S(3), 'X', R),

      TMRule(S(3), 'b', S(3), 'b', R),
      TMRule(S(3), 'X', S(3), 'X', R),
      TMRule(S(3), 'c', S(4), 'X', R),

      TMRule(S(4), 'c', S(4), 'c', R),
      TMRule(S(4), '_', S(5), '_', L),

      TMRule(S(5), 'a', S(5), 'a', L),
      TMRule(S(5), 'b', S(5), 'b', L),
      TMRule(S(5), 'c', S(5), 'c', L),
      TMRule(S(5), 'X', S(5), 'X', L),
      TMRule(S(5), '_', S(1), '_', R)
    ))

    val tape = Tape(Vector(), 'a', Vector('a', 'a', 'b', 'b', 'b', 'c', 'c', 'c'), '_')
    val dtm = DTM(TMConfiguration(S(1), tape), Set(S(6)), ruleBook)
    DTM.run(dtm).currentConfig.state should be (S(6))
  }
}
