package machine

package object elaborate {

  private def addPrime(s: String): String = s + "'"

  def freshen(used: List[String], x: String): String = {
    if (used.contains(x)) freshen(x :: used, addPrime(x))
    else x
  }
}
