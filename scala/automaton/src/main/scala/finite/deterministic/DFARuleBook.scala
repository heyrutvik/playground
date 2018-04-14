package finite.deterministic

import finite.FARule
import utils._

case class DFARuleBook(rules: Seq[FARule]) {

  def nextState(state: S, char: Char): S = ruleFor(state, char).map(_.follow).getOrElse(throw new Exception("dfa rule not found!"))

  def ruleFor(state: S, char: Char): Option[FARule] = rules.filter(r => r.isApply(state, char)).headOption
}