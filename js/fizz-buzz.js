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
