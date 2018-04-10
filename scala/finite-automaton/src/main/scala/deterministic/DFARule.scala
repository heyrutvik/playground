package deterministic

import utils._

case class DFARule(state: State, char: Char, nextState: State) {

  def isApply(state: State, char: Char) = {
    this.state == state && this.char == char
  }

  def follow = nextState
}
