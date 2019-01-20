package types_and_functions_2

import scala.util._

object Challenges {

  def memoize[A, B](f: A => B): A => B = {

    // `var` so we can re-assign returned immutable map
    var storage = Map.empty[A, B]

    x: A =>
      storage.getOrElse(x, {
        val a = f(x)
        storage += (x -> a)
        a
      })
  }

  val rand: Unit => Int = _ => Random nextInt Int.MaxValue

  val randWithSeed: Int => Int = seed => new Random(seed) nextInt Int.MaxValue

  /**
    * Notice (in below code) that every function of unit is equivalent to picking a single element
    * from the target type. This is an example of how we can replace explicit mention of elements
    * of a set by talking about functions (arrows) instead. Functions from unit to any type A are
    * in one-to-one correspondence with the elements of that set A.
    * (page 24)
    */

  val mRand: Unit => Int = memoize(rand)

  val mRandWithSeed: Int => Int = memoize(randWithSeed)

  // factorial
  val fact: Int => Int = {
    case 0 => 1
    case n => (1 to n).reduce(_ * _)
  }

  val mFact: Int => Int = memoize(fact)

  // c++ getchar
  val getChar: Unit => Char = _ => Console.in.read.toChar

  val mGetChar: Unit => Char = memoize(getChar)

  // function with side-effect
  val printHelloReturnTrue: Unit => Boolean = _ => {
    println("Hello!")
    true
  }

  val mPrintHelloReturnTrue: Unit => Boolean = memoize(printHelloReturnTrue)

  /**
    * c++ function using static variable
    * int f(int x) {
    * static int y = 0;
    * y += x;
    * return y;
    * }
    */
  val rememberAndAdd: Int => Int = {
    var acc: Int = 0
    x => {
      acc = acc + x
      acc
    }
  }

  val mRememberAndAdd: Int => Int = memoize(rememberAndAdd)

  /**
    * properties of functions: f: X => Y
    * injective (one-to-one) => f(a) /= f(b) for any choice of a and b
    * surjective (onto) => range is equals to codomain, f(X) = Y
    * bijective (one-to-one correspondence) => injective && surjective
    *
    *
    * Number of functions from Boolean => Boolean
    * For category Hask, there will be 3 ^ 3 = 27 possible functions.
    * For category Set, there will be 2 ^ 2 = 4 possible functions.
    */
}
