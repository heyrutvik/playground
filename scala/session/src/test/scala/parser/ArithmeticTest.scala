package parser

import common.UnitTest

class ArithmeticTest extends UnitTest("Arithmetic") {

  "evaluation" should "successed" in {
    Arithmetic.eval("1") should be (1)
    Arithmetic.eval("(1 + 2)") should be (3)
    Arithmetic.eval("1 + 2 + 3") should be (6)
    Arithmetic.eval("(1 + 2) * 3") should be (9)
    Arithmetic.eval("1 + 2 * 3") should be (7)
  }
}
