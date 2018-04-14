package nondeterministic

import common.FARule
import utils._

case class NFARuleBook(rules: Seq[FARule]) {

  def nextStates(states: Set[S], char: Char): Set[S] = {
    states.flatMap(s => ruleFor(s, char)).map(_.follow)
  }

  def ruleFor(state: S, char: Char): Seq[FARule] = rules.filter(r => r.isApply(state, char))
}