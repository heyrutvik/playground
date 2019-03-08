package machine

package object elaborate {

  private val quote: Char = '\''

  private def addPrime(s: String): String = s + quote

  def freshen(used: List[String], x: String): String = {
    if (used.contains(x)) freshen(x :: used, addPrime(x))
    else x
  }

  def removePrimes(name: String): String = name.filterNot(_ == quote)
}
