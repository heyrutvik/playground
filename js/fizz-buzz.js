/**
 * helper function to print output
 */
let print = function(msg, output) { 
  console.log(msg + ": " + output)
}

/**
 * fizz buzz program using javascript features
 */

const range = (start, end) => (
  Array.from(Array(end - start + 1).keys()).map(i => i + start)
);

let fizzBuzzJS = range(1, 100).map(n => {
  if (n % 15 === 0) {
    return 'FizzBuzz'
  } else if (n % 3 === 0) {
    return 'Fizz'
  } else if (n % 5 === 0) {
    return 'Buzz'
  } else {
    return n
  }
})

print("javascript fizz buzz output", fizzBuzzJS)

/**
 * let us try to write fizz buzz program using functions only... we are going to
 * encode everything using functions.. numbers, boolean and other operations!
 *
 * properties of a function:
 * (1) plumbing - a function application is just about replacing parameters of 
 *     the block with the arguments. (of course, if you just ignore the part of 
 *     execution)
 * (2) currying - a function with any number of parameters can be rewritten as 
 *     as a function of single parameter.
 * (3) equality - functions are interchangeable if they produce identical 
 *     results when called with the same arguments. (regardless of their 
 *     implementation)
 */

/**
 * number encoded as function - aka Church Numerals
 */

let ZERO = function(f) {
  return function(x) {
    return x
  }
}

let ONE = function(f) {
  return function(x) {
    return f(x)
  }
}

let TWO = function(f) {
  return function(x) {
    return f(f(x))
  }
}

let THREE = function(f) {
  return function(x) {
    return f(f(f(x)))
  }
}

/**
 * helper function to convert church numerals to javascript numbers
 */

let toInt = function(cn) {
  return cn(function(n) {
    return n + 1
  })(0);
}

print("ONE", toInt(ONE))
print("TWO", toInt(TWO))

/**
 * boolean encoded as function
 */

let TRUE = function(x) {
  return function(y) {
    return x
  }
}

let FALSE = function(x) {
  return function(y) {
    return y
  }
}

/**
 * helper function to convert church boolean to javascript boolean
 */

let toBoolean = function(cb) {
  return cb(true)(false)
}

print("TRUE", toBoolean(TRUE))
print("FALSE", toBoolean(FALSE))

/**
 * if condition encoded as function
 * it's just a syntactic sugar for calling chruch boolean
 */

let IF = function(cb) {
  return function(c) {
    return function(a) {
      return cb(c)(a)
    }
  }
}

print("IF TRUE", IF(TRUE)("happy")("sad"))
print("IF FALSE", IF(FALSE)("happy")("sad"))

/**
 * predicate to check if given number is zero or not
 */

let IS_ZERO = function(cn) {
  return cn(function(x) { return FALSE })(TRUE)
}

print("IS_ZERO ONE", toBoolean(IS_ZERO(ONE)))
print("IS_ZERO ZERO", toBoolean(IS_ZERO(ZERO)))

/**
 * PAIR data structure constructor
 */

let PAIR = function(x) {
  return function(y) {
    return function(f) {
      return f(x)(y)
    }
  }
}

print("PAIR constructor", PAIR(1)(2))

/**
 * PAIR data structure selector
 */

let LEFT = function(p) {
  return p(function(x) {
    return function(y) {
      return x
    }
  })
}

let RIGHT = function(p) {
  return p(function(x) {
    return function(y) {
      return y
    }
  })
}

print("PAIR LEFT selector", LEFT(PAIR(1)(2)))
print("PAIR RIGHT selector", RIGHT(PAIR(1)(2)))

/**
 * increment operation on chucrch numeral
 */

let INCREMENT = function(cn) {
  return function(f) {
    return function(x) {
      return f(cn(f)(x))
    }
  }
}

print("INCREMENT THREE", toInt(INCREMENT(THREE)))

/**
 * decrement operation using pair
 * (zero, zero) as input
 * slide as function
 * after appling n times, take left
 */

let SLIDE = function(p) {
  return PAIR(RIGHT(p))(INCREMENT(RIGHT(p)))
}

let DECREMENT = function(cn) {
  return LEFT(cn(SLIDE)(PAIR(ZERO)(ZERO)))
}

print("DECREMENT ONE", toInt(DECREMENT(ONE)))
print("DECREMENT THREE", toInt(DECREMENT(THREE)))

/**
 * addition operation using increment
 */

let ADD = function(cn1) {
  return function(cn2) {
    return cn2(INCREMENT)(cn1)
  }
}

print("ADD ONE, ONE", toInt(ADD(ONE)(ONE)))

/**
 * subtract operation using decrement
 */

