package turingmachine.deterministic

import turingmachine._
import utils._

case class DTM(currentConfig: TMConfiguration, acceptStates: Set[S], ruleBook: DTMRuleBook) {

  def isInAccept: Boolean = acceptStates.contains(currentConfig.state)

  def isStuck: Boolean = !isInAccept && !ruleBook.isApply(currentConfig)
}

object DTM {

  def step(config: TMConfiguration): DTM => DTM = { dtm =>
    DTM(dtm.ruleBook.nextConfig(config), dtm.acceptStates, dtm.ruleBook)
  }

  def run(dtm: DTM) = {
    var dtm_ = dtm
    while((!dtm_.isInAccept) || !(dtm_.isStuck)) {
      println(dtm_.currentConfig)
      dtm_ = step(dtm_.currentConfig)(dtm_)
      println(dtm_.currentConfig.state)
    }
    dtm_
  }
}