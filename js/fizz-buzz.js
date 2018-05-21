// write fizz-buzz program using most fundamental thing.. functions!

const range = (start, end) => (
  Array.from(Array(end - start + 1).keys()).map(i => i + start)
);

let fizzBuzzString = range(1, 100).map(n => {
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
//console.log(fizzBuzzString.join(', '))

/**
properties of a function:
1) plumbing - a function application is just about replacing parameters of the block with the arguments. (of course, if you just ignore the part of execution)
2) currying - a function with any number of parameters can be rewritten as as a function of single parameter.
3) equality - functions are interchangeable if they produce identical results when called with the same arguments. (regardless of their implementation)
*/

// number representation as function - aka Church Numerals
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
let HUNDRED = function(f) {
  return function(x) {
    return f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(x))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))
  }
}

// method to convert church numerals(cn) to js number
let toInt = function(cn) {
  return cn(function(n) {
    return n + 1
  })(0);
}
/*
console.log("church numeral zero as js number: " +toInt(ZERO))
console.log("church numeral one as js number: " +toInt(ONE))
console.log("church numeral two as js number: " +toInt(TWO))
console.log("church numeral hundred as js number: " +toInt(HUNDRED))
*/

// church boolean
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

// method to convert church boolean(cb) to js boolean
let toBoolean = function(cb) {
  return cb(true)(false)
}
/*
console.log(toBoolean(TRUE))
console.log(toBoolean(FALSE))
*/

// church if condition
let IF = function(cb) {
  return function(c) {
    return function(a) {
      return cb(c)(a)
    }
  }
}
/*
console.log(IF(TRUE)("happy")("sad"))
console.log(IF(FALSE)("happy")("sad"))
*/

// === 0 predicate
let IS_ZERO = function(cn) {
  return cn(function(x) { return FALSE })(TRUE)
}
/*
console.log(toBoolean(IS_ZERO(ONE)))
console.log(toBoolean(IS_ZERO(ZERO)))
*/


// pair data structure
let PAIR = function(x) {
  return function(y) {
    return function(f) {
      return f(x)(y)
    }
  }
}
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
/*
let pair1 = PAIR(1)(2)
console.log(LEFT(pair1))
console.log(RIGHT(pair1))
*/

// increment operation on church numeral
let INCREMENT = function(cn) {
  return function(f) {
    return function(x) {
      return f(cn(f)(x))
    }
  }
}
/*
console.log(toInt(INCREMENT(HUNDRED)))
*/

/* decrement using pair
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
/*
console.log(toInt(DECREMENT(ONE)))
console.log(toInt(DECREMENT(HUNDRED)))
*/

// addition
let ADD = function(cn1) {
  return function(cn2) {
    return cn2(INCREMENT)(cn1)
  }
}
/*
console.log(toInt(ADD(ONE)(ONE)))
*/

// subtract
let SUBTRACT = function(cn1) {
  return function(cn2) {
    return cn2(DECREMENT)(cn1)
  }
}
/*
console.log(toInt(SUBTRACT(TWO)(TWO)))
*/

// multiply
let MULTIPLY = function(cn1) {
  return function(cn2) {
    return cn2(ADD(cn1))(ZERO)
  }
}
/*
console.log(toInt(MULTIPLY(TWO)(TWO)))
*/

// power
let POWER = function(cn1) {
  return function(cn2) {
    return cn2(MULTIPLY(cn1))(ONE)
  }
}
/*
console.log(toInt(POWER(TWO)(TWO)))
*/
