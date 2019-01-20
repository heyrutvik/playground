package finite.nondeterministic

import common.UnitTest
import finite.FARule
import utils._

class NFADesignTest extends UnitTest("NFA Rule Book") {

  "simulate nd fa using string" should "be in accept state" in {

    val rulebook = NFARuleBook(Seq(
      FARule(S(1), 'a', S(1)), FARule(S(1), 'b', S(1)), FARule(S(1), 'b', S(2)),
      FARule(S(2), 'a', S(3)), FARule(S(2), 'b', S(3)),
      FARule(S(3), 'a', S(4)), FARule(S(3), 'b', S(4))
    ))

    val design = NFADesign(S(1), Set(S(4)), rulebook)
    design.isAccept("bab") should be (true)
    design.isAccept("bbbbb") should be (true)
    design.isAccept("bbabb") should be (false)
  }

  it should "with free moves" in {
    val rulebook = NFARuleBook(Seq(
      FARule(S(1), ' ', S(2)), FARule(S(1), ' ', S(4)),
      FARule(S(2), 'a', S(3)),
      FARule(S(3), 'a', S(2)),
      FARule(S(4), 'a', S(5)),
      FARule(S(5), 'a', S(6)),
      FARule(S(6), 'a', S(4))
    ))

    rulebook.followFreeMoves(Set(S(1))) should be (Set(S(1),S(2),S(4)))
    val design = NFADesign(S(1), Set(S(2), S(4)), rulebook)
    design.isAccept("aa") should be (true)
    design.isAccept("aaa") should be (true)
    design.isAccept("aaaaa") should be (false)
    design.isAccept("aaaaaa") should be (true)
  }
}
