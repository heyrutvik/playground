package pushdown.nondeterministic

import pushdown.{PDAConfig, PDARule}

case class NPDARuleBook(rules: Seq[PDARule]) {
  def nextConfig(configs: Set[PDAConfig], char: Char) = {
    configs.flatMap(config => rulesFor(config, char).map(_.follow(config)))
  }

  def rulesFor(config: PDAConfig, char: Char) = {
    rules.filter(r => r.isApply(config, char))
  }

  def followFreeMoves(configs: Set[PDAConfig]): Set[PDAConfig] = {
    val moreConfigs = nextConfig(configs, ' ')
    if (moreConfigs.subsetOf(configs))
      configs
    else
      followFreeMoves(configs ++ moreConfigs)
  }
}