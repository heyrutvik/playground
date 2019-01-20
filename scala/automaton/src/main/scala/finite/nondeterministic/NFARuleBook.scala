package finite.nondeterministic

import finite.FARule
import utils._

case class NFARuleBook(rules: Seq[FARule]) {

  def nextStates(states: Set[S], char: Char): Set[S] = {
    states.flatMap(s => ruleFor(s, char)).map(_.follow)
  }

  def ruleFor(state: S, char: Char): Seq[FARule] = rules.filter(r => r.isApply(state, char))

  def followFreeMoves(states: Set[S]): Set[S] = {
    val moreStates = nextStates(states, ' ')
    if (moreStates.subsetOf(states))
      states
    else
      followFreeMoves(states ++ moreStates)
  }
}