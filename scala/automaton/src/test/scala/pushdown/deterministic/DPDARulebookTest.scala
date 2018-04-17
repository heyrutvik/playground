package pushdown.deterministic

import common.UnitTest
import pushdown._
import utils._

class DPDARulebookTest extends UnitTest("DPDA Rule Book") {

  "dpda rulebook" should "state check" in {

    val rulebook = DPDARuleBook(Seq(
      PDARule(S(1), '(', S(2), '$', List('b', '$')),
      PDARule(S(2), '(', S(2), 'b', List('b', 'b')),
      PDARule(S(2), ')', S(2), 'b', Nil),
      PDARule(S(2), ' ', S(1), '$', List('$'))
    ))

    val config = PDAConfig(S(2), Stack(List('$')))
    val nextConfig = rulebook.followFreeMoves(config)
    nextConfig.state should be (S(1))
  }
}

