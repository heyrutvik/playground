package deterministic

import utils._
import cats.data._

case class DFADesign(startState: S, acceptState: Set[S], ruleBook: DFARuleBook) {

  val toDFA = DFA(startState, acceptState, ruleBook)

  def isAccept(s: String) = {
    val simulate = for {
      _ <- DFA.readString(s)
      dfa <- State.get
    } yield (dfa.isInAccept)
    simulate.runA(toDFA).value
  }
}
