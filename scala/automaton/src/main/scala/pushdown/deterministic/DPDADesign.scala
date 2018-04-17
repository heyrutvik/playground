package pushdown.deterministic

import cats.data.State
import pushdown._
import utils._

case class DPDADesign(startState: S, bottomChar: Char, acceptStates: Set[S], ruleBook: DPDARuleBook) {

  val toDPDA = DPDA(PDAConfig(startState, Stack(List(bottomChar))), acceptStates, ruleBook)

  def isAccept(s: String) = {
    val simulate = for {
      _ <- DPDA.readString(s)
      dpda <- State.get
    } yield (dpda.isInAccept)
    simulate.runA(toDPDA).value
  }
}