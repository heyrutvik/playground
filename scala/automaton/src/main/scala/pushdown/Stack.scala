package pushdown

case class Stack(contents: List[Char]) {
  def push(c: Char) = Stack(c :: contents)
  def pop = Stack(contents.tail)
  def top = contents.head
  override def toString: String = s"<Stack ($top)${contents.tail.mkString}>"
}