let SUBTRACT = function(cn1) {
  return function(cn2) {
    return cn2(DECREMENT)(cn1)
  }
}

print("SUBTRACT TWO, ONE", toInt(SUBTRACT(TWO)(ONE)))

/**
 * multiply operation using add
 */

let MULTIPLY = function(cn1) {
  return function(cn2) {
    return cn2(ADD(cn1))(ZERO)
  }
}

print("MULTIPLY TWO, TWO", toInt(MULTIPLY(TWO)(TWO)))

/**
 * power operation using multiply
 */

let POWER = function(cn1) {
  return function(cn2) {
    return cn2(MULTIPLY(cn1))(ONE)
  }
}

print("POWER THREE, TWO", toInt(POWER(THREE)(TWO)))

/**
 * more numbers..
 */

let FIVE = ADD(THREE)(TWO)
let TEN = ADD(FIVE)(FIVE)
let FIFTEEN = MULTIPLY(FIVE)(THREE)
let HUNDRED = MULTIPLY(FIVE)(ADD(TEN)(TEN))

/**
 * compare operation: less than or equals to
 */

let IS_LESS_OR_EQUAL = function(cn1) {
  return function(cn2) {
    return IS_ZERO(SUBTRACT(cn1)(cn2))
  }
}

print("TWO <= ONE", toBoolean(IS_LESS_OR_EQUAL(TWO)(ONE)))
print("ONE <= TWO", toBoolean(IS_LESS_OR_EQUAL(ONE)(TWO)))
print("THREE <= THREE", toBoolean(IS_LESS_OR_EQUAL(THREE)(THREE)))

/**
 * cheated version of modulo operation
 */

let MOD_CHEAT = function(cn1) {
  return function(cn2) {
    return IF(IS_LESS_OR_EQUAL(cn2)(cn1))(
      function(x) { return MOD_CHEAT(SUBTRACT(cn1)(cn2))(cn2)(x) }
    )(
      cn1
    )
  }
}

/**
 * Y combinator to mimic recursion in the environment where we don't have
 * recursion construct built-in
 */

let Y = function(f) {
  return (function(x) { return f(x(x)) })(function(x) {return f(x(x)) })
}

/**
 * Z combinator is same as Y combinator but it's variant for strict-evaluation
 * machine
 */

let Z = function(f) {
  return (function(x) { 
    return f(function(y) { return x(x)(y) }) }
  )(function(x) {return f(function(y) { return x(x)(y) }) })
}

/**
 * modulo operation using Z combinator
 */

let MOD = Z(
  function(f) {
    return function(cn1) {
      return function(cn2) {
        return IF(IS_LESS_OR_EQUAL(cn2)(cn1))(
          function(x) { return f(SUBTRACT(cn1)(cn2))(cn2)(x) }
        )(
          cn1
        )
      }
    }
  }
)

print("100 % 3", toInt(MOD(ADD(HUNDRED)(ONE))(THREE)))

/**
 * cheated version of factorial operation
 */

let FACT_CHEAT = function(cn) {
  return IF(IS_LESS_OR_EQUAL(cn)(ONE))(
    ONE
  )(
    MULTIPLY(function(x) { return FACT_CHEAT(SUBTRACT(cn)(ONE))(x) })(cn)
  )
}

/**
 * factorial operation using Z combinator
 */

let FACT = Z(
  function(f){
    return function(cn) {
      return IF(IS_LESS_OR_EQUAL(cn)(ONE))(
        ONE
      )(
        MULTIPLY(function(x) { return f(SUBTRACT(cn)(ONE))(x) })(cn)
      )
    }
  }
)

print("factorial of FIVE", toInt(FACT(FIVE)))

/**
 * List data structure constructor
 */

let EMPTY = PAIR(TRUE)(TRUE)
let PREPEND = function(l) {
  return function(x) {
    return PAIR(FALSE)(PAIR(x)(l))
  }
}

/**
 * List data structure predicate
 */

let IS_EMPTY = LEFT

/**
 * List data structure selector
 */

let HEAD = function(l) {
  return LEFT(RIGHT(l))
}
let TAIL = function(l) {
  return RIGHT(RIGHT(l))
}

/**
 * helper function to process List data structure
 */

let toArray = function(proc) {
  return function(f) {
    var array = []
    while(!toBoolean(IS_EMPTY(proc))) {
      array.push(HEAD(proc))
      proc = TAIL(proc)
    }
    return array.map(n => f(n))
  }
}

