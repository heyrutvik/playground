module Average

export
average : String -> Double
average str = let wordNum = wordCount str
                  totalChar = sum (allLength (words str)) in
                  cast totalChar / cast wordNum
  where
    allLength : List String -> List Nat
    allLength ss = map length ss

    wordCount : String -> Nat
    wordCount str = length (words str)

export
showAverage : String -> String
showAverage str = "Average word length of string [" ++ str ++ "] is " ++ (cast (average str)) ++ "\n"

main : IO ()
main = repl "Enter your name: " showAverage
