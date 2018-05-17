// write fizz-buzz program using most fundamental thing.. functions!

var debug = true;
separator = function() {
  console.log("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")
}

const range = (start, end) => (
  Array.from(Array(end - start + 1).keys()).map(i => i + start)
);

var fizzBuzzString = range(1, 100).map(n => {
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

if (debug) {
  console.log(fizzBuzzString.join(', '))
  separator()
}

/**
properties of a function:
1) plumbing - a function application is just about replacing parameters of the block with the arguments. (of course, if you just ignore the part of execution)
2) currying - a function with any number of parameters can be rewritten as as a function of single parameter.
3) equality - functions are interchangeable if they produce identical results when called with the same arguments. (regardless of their implementation)
*/

// number representation as function - aka Church Numerals

ZERO = function(f) {
  return function(x) {
    return x
  }
}

ONE = function(f) {
  return function(x) {
    return f(x)
  }
}

// method to convert church numerals(cn) to js number
toInt = function(cn) {
  return cn(function(n) {
    return n + 1
  })(0);
}
if (debug) {
  console.log("cn zero as js number: " +toInt(ZERO))
  console.log("cn one as js number: " +toInt(ONE))
  separator()
}