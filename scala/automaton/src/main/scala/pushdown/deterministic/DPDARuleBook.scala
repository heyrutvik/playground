package pushdown.deterministic

import pushdown.{PDAConfig, PDARule}

case class DPDARuleBook(rules: Seq[PDARule]) {
  def nextConfig(config: PDAConfig, char: Char) = {
    ruleFor(config, char).map(_.follow(config)).getOrElse(throw new Exception("rule not found"))
  }

  def ruleFor(config: PDAConfig, char: Char) = {
    rules.filter(r => r.isApply(config, char)).headOption
  }

  private def isApply(config: PDAConfig, char: Char) = {
    ruleFor(config, char).nonEmpty
  }

  def followFreeMoves(config: PDAConfig): PDAConfig = {
    if (isApply(config, ' '))
      followFreeMoves(nextConfig(config, ' '))
    else
      config
  }
}