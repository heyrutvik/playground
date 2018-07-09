package the_essence

object Challenges {

  // implement identity function
  def id[A](x: A): A = x

  // function composition: [g . f] [right to left] [g after f]
  def compose[A, B, C](g: B => C, f: A => B): A => C = x => g(f(x))

  // test that composition respects identity
  def idTest[A, B](f: A => B)(x: A): Boolean = {
    f(x) == compose(f, id[A])(x) == compose(id[B], f)(x)
  }

  // yes, www is a category where webpages are objects and hyperlink between them are morphisms.

  // yes, facebook is a category where people are objects and friendships are morphisms.

  // iff each node has one edge to itself and possibly more than one to other nodes.
}
