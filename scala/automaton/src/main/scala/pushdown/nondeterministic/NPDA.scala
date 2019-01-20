package pushdown.nondeterministic

import cats.data.State
import finite.nondeterministic.NFA
import pushdown.PDAConfig
import utils._

case class NPDA(ccs: Set[PDAConfig], acceptStates: Set[S], ruleBook: NPDARuleBook) {
  val currentConfigs = ruleBook.followFreeMoves(ccs)
  def isInAccept: Boolean = currentConfigs.filter(config => acceptStates.contains(config.state)).nonEmpty
}

object NPDA {

  def readChar(char: Char): NPDA => NPDA =  { npda =>
    NPDA(npda.ruleBook.nextConfig(npda.currentConfigs, char), npda.acceptStates, npda.ruleBook)
  }

  def readString(s: String): State[NPDA, Unit] = {
    s.map(c => readChar(c)).map(f => State.modify(f)).reduce((s1, s2) => s1.flatMap(_ => s2))
  }
}

