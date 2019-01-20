package turingmachine

import utils._

case class TMConfiguration(state: S, tape: Tape) {

  override def toString: String = s"<State ${state.name} :: ${tape}>"
}
