package pushdown.deterministic

import cats.data.State
import pushdown.PDAConfig
import utils._

case class DPDA(cc: PDAConfig, acceptStates: Set[S], ruleBook: DPDARuleBook) {
  val currentConfig = ruleBook.followFreeMoves(cc)
  def isInAccept: Boolean = acceptStates.contains(currentConfig.state)
}

object DPDA {

  def readChar(char: Char): DPDA => DPDA =  { dpda =>
    DPDA(dpda.ruleBook.nextConfig(dpda.currentConfig, char), dpda.acceptStates, dpda.ruleBook)
  }

  def readString(s: String): State[DPDA, Unit] = {
    s.map(c => readChar(c)).map(f => State.modify(f)).reduce((s1, s2) => s1.flatMap(_ => s2))
  }
}