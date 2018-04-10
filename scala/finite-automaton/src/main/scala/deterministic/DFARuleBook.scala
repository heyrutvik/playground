package deterministic

import utils._

case class DFARuleBook(rules: Seq[DFARule]) {

  def nextState(state: State, char: Char) = ruleFor(state, char).map(_.follow).getOrElse(throw new Exception("dfa rule not found!"))

  def ruleFor(state: State, char: Char): Option[DFARule] = rules.filter(r => r.isApply(state, char)).headOption
}