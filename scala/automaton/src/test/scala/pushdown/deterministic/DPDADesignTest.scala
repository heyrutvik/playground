package pushdown.deterministic

import common.UnitTest
import pushdown._
import utils._

class DPDADesignTest extends UnitTest("DFA Rule Book") {

  "simulate dpda using string" should "be in accept state" in {

    val rulebook = DPDARuleBook(Seq(
      PDARule(S(1), '(', S(2), '$', List('b', '$')),
      PDARule(S(2), '(', S(2), 'b', List('b', 'b')),
      PDARule(S(2), ')', S(2), 'b', Nil),
      PDARule(S(2), ' ', S(1), '$', List('$'))
    ))

    val dpda = DPDA(PDAConfig(S(1), Stack(List('$'))), Set(S(1)), rulebook)
    dpda.isInAccept should be (true)
    DPDA.readString("(()").runS(dpda).value.isInAccept should be (false)

    val dpdaDesign = DPDADesign(S(1), '$', Set(S(1)), rulebook)
    dpdaDesign.isAccept("(((((((((())))))))))") should be (true)
    dpdaDesign.isAccept("(()") should be (false)
  }
}