let list = PREPEND(PREPEND(PREPEND(EMPTY)(ONE))(TWO))(THREE)
print("list using prepend operation", 
  toArray(list)(function(n) { return toInt(n) })
)
print("HEAD of list", toInt(HEAD(list)))
print("HEAD of TAIL of list", toInt(HEAD(TAIL(list))))
print("IS_EMPTY the TAIL of TAIL of TAIL of list", 
  toBoolean(IS_EMPTY(TAIL(TAIL(TAIL(list))))))

/**
 * range operation using Z combinator and list
 */

let RANGE = Z(
  function(f) {
    return function(cn1) {
      return function(cn2) {
        return IF(IS_LESS_OR_EQUAL(cn1)(cn2))(
          function(x) { return PREPEND(f(INCREMENT(cn1))(cn2))(cn1)(x) }
        )(
          EMPTY
        )
      }
    }
  }
)

print("RANGE from ONE to FIVE", toArray(RANGE(ONE)(FIVE))(function(n) { return toInt(n)}))

/**
 * fold operation using Z combinator
 */

let FOLD = Z(
  function(f) {
    return function(l) {
      return function(z) {
        return function(g) {
          return IF(IS_EMPTY(l))(
            z
          )(
            function(y) { return g(f(TAIL(l))(z)(g))(HEAD(l))(y)}
          )
        }
      }
    }
  }
)

print("FOLD [ONE..FIVE] using ZERO and ADD opeartion", 
  toInt(FOLD(RANGE(ONE)(FIVE))(ZERO)(ADD)))

/**
 * map operation on list using fold
 */
let MAP = function(k) {
  return function(f) {
    return FOLD(k)(EMPTY)(function(l) {
      return function(x) {
        return PREPEND(l)(f(x))
      }
    })
  }
}

print("MAP over [ONE..FIVE] using +2 operation", 
  toArray(MAP(RANGE(ONE)(FIVE))(ADD(TWO)))(function(n){return toInt(n)}))

/**
 * encoding string and operations
 */

let B = TEN
let F = INCREMENT(B)
let I = INCREMENT(F)
let U = INCREMENT(I)
let ZED = INCREMENT(U)

let FIZZ = PREPEND(PREPEND(PREPEND(PREPEND(EMPTY)(ZED))(ZED))(I))(F)
let BUZZ = PREPEND(PREPEND(PREPEND(PREPEND(EMPTY)(ZED))(ZED))(U))(B)
let FIZZBUZZ = PREPEND(PREPEND(PREPEND(PREPEND(BUZZ)(ZED))(ZED))(I))(F)

let toChar = function(c) {
  return '0123456789BFiuz'.charAt(toInt(c))
}

print("I to character", toChar(I))

let toString = function(s) {
  return (toArray(s)(function(c) {return toChar(c)})).join('')
}

print("FIZZBUZZ to string", toString(FIZZBUZZ))

/**
 * division operation using Z combinator
 */

let DIV = Z(
  function(f) {
    return function(cn1) {
      return function(cn2) {
        return IF(IS_LESS_OR_EQUAL(cn2)(cn1))(
          function(x) { return INCREMENT(f(SUBTRACT(cn1)(cn2))(cn2))(x) }
        )(
          ZERO
        )
      }
    }
  }
)

print("DIV HUNDRED, FIVE", toInt(DIV(HUNDRED)(FIVE)))

/**
 * List append operation
 */

let APPEND = function(l) {
  return function(x) {
    return FOLD(l)(PREPEND(EMPTY)(x))(PREPEND)
  }
}

print("append ONE to EMPTY list", toArray(APPEND(EMPTY)(ONE))(function(n){
  return toInt(n)
}))

/**
 * convert number to list of digit
 */

let TO_DIGITS = Z(
  function(f) {
    return function(cn) {
      return APPEND(
        IF(IS_LESS_OR_EQUAL(cn)(DECREMENT(TEN)))(
          EMPTY
        )(
          function(x) { return f(DIV(cn)(TEN))(x)}
        )
      )(MOD(cn)(TEN))
    }
  }
)

print("convert HUNDRED to list of ONE, ZERO, ZERO", 
  toArray(TO_DIGITS(HUNDRED))(function(x){return toInt(x)}))

/**
 * fizz buzz program using functions only
 */

let fizzBuzzLC = MAP(RANGE(ONE)(HUNDRED))(function(n) {
  return IF(IS_ZERO(MOD(n)(FIFTEEN)))(
    FIZZBUZZ
  )(IF(IS_ZERO(MOD(n)(THREE)))(
    FIZZ
  )(IF(IS_ZERO(MOD(n)(FIVE)))(
    BUZZ
  )(
    TO_DIGITS(n)
  )))
})

print("fizz buzz using functions only", 
  toArray(fizzBuzzLC)(function(n){return toString(n)}))
