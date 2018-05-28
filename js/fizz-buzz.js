// write fizz-buzz program using most fundamental thing.. functions!

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
//console.log(fizzBuzzStringJS)

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
let THREE = function(f) {
  return function(x) {
    return f(f(f(x)))
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

let FIVE = ADD(TWO)(THREE)

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

// less than or equals to
let LESS_THAN_EQUALS_TO = function(cn1) {
  return function(cn2) {
    return IS_ZERO(SUBTRACT(cn1)(cn2))
  }
}
/*
console.log(toBoolean(LESS_THAN_EQUALS_TO(TWO)(ONE)))
console.log(toBoolean(LESS_THAN_EQUALS_TO(ONE)(TWO)))
*/

// modulo
let MOD_CHEAT = function(cn1) {
  return function(cn2) {
    return IF(LESS_THAN_EQUALS_TO(cn2)(cn1))(
      function(x) { return MOD_CHEAT(SUBTRACT(cn1)(cn2))(cn2)(x) }
    )(
      cn1
    )
  }
}

/*
console.log(toInt(MOD_CHEAT(THREE)(TWO)))
*/

let Y = function(f) {
  return (function(x) { return f(x(x)) })(function(x) {return f(x(x)) })
}

//console.log(Y(function(x) {return x + 1}))

let Z = function(f) {
  return (function(x) { return f(function(y) { return x(x)(y) }) })(function(x) {return f(function(y) { return x(x)(y) }) })
}

/*
console.log(Z(function(x) {return x}))
*/

let MOD = Z(
	function(f) {
		return function(cn1) {
  		return function(cn2) {
				return IF(LESS_THAN_EQUALS_TO(cn2)(cn1))(
					function(x) { return f(SUBTRACT(cn1)(cn2))(cn2)(x) }
				)(
					cn1
				)
			}
		}
	}
)
/*
console.log(toInt(MOD(ADD(HUNDRED)(ONE))(THREE)))
*/
let FACT_CHEAT = function(cn) {
  return IF(LESS_THAN_EQUALS_TO(cn)(ONE))(
		ONE
	)(
		MULTIPLY(function(x) { return FACT_CHEAT(SUBTRACT(cn)(ONE))(x) })(cn)
	)
}
/*
console.log(toInt(FACT_CHEAT(THREE)))
*/
let FACT = Z(
	function(f){
		return function(cn) {
			return IF(LESS_THAN_EQUALS_TO(cn)(ONE))(
				ONE
			)(
				MULTIPLY(function(x) { return f(SUBTRACT(cn)(ONE))(x) })(cn)
			)
		}
	}
)
/*
console.log(toInt(FACT(FIVE)))
*/


// list

let EMPTY = PAIR(TRUE)(TRUE)
let PREPEND = function(l) {
	return function(x) {
		return PAIR(FALSE)(PAIR(x)(l))
	}
}
let IS_EMPTY = LEFT
let HEAD = function(l) {
	return LEFT(RIGHT(l))
}
let TAIL = function(l) {
	return RIGHT(RIGHT(l))
}

/*
let l = PREPEND(PREPEND(PREPEND(EMPTY)(ONE))(TWO))(THREE)
console.log(toInt(HEAD(l)))
console.log(toInt(HEAD(TAIL(l))))
console.log(toBoolean(IS_EMPTY(TAIL(TAIL(TAIL(l))))))
*/

let RANGE = Z(
	function(f) {
		return function(cn1) {
			return function(cn2) {
				return IF(LESS_THAN_EQUALS_TO(cn1)(cn2))(
					function(x) { return PREPEND(f(INCREMENT(cn1))(cn2))(cn1)(x) }
				)(
					EMPTY
				)
			}
		}
	}
)

/*
console.log(toInt(HEAD(TAIL(RANGE(TWO)(FIVE)))))
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

/*
console.log(toArray(RANGE(ONE)(FIVE))(function(n) { return toInt(n)}))
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
/*
console.log(toInt(FOLD(RANGE(ONE)(FIVE))(ZERO)(ADD)))
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

console.log(toArray(MAP(RANGE(ONE)(FIVE))(ADD(TWO)))(function(n){return toInt(n)}))

// string

let TEN = MULTIPLY(FIVE)(TWO)
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

/*
console.log(toChar(I))
*/

let toString = function(s) {
	return (toArray(s)(function(c) {return toChar(c)})).join('')
}

/*
console.log(toString(FIZZBUZZ))
*/

let DIV = Z(
	function(f) {
		return function(cn1) {
			return function(cn2) {
				return IF(LESS_THAN_EQUALS_TO(cn2)(cn1))(
					function(x) { return INCREMENT(f(SUBTRACT(cn1)(cn2))(cn2))(x) }
				)(
					ZERO
				)
			}
		}
	}
)

/*
console.log(toInt(DIV(HUNDRED)(FIVE)))
*/

let APPEND = function(l) {
	return function(x) {
		return FOLD(l)(PREPEND(EMPTY)(x))(PREPEND)
	}
}

/*
console.log(toArray(APPEND(EMPTY)(ONE))(function(n){return toInt(n)}))
*/

let TO_DIGITS = Z(
	function(f) {
		return function(cn) {
			return APPEND(
				IF(LESS_THAN_EQUALS_TO(cn)(DECREMENT(TEN)))(
					EMPTY
				)(
					function(x) { return f(DIV(cn)(TEN))(x)}
				)
			)(MOD(cn)(TEN))
		}
	}
)
/*
console.log(toArray(TO_DIGITS(HUNDRED))(function(x){return toInt(x)}))
*/

let FIFTEEN = MULTIPLY(FIVE)(THREE)

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

//console.log(toArray(fizzBuzzLC)(function(n){return toString(n)}))
