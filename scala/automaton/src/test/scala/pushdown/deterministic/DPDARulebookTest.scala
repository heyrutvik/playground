package pushdown.deterministic

import common.UnitTest
import pushdown._
import utils._

class DPDARulebookTest extends UnitTest("DPDA Rule Book") {

  "check next state" should "be as in rulebook" in {

    val rulebook = DPDARulebook(Seq(
      PDARule(S(1), '(', S(2), '$', List('b', '$')),
      PDARule(S(2), '(', S(2), 'b', List('b', 'b')),
      PDARule(S(2), ')', S(2), 'b', Nil),
      PDARule(S(1), ' ', S(1), '$', List('$'))
    ))

    val config = PDAConfig(S(1), Stack(List('$')))
  }
}

