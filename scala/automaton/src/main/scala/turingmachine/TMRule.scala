package turingmachine

import utils._

case class TMRule(state: S, char: Char, nextState: S, writeChar: Char, direction: Direction) {

  def isApply(config: TMConfiguration) = {
    state == config.state && char == config.tape.middle
  }

  def follow(config: TMConfiguration) = TMConfiguration(nextState, nextTape(config))

  def nextTape(config: TMConfiguration) = {
    val newTape = config.tape.write(writeChar)
    direction match {
      case L => newTape.moveHeadLeft
      case R => newTape.moveHeadRight
    }
  }
}