package pushdown

import utils._

case class PDARule(state: S, char: Char, nextState: S, popChar: Char, pushChars: List[Char]) {

  def isApply(config: PDAConfig, char: Char) = {
    this.state == config.state &&
    this.popChar == config.stack.top &&
    this.char == char
  }

  def follow(config: PDAConfig) = {
    PDAConfig(nextState, nextStack(config))
  }

  def nextStack(config: PDAConfig) = {
    config.stack.pop.push(pushChars)
  }
}