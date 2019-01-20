package pushdown.nondeterministic

import cats.data.State
import pushdown._
import utils._

case class NPDADesign(startState: S, bottomChar: Char, acceptStates: Set[S], ruleBook: NPDARuleBook) {

  val toNPDA = NPDA(Set(PDAConfig(startState, Stack(List(bottomChar)))), acceptStates, ruleBook)

  def isAccept(s: String) = {
    val simulate = for {
      _ <- NPDA.readString(s)
      dpda <- State.get
    } yield (dpda.isInAccept)
    simulate.runA(toNPDA).value
  }
}