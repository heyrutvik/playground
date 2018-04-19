package turingmachine.deterministic

import turingmachine.{TMConfiguration, TMRule}

case class DTMRuleBook(rules: Seq[TMRule]) {

  def nextConfig(config: TMConfiguration) = ruleFor(config).map(_.follow(config)).getOrElse(throw new Exception("rule not found"))

  def ruleFor(config: TMConfiguration) = rules.filter(rule => rule.isApply(config)).headOption

  def isApply(config: TMConfiguration) = ruleFor(config).isEmpty
}