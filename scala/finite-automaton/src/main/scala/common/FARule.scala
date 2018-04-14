package common

import utils._

case class FARule(state: S, char: Char, nextState: S) {

  def isApply(state: S, char: Char) = {
    this.state == state && this.char == char
  }

  def follow = nextState
}
